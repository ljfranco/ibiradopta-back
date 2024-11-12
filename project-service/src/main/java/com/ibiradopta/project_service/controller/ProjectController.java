package com.ibiradopta.project_service.controller;

import com.ibiradopta.project_service.models.Project;
import com.ibiradopta.project_service.services.impl.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/getall")
    public ResponseEntity<List<Project>> getProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    //Get by Path Variable "Id"
    @GetMapping("/id/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    //Get by Request Params "Name"
    @GetMapping("/name")
    public ResponseEntity<Project> getProjectByName(@RequestParam String name) {
        return ResponseEntity.ok(projectService.getProjectByName(name));
    }

    //get by filters Request Params "name", "location", "startDate", "endDate"
    @GetMapping("/filters")
    public ResponseEntity<List<Project>> getProjectsByFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(projectService.getProjectsByFilters(name, location, startDate, endDate));
    }

    @PostMapping("/save")
    public ResponseEntity<Project> saveProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.saveProject(project));
    }

    @PutMapping("/update")
    public ResponseEntity<Project> updateProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.updateProjectFull(project));
    }

    @PatchMapping("/update")
    public ResponseEntity<Project> updateProjectPartial(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.updateProjectPartial(project));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteProject(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.deleteProject(id));
    }

}
