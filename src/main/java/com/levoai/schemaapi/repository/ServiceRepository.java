package com.levoai.schemaapi.repository;

import com.levoai.schemaapi.entity.Application;
import com.levoai.schemaapi.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    Optional<ServiceEntity> findByNameAndApplication(String name, Application application);
}
