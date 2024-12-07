package com.ibiradopta.project_service.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public interface IStorageService {

    public List<String> uploadFiles(MultipartFile[] files, String folderName) throws IOException;
}
