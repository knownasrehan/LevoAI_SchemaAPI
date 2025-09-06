package com.levoai.schemaapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ServiceEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Application application;
}
