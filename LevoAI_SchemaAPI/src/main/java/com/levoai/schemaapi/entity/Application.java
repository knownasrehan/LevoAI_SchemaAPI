package com.levoai.schemaapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Application {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;
}
