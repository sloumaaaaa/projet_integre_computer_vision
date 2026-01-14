package com.esprit.animaldetection.repository;

import com.esprit.animaldetection.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    
    Optional<UserProfile> findByUserId(String userId);
    
    @Query("SELECT p FROM UserProfile p WHERE p.age BETWEEN ?1 AND ?2")
    List<UserProfile> findByAgeRange(Integer minAge, Integer maxAge);
    
    @Query("SELECT p FROM UserProfile p WHERE p.occupation = ?1")
    List<UserProfile> findByOccupation(String occupation);
    
    @Query("SELECT p FROM UserProfile p ORDER BY p.totalTimeSpentMinutes DESC")
    List<UserProfile> findMostActiveUsers();
    
    @Query("SELECT p FROM UserProfile p WHERE p.marketingConsent = true")
    List<UserProfile> findUsersWithMarketingConsent();
}
