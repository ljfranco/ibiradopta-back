package com.ibiradopta.project_service.repositories;

import com.ibiradopta.project_service.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IImageRepository extends JpaRepository<Image, String> {
    List<Image> findByProjectId(Long projectId);
    void deleteByProjectId(Long projectId);
}
