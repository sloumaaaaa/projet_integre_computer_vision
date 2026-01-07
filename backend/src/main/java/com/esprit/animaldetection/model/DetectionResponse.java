package com.esprit.animaldetection.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetectionResponse {
    private String annotatedImageUrl;
    private Map<String, Object> metrics;
    private Map<String, String> descriptions;
    private String status;
    private String message;
}
