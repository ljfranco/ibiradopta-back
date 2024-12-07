package com.ibiradopta.project_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibiradopta.project_service.exceptions.ProjectAlreadyExistsException;
import com.ibiradopta.project_service.exceptions.ProjectBlockedException;
import com.ibiradopta.project_service.models.Project;
import com.ibiradopta.project_service.services.impl.ProjectService;
import com.ibiradopta.project_service.services.impl.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private StorageService storageService;

    @Operation(summary = "Get all projects", description = "Obtain all existing projects, the respone is a list of projects")
    @GetMapping("/getall")
    public ResponseEntity<List<Project>> getProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    //Get by Path Variable "Id"
    @Operation(summary = "Get a project by Id", description = "Obtain an existing project, the respone is the project")
    @GetMapping("/id/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    //Get by Request Params "Name"
    @Operation(summary = "Get a project by name", description = "Obtain an existing project, the respone is the project")
    @GetMapping("/name")
    public ResponseEntity<Project> getProjectByName(@RequestParam String name) {
        return ResponseEntity.ok(projectService.getProjectByName(name));
    }

    //get by filters Request Params "name", "location", "startDate", "endDate"
    @Operation(summary = "Get projects by filters", description = "Obtain existing projects by filters, the respone is a list of projects")
    @GetMapping("/filters")
    public ResponseEntity<List<Project>> getProjectsByFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(projectService.getProjectsByFilters(name, location, startDate, endDate));
    }

    @Operation(summary = "Save a project", description = "Save a new project, the respone is the saved project")
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_Administrador')")
    @Transactional
    public ResponseEntity<Project> saveProject(@RequestPart("projectJson") Project project, @RequestPart("files") MultipartFile[] files) throws ProjectAlreadyExistsException, IOException {


        if (projectService.doesProjectExist(project.getName())){
            throw new ProjectAlreadyExistsException("Project already exists");
        }

        return ResponseEntity.ok(projectService.saveProject(project, files));
    }

    @Operation(summary = "Update a project", description = "Update an existing project, the respone is the updated project")
    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_Administrador')")
    public ResponseEntity<Project> updateProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.updateProjectFull(project));
    }

    @Operation(summary = "Update a project partially", description = "Update an existing project partially, the respone is the updated project")
    @PatchMapping("/update")
    @PreAuthorize("hasRole('ROLE_Administrador')")
    public ResponseEntity<Project> updateProjectPartial(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.updateProjectPartial(project));
    }

    @Operation(summary = "Add images to a project", description = "Add new images to an existing project")
    @PostMapping("/addImages/{projectId}")
    @PreAuthorize("hasRole('ROLE_Administrador')")
    public ResponseEntity<Project> addImagesToProject(
            @PathVariable Integer projectId,
            @RequestParam("images") MultipartFile[] images) throws IOException {
        Project updatedProject = projectService.addImagesToProject(projectId, images);
        return ResponseEntity.ok(updatedProject);
    }

    @Operation(summary = "Remove images from a project", description = "Remove images from an existing project")
    @DeleteMapping("/removeImages/{projectId}")
    @PreAuthorize("hasRole('ROLE_Administrador')")
    public ResponseEntity<Project> removeImagesFromProject(
            @PathVariable Integer projectId,
            @RequestParam("imageUrls") List<String> imageUrls) {
        Project updatedProject = projectService.removeImagesFromProject(projectId, imageUrls);
        return ResponseEntity.ok(updatedProject);
    }

    @Operation(summary = "Update the main picture", description = "Update the main picture of an existing project")
    @PutMapping("/main-image/{projectId}")
    @PreAuthorize("hasRole('ROLE_Administrador')")
    public ResponseEntity<Project> updateMainImage(@PathVariable Integer projectId, @RequestParam String mainImageId) {
        Project updatedProject = projectService.updateMainImage(projectId, mainImageId);
        return ResponseEntity.ok(updatedProject);
    }


    @Operation(summary = "Delete a project", description = "Delete an existing project, the respone is a boolean")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_Administrador')")
    public ResponseEntity<Boolean> deleteProject(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.deleteProject(id));
    }



}
