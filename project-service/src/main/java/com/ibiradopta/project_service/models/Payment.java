package com.ibiradopta.project_service.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pagos")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "monto")
    private Double amount;
    @Column(name = "fecha")
    private LocalDate date;
    @Column(name = "usuario")
    private String userId;
    @ManyToOne
    @JoinColumn(name = "proyecto_id", referencedColumnName = "id",nullable = false)
    private Project project;
}
