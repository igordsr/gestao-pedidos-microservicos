package com.catalogoprodutosservice.service;

import com.catalogoprodutosservice.controller.exception.modal.EntidadeNaoProcessavelException;
import com.catalogoprodutosservice.controller.exception.modal.RegistroJaExisteException;
import com.catalogoprodutosservice.controller.exception.modal.RegistroNaoEncontradoException;
import com.catalogoprodutosservice.dto.ProdutoDTO;
import com.catalogoprodutosservice.model.Produto;
import com.catalogoprodutosservice.repository.ProdutoRepository;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final FileUploadController fileUploadController;
    private final JobLauncher jobLauncher;
    private final Job job;
    private final Resource inputFile;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository, FileUploadController fileUploadController, JobLauncher jobLauncher, Job job, @Value("${inputFile}") Resource inputFile) {
        this.produtoRepository = produtoRepository;
        this.fileUploadController = fileUploadController;
        this.jobLauncher = jobLauncher;
        this.job = job;
        this.inputFile = inputFile;
    }

    public ProdutoDTO cadastrar(final ProdutoDTO produtoDTO) {
        Optional<Produto> produtoOptional = this.produtoRepository.findByNomeIgnoreCase(produtoDTO.nome());
        if (produtoOptional.isPresent()) {
            throw new RegistroJaExisteException(produtoOptional.get().getNome());
        }
        final Produto produto = produtoRepository.save(produtoDTO.toProduto());
        return ProdutoDTO.getInstance(produto);
    }

    public List<ProdutoDTO> listarProdutos() {
        return produtoRepository.findByStatusTrue().stream().map(ProdutoDTO::getInstance).toList();
    }

    public ProdutoDTO encontrarProdutoPorId(UUID id) {
        final Produto produto = this.findById(id);
        return ProdutoDTO.getInstance(produto);
    }

    public List<ProdutoDTO> encontrarProdutosPorIds(List<UUID> id) {
        List<Produto> produtos = this.produtoRepository.findAllById(id);
        final List<ProdutoDTO> produtoDTOS = new ArrayList<>();
        for (Produto produto : produtos) {
            ProdutoDTO instance = ProdutoDTO.getInstance(produto);
            produtoDTOS.add(instance);
        }
        return produtoDTOS;
    }

    public ProdutoDTO atualizarProduto(final UUID id, final ProdutoDTO produtoDTO) {
        Produto produto = this.findById(id);
        produto.setNome(produtoDTO.nome());
        produto.setDescricao(produtoDTO.descricao());
        produto.setPreco(produtoDTO.preco());
        produto.setQtdEstoque(produtoDTO.qtdEstoque());
        produto = this.produtoRepository.save(produto);
        return ProdutoDTO.getInstance(produto);
    }

    public void deletarProduto(UUID id) {
        Produto produto = this.findById(id);
        produto.setStatus(Boolean.FALSE);
        this.produtoRepository.save(produto);
    }

    @Transactional
    public ProdutoDTO decrementarEstoque(UUID id, int quantidade) {
        Produto produto = this.findById(id);
        if (produto.getQtdEstoque() < quantidade) {
            final String resp = "Não há quantidade [%s] suficiente disponível do produto [%s] para a venda solicitada. Por favor, ajuste a quantidade ou escolha outro produto";
            throw new EntidadeNaoProcessavelException(String.format(resp, produto.getQtdEstoque(), produto.getNome()));
        }
        int novoEstoque = produto.getQtdEstoque() - quantidade;
        produto.setQtdEstoque(novoEstoque);
        produto = this.produtoRepository.save(produto);
        return ProdutoDTO.getInstance(produto);
    }

    @Transactional
    public ProdutoDTO incrementarEstoque(UUID id, Integer quantidade) {
        Produto produto = this.findById(id);
        int novoEstoque = produto.getQtdEstoque() + quantidade;
        produto.setQtdEstoque(novoEstoque);
        produto = this.produtoRepository.save(produto);
        return ProdutoDTO.getInstance(produto);
    }

    private Produto findById(UUID id) {
        return this.produtoRepository.findById(id).orElseThrow(() -> new RegistroNaoEncontradoException(id.toString()));
    }

    public BatchStatus criarProdutosFromFile(MultipartFile file, LocalDateTime executionDate) throws IOException {
        try {
            this.fileUploadController.uploadFile(this.inputFile.getFilename(), file);
            Date time = this.convertToLocalDateTimeToDate(executionDate);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addDate("timestamp", Calendar.getInstance().getTime())
                    .toJobParameters();
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            while (jobExecution.isRunning()) {
                System.out.println("..................");
            }
            return jobExecution.getStatus();
        }
        catch (IOException e) {
            this.fileUploadController.deleteFile(this.inputFile.getFilename());
            throw new RuntimeException(e);
        }
        catch (JobInstanceAlreadyCompleteException e) {
            this.fileUploadController.deleteFile(this.inputFile.getFilename());
            throw new RuntimeException(e);
        }
        catch (JobExecutionAlreadyRunningException e) {
            this.fileUploadController.deleteFile(this.inputFile.getFilename());
            throw new RuntimeException(e);
        }
        catch (JobParametersInvalidException e) {
            this.fileUploadController.deleteFile(this.inputFile.getFilename());
            throw new RuntimeException(e);
        }
        catch (JobRestartException e) {
            this.fileUploadController.deleteFile(this.inputFile.getFilename());
            throw new RuntimeException(e);
        }
    }

    private Date convertToLocalDateTimeToDate(LocalDateTime executionDate){
        if(Objects.isNull(executionDate)){
            executionDate = LocalDateTime.now();
        }
        return Date.from(executionDate.atZone(ZoneId.systemDefault()).toInstant());
    }

}
