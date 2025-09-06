package com.levoai.schemaapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class SchemaVersion {

    @Id @GeneratedValue
    private Long id;

    private int version;

    private String filePath;

    private LocalDateTime uploadedAt;

    @ManyToOne
    private Application application;

    @ManyToOne
    private ServiceEntity service;
}
