package com.levoai.schemaapi.repository;

import com.levoai.schemaapi.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByName(String name);
}
