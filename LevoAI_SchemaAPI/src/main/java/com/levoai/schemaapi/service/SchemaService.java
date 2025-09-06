package com.levoai.schemaapi.service;

import com.levoai.schemaapi.entity.*;
import com.levoai.schemaapi.repository.*;
import com.levoai.schemaapi.entity.Application;
import com.levoai.schemaapi.entity.SchemaVersion;
import com.levoai.schemaapi.entity.ServiceEntity;
import com.levoai.schemaapi.repository.ApplicationRepository;
import com.levoai.schemaapi.repository.SchemaRepository;
import com.levoai.schemaapi.repository.ServiceRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SchemaService {

    private final ApplicationRepository appRepo;
    private final ServiceRepository serviceRepo;
    private final SchemaRepository schemaRepo;

    @Value("${file.storage.dir}")
    private String baseDir;

    /*
    This Util is to Parse, Validate and Upload the Schema to DataBase.
     */
    public void uploadSchema(String appName, String serviceName, MultipartFile file) throws Exception {

        /* Parse and Validate */
        OpenAPI openAPI = new OpenAPIV3Parser().readContents(new String(file.getBytes())).getOpenAPI();
        if (openAPI == null){
            throw new IllegalArgumentException("Invalid OpenAPI schema!");
        }

        Application app = appRepo.findByName(appName).orElseGet(() -> {
            Application a = new Application();
            a.setName(appName);
            return appRepo.save(a);
        });

        ServiceEntity service = null;
        if (serviceName != null) {
            service = serviceRepo.findByNameAndApplication(serviceName, app).orElseGet(() -> {
                ServiceEntity s = new ServiceEntity();
                s.setName(serviceName);
                s.setApplication(app);
                return serviceRepo.save(s);
            });
        }

        int version = schemaRepo.findMaxVersion(app, service).orElse(0) + 1;
        String dir = baseDir + "/" + appName + (serviceName != null ? "/" + serviceName : "");
        Path dirPath = Paths.get(dir);
        Files.createDirectories(dirPath);
        String filePath = dir + "/v" + version + "-" + file.getOriginalFilename();
        Path targetPath = Paths.get(filePath);

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        SchemaVersion schema = new SchemaVersion();
        schema.setApplication(app);
        schema.setService(service);
        schema.setVersion(version);
        schema.setFilePath(filePath);
        schema.setUploadedAt(LocalDateTime.now());
        schemaRepo.save(schema);
    }

    public ResponseEntity<Resource> getSchema(String appName, String serviceName, Integer version) {

        Application app = appRepo.findByName(appName).orElseThrow();
        ServiceEntity service = null;

        if (serviceName != null) {
            service = serviceRepo.findByNameAndApplication(serviceName, app).orElseThrow();
        }

        SchemaVersion schema = (version == null) ?
                schemaRepo.findTopByApplicationAndServiceOrderByVersionDesc(app, service).orElseThrow() :
                schemaRepo.findByApplicationAndServiceAndVersion(app, service, version).orElseThrow();

        FileSystemResource resource = new FileSystemResource(schema.getFilePath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .body(resource);
    }
}
