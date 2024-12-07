package com.ibiradopta.project_service.services;

import com.ibiradopta.project_service.models.Image;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IImageService {
    List<Image> getAllImages();

    List<Image> getImagesByProductId(Long idProduct);
}
