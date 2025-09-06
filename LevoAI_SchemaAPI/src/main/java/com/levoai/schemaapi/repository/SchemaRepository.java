package com.levoai.schemaapi.repository;

import com.levoai.schemaapi.entity.Application;
import com.levoai.schemaapi.entity.ServiceEntity;
import com.levoai.schemaapi.entity.SchemaVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface SchemaRepository extends JpaRepository<SchemaVersion, Long> {

    Optional<SchemaVersion> findTopByApplicationAndServiceOrderByVersionDesc(Application app, ServiceEntity service);
    Optional<SchemaVersion> findByApplicationAndServiceAndVersion(Application app, ServiceEntity service, int version);

    @Query("SELECT MAX(s.version) FROM SchemaVersion s WHERE s.application = :app AND (:service IS NULL OR s.service = :service)")
    Optional<Integer> findMaxVersion(@Param("app") Application app, @Param("service") ServiceEntity service);
}
