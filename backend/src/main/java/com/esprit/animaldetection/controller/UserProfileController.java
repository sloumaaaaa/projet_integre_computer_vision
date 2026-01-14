package com.esprit.animaldetection.controller;

import com.esprit.animaldetection.model.UserProfile;
import com.esprit.animaldetection.model.UserSession;
import com.esprit.animaldetection.service.SessionService;
import com.esprit.animaldetection.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserProfileController {
    
    private final UserProfileService userProfileService;
    private final SessionService sessionService;
    
    /**
     * Get user profile
     */
    @GetMapping
    public ResponseEntity<?> getProfile(@RequestHeader("Session-Id") String sessionId) {
        try {
            UserSession session = sessionService.validateSession(sessionId);
            UserProfile profile = userProfileService.getOrCreateProfile(session.getUserId());
            
            return ResponseEntity.ok(profile);
            
        } catch (Exception e) {
            log.error("Get profile error", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Update user profile
     */
    @PutMapping
    public ResponseEntity<?> updateProfile(
            @RequestHeader("Session-Id") String sessionId,
            @RequestBody UserProfile profileData) {
        try {
            UserSession session = sessionService.validateSession(sessionId);
            UserProfile updated = userProfileService.updateProfile(session.getUserId(), profileData);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile updated successfully");
            response.put("profile", updated);
            
            log.info("Profile updated for user: {}", session.getUserId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Update profile error", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Track time spent (called periodically from frontend)
     */
    @PostMapping("/track-time")
    public ResponseEntity<?> trackTime(
            @RequestHeader("Session-Id") String sessionId,
            @RequestBody Map<String, Long> data) {
        try {
            UserSession session = sessionService.validateSession(sessionId);
            Long minutes = data.get("minutes");
            
            if (minutes != null && minutes > 0) {
                userProfileService.trackTimeSpent(session.getUserId(), minutes);
            }
            
            return ResponseEntity.ok(Map.of("success", true));
            
        } catch (Exception e) {
            log.error("Track time error", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get analytics (admin only - for now open to all)
     */
    @GetMapping("/analytics")
    public ResponseEntity<?> getAnalytics() {
        try {
            Map<String, Object> analytics = userProfileService.getAnalytics();
            return ResponseEntity.ok(analytics);
            
        } catch (Exception e) {
            log.error("Get analytics error", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get occupation statistics
     */
    @GetMapping("/analytics/occupations")
    public ResponseEntity<?> getOccupationStats() {
        try {
            Map<String, Long> stats = userProfileService.getOccupationStats();
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            log.error("Get occupation stats error", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get most active users
     */
    @GetMapping("/analytics/most-active")
    public ResponseEntity<?> getMostActiveUsers(@RequestParam(defaultValue = "10") int limit) {
        try {
            var users = userProfileService.getMostActiveUsers(limit);
            return ResponseEntity.ok(users);
            
        } catch (Exception e) {
            log.error("Get most active users error", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
