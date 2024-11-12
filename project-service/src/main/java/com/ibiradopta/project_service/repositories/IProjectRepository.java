package com.ibiradopta.project_service.repositories;

import com.ibiradopta.project_service.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProjectRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findByName(String name);

    // consulta por nombre o ubicacion o rango de fechas y devuelve una lista de proyectos (se puede usar mas de un criterio a la vez)
    @Query(value = "SELECT * FROM proyectos WHERE (:name IS NULL OR nombre LIKE %:name%) " +
            "AND (:location IS NULL OR ubicacion LIKE %:location%) " +
            "AND (:startDate IS NULL OR fecha_finalizacion >= :startDate) " +
            "AND (:endDate IS NULL OR fecha_finalizacion <= :endDate)", nativeQuery = true)
    List<Project> findByFilters(String name, String location, String startDate, String endDate);
}
