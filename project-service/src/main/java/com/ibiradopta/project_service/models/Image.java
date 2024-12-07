package com.ibiradopta.project_service.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "proyecto_imagen", uniqueConstraints = @UniqueConstraint(columnNames = "urlimgproyecto"))
public class Image {
    @Id
    @Column(name = "urlimgproyecto")
    private String imageUrl;
    @Column(name = "ordenimgproyecto")
    private Integer imageOrder;
    @ManyToOne
    @JoinColumn(name = "proyecto_id", referencedColumnName = "id",nullable = false)
    @JsonIgnore
    private Project project;
}
