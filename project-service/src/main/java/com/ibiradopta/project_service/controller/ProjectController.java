package com.ibiradopta.project_service.controller;

import com.ibiradopta.project_service.models.Project;
import com.ibiradopta.project_service.services.impl.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

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
    @PostMapping("/save")
    public ResponseEntity<Project> saveProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.saveProject(project));
    }

    @Operation(summary = "Update a project", description = "Update an existing project, the respone is the updated project")
    @PutMapping("/update")
    public ResponseEntity<Project> updateProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.updateProjectFull(project));
    }

    @Operation(summary = "Update a project partially", description = "Update an existing project partially, the respone is the updated project")
    @PatchMapping("/update")
    public ResponseEntity<Project> updateProjectPartial(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.updateProjectPartial(project));
    }

    @Operation(summary = "Delete a project", description = "Delete an existing project, the respone is a boolean")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteProject(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.deleteProject(id));
    }

}
