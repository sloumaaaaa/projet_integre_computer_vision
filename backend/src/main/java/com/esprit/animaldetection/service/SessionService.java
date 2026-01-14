package com.esprit.animaldetection.service;

import com.esprit.animaldetection.model.User;
import com.esprit.animaldetection.model.UserGameProgress;
import com.esprit.animaldetection.model.UserSession;
import com.esprit.animaldetection.repository.UserGameProgressRepository;
import com.esprit.animaldetection.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SessionService {
    
    private final UserRepository userRepository;
    private final UserGameProgressRepository gameProgressRepository;
    
    // In-memory storage for sessions only
    private final Map<String, UserSession> sessions = new ConcurrentHashMap<>();
    
    // Session expiration time in hours
    private static final int SESSION_EXPIRATION_HOURS = 24;
    
    /**
     * Register a new user
     */
    public User registerUser(String username, String email, String password) {
        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User(username, email, password);
        user = userRepository.save(user);
        
        // Initialize game progress for new user in database
        UserGameProgress progress = new UserGameProgress(user.getId());
        gameProgressRepository.save(progress);
        
        return user;
    }
    
    /**
     * Login user and create session
     */
    public UserSession login(String username, String password) {
        User user = userRepository.findByUsername(username)
            .filter(u -> u.getPassword().equals(password))
            .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
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
        return userRepository.findById(session.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    /**
     * Get or create game progress for user
     */
    public UserGameProgress getGameProgress(String sessionId) {
        UserSession session = validateSession(sessionId);
        return gameProgressRepository.findById(session.getUserId())
            .orElseGet(() -> {
                UserGameProgress newProgress = new UserGameProgress(session.getUserId());
                return gameProgressRepository.save(newProgress);
            });
    }
    
    /**
     * Update game progress
     */
    public UserGameProgress updateGameProgress(String sessionId, UserGameProgress progress) {
        UserSession session = validateSession(sessionId);
        progress.setUserId(session.getUserId());
        progress.setLastUpdated(LocalDateTime.now());
        return gameProgressRepository.save(progress);
    }
    
    /**
     * Refresh and get latest game progress directly from database
     */
    public UserGameProgress refreshProgressFromDatabase(String sessionId) {
        UserSession session = validateSession(sessionId);
        return gameProgressRepository.findById(session.getUserId())
            .orElseThrow(() -> new RuntimeException("Game progress not found. Please start playing to create progress."));
    }
    
    /**
     * Get leaderboard (top 10 players by points)
     */
    public List<Map<String, Object>> getLeaderboard() {
        List<Map<String, Object>> leaderboard = new ArrayList<>();
        
        gameProgressRepository.findLeaderboard().stream()
            .limit(10)
            .forEach(progress -> {
                userRepository.findById(progress.getUserId()).ifPresent(user -> {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("username", user.getUsername());
                    entry.put("points", progress.getPoints());
                    entry.put("level", progress.getLevel());
                    entry.put("collected", progress.getCollectedAnimals().size());
                    entry.put("badges", progress.getBadges().size());
                    leaderboard.add(entry);
                });
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
