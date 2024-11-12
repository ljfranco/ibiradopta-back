package com.ibiradopta.project_service.services.impl;

import com.ibiradopta.project_service.exceptions.ResourceNotFoundException;
import com.ibiradopta.project_service.models.Project;
import com.ibiradopta.project_service.repositories.IProjectRepository;
import com.ibiradopta.project_service.services.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService implements IProjectService {


    @Autowired
    private IProjectRepository projectRepository;

    @Override
    public List<Project> getAllProjects() {
        //get all projects
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Integer id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    @Override
    public Project getProjectByName(String name) {
        return projectRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    @Override
    public List<Project> getProjectsByFilters(String name, String location, String startDate, String endDate) {
        // get Projects by filter or filters (Date Range,name,location)
        return projectRepository.findByFilters(name, location, startDate, endDate);

    }

    @Override
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project updateProjectFull(Project project) {
        //Verificar si existe el proyecto
        projectRepository.findById(project.getId()).orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + project.getId()));
        return projectRepository.save(project);
    }

    @Override
    public Project updateProjectPartial(Project project) {
        Project existingProject = projectRepository.findById(project.getId()).orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (project.getName() != null) existingProject.setName(project.getName());
        if (project.getLocation() != null) existingProject.setLocation(project.getLocation());
        if (project.getImageUrl() != null) existingProject.setImageUrl(project.getImageUrl());
        if (project.getDescription() != null) existingProject.setDescription(project.getDescription());
        if (project.getEndDate() != null) existingProject.setEndDate(project.getEndDate());
        if (project.getIsFinished() != null) existingProject.setIsFinished(project.getIsFinished());
        // Actualiza otros campos según sea necesario

        return projectRepository.save(existingProject);
    }

    @Override
    public Boolean deleteProject(Integer id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        try {
            projectRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
