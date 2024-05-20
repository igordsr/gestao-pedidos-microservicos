package com.catalogoprodutosservice.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;
import com.catalogoprodutosservice.controller.exception.modal.SolicitacaoInvalidaException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Logger;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadService {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;
    private static final Logger logger = Logger.getLogger(FileUploadService.class.getName());

    public String uploadFile(final String fileName, MultipartFile file) throws IOException {
        if (!Objects.equals(file.getContentType(), "text/csv")) {
            throw new SolicitacaoInvalidaException("O arquivo enviado não é um arquivo CSV.");
        }
        try (InputStream inputStream = file.getInputStream()) {
            BlobContainerClient containerClient = new BlobContainerClientBuilder()
                    .connectionString(connectionString)
                    .containerName(containerName)
                    .buildClient();

            BlobClient blobClient = containerClient.getBlobClient(fileName);
            blobClient.upload(inputStream, file.getSize(), true);
            return fileName;
        } catch (BlobStorageException ex) {
            throw new IOException("Failed to upload file to Azure Blob Storage", ex);
        }
    }

    public ByteArrayOutputStream downloadFile(final String fileName) throws IOException {
        try {
            BlobContainerClient containerClient = new BlobContainerClientBuilder()
                    .connectionString(connectionString)
                    .containerName(containerName)
                    .buildClient();

            BlobClient blobClient = containerClient.getBlobClient(fileName);
            if (!blobClient.exists()) {
                return null;
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blobClient.downloadStream(outputStream);
            return outputStream;
        } catch (BlobStorageException ex) {
            throw new IOException("Failed to download file from Azure Blob Storage", ex);
        }
    }

    public void deleteFile(String fileName) throws IOException {
        try {
            BlobContainerClient containerClient = new BlobContainerClientBuilder()
                    .connectionString(connectionString)
                    .containerName(containerName)
                    .buildClient();

            BlobClient blobClient = containerClient.getBlobClient(fileName);

            if (!blobClient.exists()) {
                return;
            }
            blobClient.delete();
            logger.info(String.format("Arquivo removido com sucesso: %s", fileName));
        } catch (BlobStorageException ex) {
            throw new IOException("Falha ao remover arquivo do Azure Blob Storage", ex);
        }
    }
}
