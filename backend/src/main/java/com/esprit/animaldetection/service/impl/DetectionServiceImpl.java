package com.esprit.animaldetection.service.impl;

import com.esprit.animaldetection.model.DetectionRequest;
import com.esprit.animaldetection.model.DetectionResponse;
import com.esprit.animaldetection.service.DetectionService;
import com.esprit.animaldetection.service.PythonExecutorService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class DetectionServiceImpl implements DetectionService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private final PythonExecutorService pythonExecutorService;
    private final Gson gson = new Gson();

    public DetectionServiceImpl(PythonExecutorService pythonExecutorService) {
        this.pythonExecutorService = pythonExecutorService;
    }

    @Override
    public DetectionResponse detectObjects(MultipartFile file, DetectionRequest request) {
        try {
            // Save uploaded file
            String originalFilename = file.getOriginalFilename();
            String filename = UUID.randomUUID() + "_" + originalFilename;
            Path filepath = Paths.get(uploadDir).resolve(filename);
            Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("File saved: {}", filepath);
            
            // Execute Python detection script
            String result = pythonExecutorService.executeDetection(
                    filepath.toString(), 
                    request.getModelType(), 
                    request.getThreshold(),
                    request.isShowDescriptions()
            );
            
            // Parse Python output (JSON format)
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> resultMap = gson.fromJson(result, type);
            
            return DetectionResponse.builder()
                    .annotatedImageUrl("/api/detection/image/" + resultMap.get("annotated_filename"))
                    .metrics((Map<String, Object>) resultMap.get("metrics"))
                    .descriptions((Map<String, String>) resultMap.get("descriptions"))
                    .status("success")
                    .message("Detection completed successfully")
                    .build();
                    
        } catch (IOException e) {
            log.error("Error processing file", e);
            return DetectionResponse.builder()
                    .status("error")
                    .message("Error processing file: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("File not found: " + filename, e);
        }
    }
}
