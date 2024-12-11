package com.ibiradopta.project_service.repositories;

import com.ibiradopta.project_service.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByProjectId(Integer projectId);
    @Query(value ="SELECT * FROM pagos WHERE " +
            "(:projectId IS NULL OR proyecto_id = :projectId) AND " +
            "(:userId IS NULL OR usuario = :userId) AND " +
            "(:startDate IS NULL OR fecha >= :startDate) AND " +
            "(:endDate IS NULL OR fecha <= :endDate)", nativeQuery = true)
    List<Payment> findByFilters(Integer projectId, String userId, String startDate, String endDate);

    List<Payment> findByUserId(String userId);
}
