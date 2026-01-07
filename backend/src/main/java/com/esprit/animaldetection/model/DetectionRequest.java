package com.esprit.animaldetection.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetectionRequest {
    private String modelType; // "roboflow" or "yolov8"
    private double threshold = 0.5;
    private boolean showDescriptions = true;
}
