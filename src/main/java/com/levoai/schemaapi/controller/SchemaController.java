package com.levoai.schemaapi.controller;

import com.levoai.schemaapi.service.SchemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/schemas")
@RequiredArgsConstructor
public class SchemaController {

    private final SchemaService schemaService;

    /*
    This API is for uploading validating and Uploading the Schema.
    We can upload directly to root or to the Service provided.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam String application,
                                         @RequestParam(required = false) String service,
                                         @RequestParam("file") MultipartFile file) throws Exception {
        schemaService.uploadSchema(application, service, file);
        return ResponseEntity.ok("Schema uploaded successfully!\n");
    }

    /*
    This API is for getting the latest Uploaded Schema.
     */
    @GetMapping("/{application}")
    public ResponseEntity<Resource> latestAppSchema(@PathVariable String application) {
        return schemaService.getSchema(application, null, null);
    }

    /*
    This API is for getting the latest Uploaded schema in the Service.
     */
    @GetMapping("/{application}/services/{service}")
    public ResponseEntity<Resource> latestServiceSchema(@PathVariable String application,
                                                        @PathVariable String service) {
        return schemaService.getSchema(application, service, null);
    }

    /*
    This API is for getting the Schema by its version.
     */
    @GetMapping("/{application}/versions/{version}")
    public ResponseEntity<Resource> specificVersion(@PathVariable String application,
                                                    @PathVariable int version) {
        return schemaService.getSchema(application, null, version);
    }

    /*
    This API is for getting the Schema from the Service by its version.
     */
    @GetMapping("/{application}/services/{service}/versions/{version}")
    public ResponseEntity<Resource> specificServiceVersion(@PathVariable String application,
                                                           @PathVariable String service,
                                                           @PathVariable int version) {
        return schemaService.getSchema(application, service, version);
    }
}
