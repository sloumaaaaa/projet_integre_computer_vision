package com.esprit.animaldetection.service;

import com.esprit.animaldetection.model.DetectionRequest;
import com.esprit.animaldetection.model.DetectionResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface DetectionService {
    DetectionResponse detectObjects(MultipartFile file, DetectionRequest request);
    Resource loadFileAsResource(String filename);
}
