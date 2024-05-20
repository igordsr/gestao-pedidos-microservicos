package com.catalogoprodutosservice.configuration;

import com.catalogoprodutosservice.model.Produto;
import com.catalogoprodutosservice.repository.ProdutoRepository;
import com.catalogoprodutosservice.service.FileUploadService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class BatchConfiguration {

    @Bean
    public Job processarProduto(JobRepository jobRepository, Step step) {
        return new JobBuilder("processarProduto", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, ItemReader<Produto> itemReader, ItemProcessor<Produto, Produto> itemProcessor, ItemWriter<Produto> itemWriter) {
        return new StepBuilder("step", jobRepository)
                .<Produto, Produto>chunk(20, platformTransactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Produto> itemReader(@Value("${inputFile}") Resource filePath, FileUploadService fileUploadService) throws IOException {
        ByteArrayOutputStream outputStream;
        try {
            // Baixar o arquivo do Azure Blob Storage
            outputStream = fileUploadService.downloadFile(filePath.getFilename());
            if (outputStream == null) {
                throw new IOException("File not found in Azure Blob Storage");
            }
        } catch (Exception e) {
            throw new IOException("Failed to download file from Azure Blob Storage", e);
        }

        Path tempFile = Files.createTempFile("produtos-", ".csv");
        Files.write(tempFile, outputStream.toByteArray());

        BeanWrapperFieldSetMapper<Produto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Produto.class);

        FlatFileItemReader<Produto> reader = new FlatFileItemReaderBuilder<Produto>()
                .name("produtoItemReader")
                .resource(new PathResource(tempFile))
                .delimited()
                .names("nome", "descricao", "preco", "qtdEstoque")
                .fieldSetMapper(fieldSetMapper)
                .build();

        reader.open(new ExecutionContext());
        return reader;
    }

    @Bean
    public ItemProcessor<Produto, Produto> itemProcessor() {
        return new ProdutoProcessor();
    }

    @Bean
    public ItemWriter<Produto> itemWriter(ProdutoRepository produtoRepository) {
        return produtoRepository::saveAll;
    }
}