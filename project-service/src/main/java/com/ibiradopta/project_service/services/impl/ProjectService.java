package com.ibiradopta.project_service.services.impl;

import com.ibiradopta.project_service.exceptions.ProjectBlockedException;
import com.ibiradopta.project_service.exceptions.ResourceNotFoundException;
import com.ibiradopta.project_service.models.Image;
import com.ibiradopta.project_service.models.Payment;
import com.ibiradopta.project_service.models.Project;
import com.ibiradopta.project_service.repositories.IImageRepository;
import com.ibiradopta.project_service.repositories.IPaymentRepository;
import com.ibiradopta.project_service.repositories.IProjectRepository;
import com.ibiradopta.project_service.services.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService implements IProjectService {


    @Autowired
    private IProjectRepository projectRepository;

    @Autowired
    private IImageRepository imageRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private IPaymentRepository paymentRepository;

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
    public Project saveProject(Project project, MultipartFile[] images) throws IOException {

        List<String> imageUrls = null;

        try {
            //Guardar el proyecto en la base de datos
            Project savedProject = projectRepository.save(project);

            //Subir las imagenes a MinIo y guardar susURLs en la base de datos
            imageUrls = storageService.uploadFiles(images, String.valueOf(savedProject.getId()));

            //Guardar las URLs de las imagenes en la base de datos
            for (String imageUrl : imageUrls) {
                imageRepository.save(new Image(imageUrl, 0, savedProject));
            }

            //Actualizar el proyecto con las imagenes guardadas
            savedProject.setImages(imageRepository.findByProjectId(Long.valueOf(savedProject.getId())));
            return projectRepository.save(savedProject);
        } catch (IOException e) {
            // Si ocurre cualquier error, eliminar las imágenes subidas en MinIO
            if (imageUrls != null) {
                storageService.deleteFiles(imageUrls);  // Metodo para eliminar las imágenes
            }
            throw new IOException("Error saving project", e);  // Lanzar excepción para realizar rollback
        }
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

    @Transactional
    public Project updateMainImage(Integer projectId, String mainImageId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // Restablecer el imageOrder de todas las imágenes a 0
        for (Image image : project.getImages()) {
            image.setImageOrder(0);
        }

        // Encontrar la nueva imagen principal y establecer imageOrder = 1
        Image newMainImage = project.getImages().stream()
                .filter(image -> image.getImageUrl().equals(mainImageId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Main image not found"));

        newMainImage.setImageOrder(1);

        // Guardar las imágenes con el orden actualizado
        imageRepository.saveAll(project.getImages());

        return project;
    }


    @Transactional
    public Project addImagesToProject(Integer projectId, MultipartFile[] images) throws IOException {
        // Buscar el proyecto existente
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // Subir las nuevas imágenes a MinIO
        List<String> imageUrls = storageService.uploadFiles(images, String.valueOf(projectId));

        //Guardar las URLs de las imagenes en la base de datos
        for (String imageUrl : imageUrls) {
            imageRepository.save(new Image(imageUrl, 0, project));
        }

        //Actualizar el proyecto con las imagenes guardadas
        project.setImages(imageRepository.findByProjectId(Long.valueOf(project.getId())));

        // Guardar el proyecto con las nuevas imágenes
        return projectRepository.save(project);
    }

    @Transactional
    public Project removeImagesFromProject(Integer projectId, List<String> imageUrls) {
        // Buscar el proyecto existente
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        // Buscar las imágenes relacionadas con el proyecto
        List<Image> imagesToRemove = project.getImages().stream()
                .filter(image -> imageUrls.contains(image.getImageUrl()))
                .collect(Collectors.toList());

        if (imagesToRemove.isEmpty()) {
            throw new ResourceNotFoundException("No images found to remove");
        }

        // Eliminar las imágenes de MinIO
        storageService.deleteFiles(imageUrls);  // Llamada a un servicio que borra las imágenes del bucket MinIO

        // Eliminar las imágenes de la base de datos
        imageRepository.deleteAll(imagesToRemove);

        // Actualizar el proyecto con las imágenes restantes
        project.getImages().removeAll(imagesToRemove);

        // Guardar el proyecto con las imágenes actualizadas
        return projectRepository.save(project);
    }



    @Transactional
    @Override
    public Boolean deleteProject(Integer id) {

        //Busco el proyecto
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        // Verificar si el proyecto tiene pagos asociados
        List<Payment> payments = paymentRepository.findByProjectId(id);
        if (!payments.isEmpty()) {
            // Si tiene pagos, lanzar una excepción o retornar un mensaje adecuado
            throw new ProjectBlockedException("Cannot delete project because it has associated payments");
        }

        try {

            //obtengo todas las imagenes asociadas al proyecto
            List<Image> images = imageRepository.findByProjectId(Long.valueOf(id));
            // Eliminar las imágenes de MinIO
            if (!images.isEmpty()) {
                List<String> imageUrls = images.stream().map(Image::getImageUrl).collect(Collectors.toList());
                storageService.deleteFiles(imageUrls);  // Eliminar imágenes de MinIO
            }
            // Eliminar las imágenes de la base de datos
            imageRepository.deleteByProjectId(Long.valueOf(id));
            // Eliminar el proyecto de la base de datos
            projectRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;// Si algo sale mal, devolver false
        }
    }

    public boolean doesProjectExist(String name) {
        return projectRepository.existsByName(name);
    }
}
