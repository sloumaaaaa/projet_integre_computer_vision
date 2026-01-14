package com.esprit.animaldetection.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @Id
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    private Integer age;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    private String occupation;
    
    private String country;
    
    private String city;
    
    @Column(name = "total_time_spent_minutes")
    private Long totalTimeSpentMinutes;
    
    @Column(name = "session_count")
    private Integer sessionCount;
    
    @Column(name = "last_active")
    private LocalDateTime lastActive;
    
    @Column(name = "first_login")
    private LocalDateTime firstLogin;
    
    @Column(name = "profile_created_at")
    private LocalDateTime profileCreatedAt;
    
    @Column(name = "profile_updated_at")
    private LocalDateTime profileUpdatedAt;
    
    // Additional fields for admin analytics
    @Column(name = "device_type")
    private String deviceType;
    
    @Column(name = "browser_info")
    private String browserInfo;
    
    @Column(name = "preferred_language")
    private String preferredLanguage;
    
    @Column(name = "marketing_consent")
    private Boolean marketingConsent;
    
    public UserProfile(String userId) {
        this.userId = userId;
        this.totalTimeSpentMinutes = 0L;
        this.sessionCount = 0;
        this.profileCreatedAt = LocalDateTime.now();
        this.profileUpdatedAt = LocalDateTime.now();
        this.firstLogin = LocalDateTime.now();
        this.marketingConsent = false;
    }
    
    public void incrementSessionCount() {
        this.sessionCount = (this.sessionCount == null ? 0 : this.sessionCount) + 1;
        this.lastActive = LocalDateTime.now();
    }
    
    public void addTimeSpent(long minutes) {
        this.totalTimeSpentMinutes = (this.totalTimeSpentMinutes == null ? 0 : this.totalTimeSpentMinutes) + minutes;
        this.lastActive = LocalDateTime.now();
    }
}
