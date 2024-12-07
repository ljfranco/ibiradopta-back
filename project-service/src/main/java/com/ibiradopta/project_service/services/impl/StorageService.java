package com.ibiradopta.project_service.services.impl;

import com.ibiradopta.project_service.services.IStorageService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class StorageService implements IStorageService {

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.url}")
    private String bucketUrl;

    @Autowired
    private MinioClient minioClient;

    @Override
    public List<String> uploadFiles(MultipartFile[] files, String folderName) throws IOException {

        if(Arrays.stream(files).anyMatch(MultipartFile::isEmpty)) {
            return null;
        }

        try {
            List<String> fileUrls = new ArrayList<>();

            for (MultipartFile file : files) {
                InputStream inputStream = file.getInputStream();
                String originalFilename = file.getOriginalFilename();
                String fileExtension = StringUtils.getFilenameExtension(originalFilename);
                String objectName = folderName + "/" + UUID.randomUUID().toString() + "." + fileExtension;

                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(inputStream, inputStream.available(), -1)
                                .build());
                // Construir la URL base del objeto
                String fileUrl = bucketUrl + "/" + bucketName + "/" + objectName;

                // Añadir la URL a la lista
                fileUrls.add(fileUrl);

            }
            return fileUrls;

        }catch (MinioException e) {
            throw new IOException(e.getMessage());
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFiles(List<String> fileUrls) {
        String reference = "/"+bucketName+"/";
        int characters = reference.length();
        try {
            for (String fileUrl : fileUrls) {
                String objectName = fileUrl.substring(fileUrl.lastIndexOf(reference) + characters);
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .build());
            }
        } catch (MinioException | IOException e) {
            // Manejar la excepción si ocurre un error al eliminar los archivos
            throw new RuntimeException("Error deleting files from MinIO", e);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
