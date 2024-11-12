package com.ibiradopta.project_service.services;

import com.ibiradopta.project_service.models.Project;

import java.util.List;

public interface IProjectService {
    List<Project> getAllProjects();
    Project getProjectById(Integer id);
    Project getProjectByName(String name);
    //get projects by filters (Date Range,name,location)
    List<Project> getProjectsByFilters(String name, String location, String startDate, String endDate);
    Project saveProject(Project project);
    //Metodo para PUT
    Project updateProjectFull(Project project);
    //Metodo para PATCH
    Project updateProjectPartial(Project project);

    Boolean deleteProject(Integer id);
}
