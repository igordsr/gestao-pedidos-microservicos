package com.catalogoprodutosservice.configuration;

import com.catalogoprodutosservice.model.Produto;
import com.catalogoprodutosservice.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
@RequiredArgsConstructor
public class BatchConfiguration extends DefaultBatchConfiguration {

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private Produto produto;

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ItemReader<Produto> produtosItemReader;


    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public Produto produto() {
        return new Produto();
    }


    @Bean
    public Job importProdutoJob() {
        return new JobBuilder("importProdutoJob", jobRepository)
                .start(importProdutoStep(jobRepository, produto, transactionManager))
                .build();
    }

    @Bean
    public Step importProdutoStep(JobRepository jobRepository, Produto produto, PlatformTransactionManager transactionManager) {
        return new StepBuilder("importProdutoStep", jobRepository)
                .<Produto, Produto>chunk(100, transactionManager)
                .reader(produtosItemReader)
                .processor(itemProcessor())
                .writer(writer())
                .build();
    }

    @Bean
    public ItemProcessor<Produto, Produto> itemProcessor() {
        return new ProdutoProcessor();
    }

    @Bean
    public ItemWriter<Produto> writer() {
        return produtoRepository::saveAll;
    }

    @Bean
    public FlatFileItemReader<Produto> flatFileItemReader(@Value("${inputFile}") Resource inputFile) {
        FlatFileItemReader<Produto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setName("DEVAL");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setResource(inputFile);
        flatFileItemReader.setLineMapper(linMappe());
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<Produto> linMappe() {
        DefaultLineMapper<Produto> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("nome", "descricao", "preco", "qtdEstoque");
        lineTokenizer.setStrict(false);
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper fieldSetMapper = new BeanWrapperFieldSetMapper();
        fieldSetMapper.setTargetType(Produto.class);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;

    }

}
