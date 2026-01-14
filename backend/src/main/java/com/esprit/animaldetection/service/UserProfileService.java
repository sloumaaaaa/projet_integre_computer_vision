package com.esprit.animaldetection.service;

import com.esprit.animaldetection.model.UserProfile;
import com.esprit.animaldetection.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    
    private final UserProfileRepository userProfileRepository;
    
    /**
     * Create or get user profile
     */
    public UserProfile getOrCreateProfile(String userId) {
        return userProfileRepository.findByUserId(userId)
            .orElseGet(() -> {
                UserProfile profile = new UserProfile(userId);
                return userProfileRepository.save(profile);
            });
    }
    
    /**
     * Update user profile
     */
    public UserProfile updateProfile(String userId, UserProfile profileData) {
        UserProfile profile = getOrCreateProfile(userId);
        
        if (profileData.getPhoneNumber() != null) profile.setPhoneNumber(profileData.getPhoneNumber());
        if (profileData.getAge() != null) profile.setAge(profileData.getAge());
        if (profileData.getDateOfBirth() != null) profile.setDateOfBirth(profileData.getDateOfBirth());
        if (profileData.getOccupation() != null) profile.setOccupation(profileData.getOccupation());
        if (profileData.getCountry() != null) profile.setCountry(profileData.getCountry());
        if (profileData.getCity() != null) profile.setCity(profileData.getCity());
        if (profileData.getDeviceType() != null) profile.setDeviceType(profileData.getDeviceType());
        if (profileData.getBrowserInfo() != null) profile.setBrowserInfo(profileData.getBrowserInfo());
        if (profileData.getPreferredLanguage() != null) profile.setPreferredLanguage(profileData.getPreferredLanguage());
        if (profileData.getMarketingConsent() != null) profile.setMarketingConsent(profileData.getMarketingConsent());
        
        profile.setProfileUpdatedAt(LocalDateTime.now());
        
        return userProfileRepository.save(profile);
    }
    
    /**
     * Track session start
     */
    public void trackSessionStart(String userId) {
        UserProfile profile = getOrCreateProfile(userId);
        profile.incrementSessionCount();
        userProfileRepository.save(profile);
    }
    
    /**
     * Track time spent
     */
    public void trackTimeSpent(String userId, long minutes) {
        UserProfile profile = getOrCreateProfile(userId);
        profile.addTimeSpent(minutes);
        userProfileRepository.save(profile);
    }
    
    /**
     * Get analytics for admin dashboard
     */
    public Map<String, Object> getAnalytics() {
        List<UserProfile> allProfiles = userProfileRepository.findAll();
        
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalUsers", allProfiles.size());
        
        // Age distribution
        Map<String, Long> ageGroups = new HashMap<>();
        ageGroups.put("under18", allProfiles.stream().filter(p -> p.getAge() != null && p.getAge() < 18).count());
        ageGroups.put("18-24", allProfiles.stream().filter(p -> p.getAge() != null && p.getAge() >= 18 && p.getAge() <= 24).count());
        ageGroups.put("25-34", allProfiles.stream().filter(p -> p.getAge() != null && p.getAge() >= 25 && p.getAge() <= 34).count());
        ageGroups.put("35-44", allProfiles.stream().filter(p -> p.getAge() != null && p.getAge() >= 35 && p.getAge() <= 44).count());
        ageGroups.put("45plus", allProfiles.stream().filter(p -> p.getAge() != null && p.getAge() >= 45).count());
        analytics.put("ageDistribution", ageGroups);
        
        // Total time spent
        long totalMinutes = allProfiles.stream()
            .mapToLong(p -> p.getTotalTimeSpentMinutes() != null ? p.getTotalTimeSpentMinutes() : 0)
            .sum();
        analytics.put("totalTimeSpentMinutes", totalMinutes);
        analytics.put("totalTimeSpentHours", totalMinutes / 60.0);
        
        // Average time per user
        double avgTimePerUser = allProfiles.isEmpty() ? 0 : (double) totalMinutes / allProfiles.size();
        analytics.put("averageTimePerUserMinutes", avgTimePerUser);
        
        // Total sessions
        int totalSessions = allProfiles.stream()
            .mapToInt(p -> p.getSessionCount() != null ? p.getSessionCount() : 0)
            .sum();
        analytics.put("totalSessions", totalSessions);
        
        // Marketing consent
        long marketingConsentCount = allProfiles.stream()
            .filter(p -> p.getMarketingConsent() != null && p.getMarketingConsent())
            .count();
        analytics.put("marketingConsentCount", marketingConsentCount);
        
        return analytics;
    }
    
    /**
     * Get occupation statistics
     */
    public Map<String, Long> getOccupationStats() {
        List<UserProfile> allProfiles = userProfileRepository.findAll();
        
        Map<String, Long> occupationStats = new HashMap<>();
        allProfiles.stream()
            .filter(p -> p.getOccupation() != null && !p.getOccupation().isEmpty())
            .forEach(p -> {
                String occupation = p.getOccupation();
                occupationStats.put(occupation, occupationStats.getOrDefault(occupation, 0L) + 1);
            });
        
        return occupationStats;
    }
    
    /**
     * Get most active users
     */
    public List<UserProfile> getMostActiveUsers(int limit) {
        return userProfileRepository.findMostActiveUsers().stream()
            .limit(limit)
            .toList();
    }
}
