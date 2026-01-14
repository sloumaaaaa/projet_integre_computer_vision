package com.esprit.animaldetection.controller;

import com.esprit.animaldetection.model.User;
import com.esprit.animaldetection.model.UserGameProgress;
import com.esprit.animaldetection.model.UserSession;
import com.esprit.animaldetection.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SessionController {

    private final SessionService sessionService;

    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");
            
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username is required"));
            }
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
            }
            if (password == null || password.length() < 6) {
                return ResponseEntity.badRequest().body(Map.of("error", "Password must be at least 6 characters"));
            }
            
            User user = sessionService.registerUser(username, email, password);
            
            // Automatically login after registration
            UserSession session = sessionService.login(username, password);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Registration successful");
            response.put("sessionId", session.getSessionId());
            response.put("username", user.getUsername());
            response.put("userId", user.getId());
            
            log.info("User registered: {}", username);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Registration error", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Login user
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            
            UserSession session = sessionService.login(username, password);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("sessionId", session.getSessionId());
            response.put("username", session.getUsername());
            response.put("userId", session.getUserId());
            response.put("expiresAt", session.getExpiresAt());
            
            log.info("User logged in: {}", username);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Login error", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Logout user
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Session-Id", required = false) String sessionId) {
        try {
            if (sessionId != null) {
                sessionService.logout(sessionId);
            }
            
            return ResponseEntity.ok(Map.of("success", true, "message", "Logged out successfully"));
            
        } catch (Exception e) {
            log.error("Logout error", e);
            return ResponseEntity.ok(Map.of("success", true)); // Always succeed logout
        }
    }

    /**
     * Validate session
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateSession(@RequestHeader("Session-Id") String sessionId) {
        try {
            UserSession session = sessionService.validateSession(sessionId);
            User user = sessionService.getUserBySession(sessionId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("username", user.getUsername());
            response.put("userId", user.getId());
            response.put("expiresAt", session.getExpiresAt());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("valid", false, "error", e.getMessage()));
        }
    }

    /**
     * Get current user info
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Session-Id") String sessionId) {
        try {
            User user = sessionService.getUserBySession(sessionId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("createdAt", user.getCreatedAt());
            response.put("lastLogin", user.getLastLogin());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get game progress
     */
    @GetMapping("/progress")
    public ResponseEntity<?> getProgress(@RequestHeader("Session-Id") String sessionId) {
        try {
            UserGameProgress progress = sessionService.getGameProgress(sessionId);
            return ResponseEntity.ok(progress);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Refresh game progress directly from database (force reload)
     */
    @GetMapping("/progress/refresh")
    public ResponseEntity<?> refreshProgress(@RequestHeader("Session-Id") String sessionId) {
        try {
            UserGameProgress progress = sessionService.refreshProgressFromDatabase(sessionId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Progress refreshed from database");
            response.put("progress", progress);
            response.put("source", "database");
            
            log.info("Progress refreshed from database for session: {}", sessionId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update game progress
     */
    @PostMapping("/progress")
    public ResponseEntity<?> updateProgress(
            @RequestHeader("Session-Id") String sessionId,
            @RequestBody UserGameProgress progress) {
        try {
            UserGameProgress updated = sessionService.updateGameProgress(sessionId, progress);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "progress", updated
            ));
            
        } catch (Exception e) {
            log.error("Progress update error", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get leaderboard
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<?> getLeaderboard() {
        try {
            List<Map<String, Object>> leaderboard = sessionService.getLeaderboard();
            return ResponseEntity.ok(Map.of("leaderboard", leaderboard));
            
        } catch (Exception e) {
            log.error("Leaderboard error", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Server stats (for debugging)
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("activeSessions", sessionService.getActiveSessionsCount());
        return ResponseEntity.ok(stats);
    }
}
