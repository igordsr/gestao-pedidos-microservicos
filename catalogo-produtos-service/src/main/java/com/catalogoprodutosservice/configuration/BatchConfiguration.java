package com.catalogoprodutosservice.configuration;

import com.catalogoprodutosservice.model.Produto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    @Bean
    public Job processarProduto(JobRepository jobRepository, Flow splitFlow) {
        return new JobBuilder("processarProduto", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(splitFlow)
                .end()
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, ItemReader<Produto> itemReader, ItemWriter<Produto> itemWriter, ItemProcessor<Produto, Produto> itemProcessor) {
        return new StepBuilder("step", jobRepository)
                .<Produto, Produto>chunk(20, platformTransactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, Tasklet tasklet) {
        return new StepBuilder("log4fun", jobRepository)
                .tasklet(tasklet, platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet tasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("Esperar 10 se");
            Thread.sleep(10000);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public ItemReader<Produto> itemReader() {
        BeanWrapperFieldSetMapper<Produto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType((Produto.class));

        return new FlatFileItemReaderBuilder<Produto>()
                .name("produtoItemReader")
                .resource(new ClassPathResource("produto.csv"))
                .delimited()
                .names("nome", "descricao", "preco", "qtdEstoque").fieldSetMapper(fieldSetMapper).build();
    }

    @Bean
    public ItemWriter<Produto> itemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Produto>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .dataSource(dataSource)
                .sql("INSERT INTO produtos (id, nome, descricao, preco, qtd_estoque, status) VALUES (:id, :nome, :descricao, :preco, :qtdEstoque, :status)").build();
    }

    @Bean
    public ItemProcessor<Produto, Produto> itemProcessor() {
        return new ProdutoProcessor();

    }

    @Bean
    public Flow splitFlow(Step step, Step step2) {
        return new FlowBuilder<SimpleFlow>("simpleFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(flow(step), flow(step2))
                .build();
    }

    private SimpleFlow flow(Step step) {
        return new FlowBuilder<SimpleFlow>("simpleFlow")
                .start(step)
                .build();
    }
}
