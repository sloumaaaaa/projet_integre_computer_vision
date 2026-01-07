package com.esprit.animaldetection.controller;

import com.esprit.animaldetection.model.DetectionRequest;
import com.esprit.animaldetection.model.DetectionResponse;
import com.esprit.animaldetection.service.DetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/detection")
@RequiredArgsConstructor
public class DetectionController {

    private final DetectionService detectionService;

    @PostMapping("/upload")
    public ResponseEntity<DetectionResponse> uploadAndDetect(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "yolov8") String modelType,
            @RequestParam(defaultValue = "0.5") double threshold,
            @RequestParam(defaultValue = "true") boolean showDescriptions) {
        
        log.info("Received file: {}, model: {}, threshold: {}", 
                 file.getOriginalFilename(), modelType, threshold);
        
        DetectionRequest request = new DetectionRequest(modelType, threshold, showDescriptions);
        DetectionResponse response = detectionService.detectObjects(file, request);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/image/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource resource = detectionService.loadFileAsResource(filename);
        
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/models")
    public ResponseEntity<?> getAvailableModels() {
        return ResponseEntity.ok(new String[]{"roboflow", "yolov8"});
    }
}
