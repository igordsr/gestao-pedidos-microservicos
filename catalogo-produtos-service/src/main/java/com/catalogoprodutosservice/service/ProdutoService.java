package com.catalogoprodutosservice.service;

import com.catalogoprodutosservice.controller.exception.modal.EntidadeNaoProcessavelException;
import com.catalogoprodutosservice.controller.exception.modal.RegistroJaExisteException;
import com.catalogoprodutosservice.controller.exception.modal.RegistroNaoEncontradoException;
import com.catalogoprodutosservice.controller.exception.modal.SolicitacaoInvalidaException;
import com.catalogoprodutosservice.dto.ProdutoDTO;
import com.catalogoprodutosservice.model.Produto;
import com.catalogoprodutosservice.repository.ProdutoRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final FileUploadService fileUploadService;
    private final JobLauncher jobLauncher;
    private final Job job;
    private final Resource inputFile;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository, FileUploadService fileUploadService, JobLauncher jobLauncher, Job job, @Value("${inputFile}") Resource inputFile) {
        this.produtoRepository = produtoRepository;
        this.fileUploadService = fileUploadService;
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

    public void criarProdutosFromFile(MultipartFile file, LocalDateTime executionDate) throws IOException {
        this.fileUploadService.uploadFile(this.inputFile.getFilename(), file);
        if (Objects.isNull(executionDate)) {
            this.importarProdutos();
        } else {
            this.agendarImportacao(executionDate);
        }
    }

    public void agendarImportacao(LocalDateTime horaAgendada) {
        long delayInicial = calcularDelayInicial(horaAgendada);
//        scheduler.close();
        scheduler.schedule(() -> {
            try {
                importarProdutos();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, delayInicial, TimeUnit.SECONDS);
    }

    private long calcularDelayInicial(LocalDateTime horaAgendada) {
        if (horaAgendada.isBefore(LocalDateTime.now())) {
            throw new SolicitacaoInvalidaException("A data agendada não pode ser uma data no passado.");
        }
        long delay = TimeUnit.SECONDS.toSeconds(horaAgendada.toEpochSecond(java.time.ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC));
        if (delay < 0) {
            delay += TimeUnit.DAYS.toSeconds(1);
        }
        return delay;
    }

    public void importarProdutos() throws IOException {
        try {

            JobParameters jobParameters = new JobParametersBuilder()
                    .addDate("timestamp", Calendar.getInstance().getTime())
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
        } catch (JobInstanceAlreadyCompleteException e) {
            this.fileUploadService.deleteFile(this.inputFile.getFilename());
            throw new RuntimeException(e);
        } catch (JobExecutionAlreadyRunningException e) {
            this.fileUploadService.deleteFile(this.inputFile.getFilename());
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            this.fileUploadService.deleteFile(this.inputFile.getFilename());
            throw new RuntimeException(e);
        } catch (JobRestartException e) {
            this.fileUploadService.deleteFile(this.inputFile.getFilename());
            throw new RuntimeException(e);
        }
    }

}
