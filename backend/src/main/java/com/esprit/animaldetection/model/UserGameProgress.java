package com.esprit.animaldetection.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGameProgress {
    private String userId;
    private int points;
    private int level;
    private int streak;
    private int bestStreak;
    private int totalDetections;
    private List<String> collectedAnimals;
    private List<String> badges;
    private LocalDateTime lastUpdated;
    
    public UserGameProgress(String userId) {
        this.userId = userId;
        this.points = 0;
        this.level = 1;
        this.streak = 0;
        this.bestStreak = 0;
        this.totalDetections = 0;
        this.collectedAnimals = new ArrayList<>();
        this.badges = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }
}
