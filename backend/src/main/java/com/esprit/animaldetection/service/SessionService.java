package com.esprit.animaldetection.service;

import com.esprit.animaldetection.model.User;
import com.esprit.animaldetection.model.UserGameProgress;
import com.esprit.animaldetection.model.UserSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    
    // In-memory storage (replace with database in production)
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, UserSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, UserGameProgress> gameProgress = new ConcurrentHashMap<>();
    
    // Session expiration time in hours
    private static final int SESSION_EXPIRATION_HOURS = 24;
    
    /**
     * Register a new user
     */
    public User registerUser(String username, String email, String password) {
        // Check if username already exists
        if (users.values().stream().anyMatch(u -> u.getUsername().equals(username))) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(email))) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User(username, email, password);
        users.put(user.getId(), user);
        
        // Initialize game progress for new user
        gameProgress.put(user.getId(), new UserGameProgress(user.getId()));
        
        return user;
    }
    
    /**
     * Login user and create session
     */
    public UserSession login(String username, String password) {
        User user = users.values().stream()
            .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        // Update last login
        user.setLastLogin(LocalDateTime.now());
        
        // Create new session
        String sessionId = UUID.randomUUID().toString();
        UserSession session = UserSession.builder()
            .sessionId(sessionId)
            .userId(user.getId())
            .username(user.getUsername())
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusHours(SESSION_EXPIRATION_HOURS))
            .build();
        
        sessions.put(sessionId, session);
        return session;
    }
    
    /**
     * Logout user and invalidate session
     */
    public void logout(String sessionId) {
        sessions.remove(sessionId);
    }
    
    /**
     * Validate session
     */
    public UserSession validateSession(String sessionId) {
        UserSession session = sessions.get(sessionId);
        if (session == null) {
            throw new RuntimeException("Session not found");
        }
        
        if (session.isExpired()) {
            sessions.remove(sessionId);
            throw new RuntimeException("Session expired");
        }
        
        return session;
    }
    
    /**
     * Get user by session
     */
    public User getUserBySession(String sessionId) {
        UserSession session = validateSession(sessionId);
        return users.get(session.getUserId());
    }
    
    /**
     * Get or create game progress for user
     */
    public UserGameProgress getGameProgress(String sessionId) {
        UserSession session = validateSession(sessionId);
        return gameProgress.computeIfAbsent(session.getUserId(), UserGameProgress::new);
    }
    
    /**
     * Update game progress
     */
    public UserGameProgress updateGameProgress(String sessionId, UserGameProgress progress) {
        UserSession session = validateSession(sessionId);
        progress.setUserId(session.getUserId());
        progress.setLastUpdated(LocalDateTime.now());
        gameProgress.put(session.getUserId(), progress);
        return progress;
    }
    
    /**
     * Get leaderboard (top 10 players by points)
     */
    public List<Map<String, Object>> getLeaderboard() {
        List<Map<String, Object>> leaderboard = new ArrayList<>();
        
        gameProgress.values().stream()
            .sorted((a, b) -> Integer.compare(b.getPoints(), a.getPoints()))
            .limit(10)
            .forEach(progress -> {
                User user = users.get(progress.getUserId());
                Map<String, Object> entry = new HashMap<>();
                entry.put("username", user.getUsername());
                entry.put("points", progress.getPoints());
                entry.put("level", progress.getLevel());
                entry.put("collected", progress.getCollectedAnimals().size());
                entry.put("badges", progress.getBadges().size());
                leaderboard.add(entry);
            });
        
        return leaderboard;
    }
    
    /**
     * Get all sessions (for admin/debugging)
     */
    public int getActiveSessionsCount() {
        // Clean expired sessions
        sessions.entrySet().removeIf(entry -> entry.getValue().isExpired());
        return sessions.size();
    }
}
