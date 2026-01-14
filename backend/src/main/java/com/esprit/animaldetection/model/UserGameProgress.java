package com.esprit.animaldetection.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_game_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGameProgress {
    @Id
    @Column(name = "user_id")
    private String userId;
    
    @Column(nullable = false)
    private int points;
    
    @Column(nullable = false)
    private int level;
    
    @Column(nullable = false)
    private int streak;
    
    @Column(name = "best_streak", nullable = false)
    private int bestStreak;
    
    @Column(name = "total_detections", nullable = false)
    private int totalDetections;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_collected_animals", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "animal_name")
    private List<String> collectedAnimals;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_badges", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "badge_name")
    private List<String> badges;
    
    @Column(name = "last_updated")
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
