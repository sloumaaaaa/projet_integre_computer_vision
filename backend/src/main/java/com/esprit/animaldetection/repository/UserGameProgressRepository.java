package com.esprit.animaldetection.repository;

import com.esprit.animaldetection.model.UserGameProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGameProgressRepository extends JpaRepository<UserGameProgress, String> {
    
    @Query("SELECT p FROM UserGameProgress p ORDER BY p.points DESC")
    List<UserGameProgress> findTopByOrderByPointsDesc();
    
    @Query("SELECT p FROM UserGameProgress p WHERE p.points > 0 ORDER BY p.points DESC")
    List<UserGameProgress> findLeaderboard();
}
