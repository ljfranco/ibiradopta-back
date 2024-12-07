package com.ibiradopta.project_service.services;

import com.ibiradopta.project_service.models.Project;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProjectService {
    List<Project> getAllProjects();
    Project getProjectById(Integer id);
    Project getProjectByName(String name);
    //get projects by filters (Date Range,name,location)
    List<Project> getProjectsByFilters(String name, String location, String startDate, String endDate);

    Project saveProject(Project project, MultipartFile[] images) throws IOException;

    //Metodo para PUT
    Project updateProjectFull(Project project);
    //Metodo para PATCH
    Project updateProjectPartial(Project project);

    Project addImagesToProject(Integer projectId, MultipartFile[] images) throws IOException;

    Project removeImagesFromProject(Integer projectId, List<String> imageUrls);

    Boolean deleteProject(Integer id);
}
