import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Animal, CollectedAnimal, Badge, GameState, Leaderboard } from '../models/game.model';

@Injectable({
  providedIn: 'root'
})
export class GameService {
  private readonly STORAGE_KEY = 'animal_ar_game_state';
  
  private gameState: GameState = {
    collectedAnimals: new Map(),
    totalPoints: 0,
    badges: this.initializeBadges(),
    level: 1,
    currentStreak: 0,
    bestStreak: 0,
    totalDetections: 0
  };

  private gameStateSubject = new BehaviorSubject<GameState>(this.gameState);
  public gameState$ = this.gameStateSubject.asObservable();

  // Animal definitions with 3D model paths
  private animals: Animal[] = [
    {
      id: 'elephant',
      name: 'elephant',
      displayName: 'Elephant',
      modelUrl: 'assets/models/elephant.glb',
      thumbnailUrl: 'assets/thumbnails/elephant.png',
      description: 'Throughout history, elephants have been revered for their size, strength, and intelligence.',
      rarity: 'uncommon',
      points: 150,
      funFacts: [
        'Elephants can recognize themselves in mirrors',
        'They have incredible memory spanning decades',
        'Elephants show empathy and mourn their dead'
      ]
    },
    {
      id: 'lion',
      name: 'lion',
      displayName: 'Lion',
      modelUrl: 'assets/models/lion.glb',
      thumbnailUrl: 'assets/thumbnails/lion.png',
      description: 'Known as the "king of the jungle," lions symbolize courage and royalty.',
      rarity: 'rare',
      points: 200,
      funFacts: [
        'Lions sleep up to 20 hours a day',
        'Female lions do most of the hunting',
        'A lion\'s roar can be heard up to 5 miles away'
      ]
    },
    {
      id: 'tiger',
      name: 'tiger',
      displayName: 'Tiger',
      modelUrl: 'assets/models/tiger.glb',
      thumbnailUrl: 'assets/thumbnails/tiger.png',
      description: 'Tigers represent power and majesty in Asian cultures.',
      rarity: 'rare',
      points: 250,
      funFacts: [
        'No two tigers have the same stripes',
        'Tigers are excellent swimmers',
        'They can leap up to 30 feet in a single bound'
      ]
    },
    {
      id: 'wolf',
      name: 'wolf',
      displayName: 'Wolf',
      modelUrl: 'assets/models/wolf.glb',
      thumbnailUrl: 'assets/thumbnails/wolf.png',
      description: 'Wolves symbolize both danger and loyalty in folklore.',
      rarity: 'uncommon',
      points: 175,
      funFacts: [
        'Wolves can run at speeds up to 40 mph',
        'They live in complex social structures called packs',
        'Wolves mate for life'
      ]
    },
    {
      id: 'pig',
      name: 'pig',
      displayName: 'Pig',
      modelUrl: 'assets/models/pig.glb',
      thumbnailUrl: 'assets/thumbnails/pig.png',
      description: 'Domesticated over 9,000 years ago, pigs are intelligent and adaptable.',
      rarity: 'common',
      points: 100,
      funFacts: [
        'Pigs are among the smartest animals on Earth',
        'They can learn their names within weeks',
        'Pigs don\'t actually sweat - "sweating like a pig" is a myth!'
      ]
    }
  ];

  constructor() {
    this.loadGameState();
  }

  private initializeBadges(): Badge[] {
    return [
      {
        id: 'first_discovery',
        name: 'First Steps',
        description: 'Discover your first animal',
        iconUrl: 'assets/badges/first.png',
        requirement: 'discover_1',
        unlocked: false
      },
      {
        id: 'collector',
        name: 'Collector',
        description: 'Collect all 5 animal types',
        iconUrl: 'assets/badges/collector.png',
        requirement: 'collect_all',
        unlocked: false
      },
      {
        id: 'streak_5',
        name: 'On a Roll',
        description: 'Find 5 animals in a row',
        iconUrl: 'assets/badges/streak.png',
        requirement: 'streak_5',
        unlocked: false
      },
      {
        id: 'points_1000',
        name: 'Point Master',
        description: 'Earn 1000 points',
        iconUrl: 'assets/badges/points.png',
        requirement: 'points_1000',
        unlocked: false
      },
      {
        id: 'rare_hunter',
        name: 'Rare Hunter',
        description: 'Find a rare or legendary animal',
        iconUrl: 'assets/badges/rare.png',
        requirement: 'find_rare',
        unlocked: false
      }
    ];
  }

  getAnimalByName(name: string): Animal | undefined {
    return this.animals.find(a => a.name.toLowerCase() === name.toLowerCase());
  }

  getAllAnimals(): Animal[] {
    return this.animals;
  }

  addDetection(animalName: string): { isNew: boolean; animal: Animal; pointsEarned: number } {
    const animal = this.getAnimalByName(animalName);
    if (!animal) {
      return { isNew: false, animal: {} as Animal, pointsEarned: 0 };
    }

    const isNew = !this.gameState.collectedAnimals.has(animal.id);
    
    if (isNew) {
      this.gameState.collectedAnimals.set(animal.id, {
        animal,
        discoveredAt: new Date(),
        timesFound: 1
      });
    } else {
      const collected = this.gameState.collectedAnimals.get(animal.id)!;
      collected.timesFound++;
    }

    // Award points
    const pointsEarned = animal.points;
    this.gameState.totalPoints += pointsEarned;
    this.gameState.totalDetections++;
    this.gameState.currentStreak++;
    
    if (this.gameState.currentStreak > this.gameState.bestStreak) {
      this.gameState.bestStreak = this.gameState.currentStreak;
    }

    // Check for badge unlocks
    this.checkBadges(animal, isNew);

    // Level up
    this.updateLevel();

    this.saveGameState();
    this.gameStateSubject.next(this.gameState);

    return { isNew, animal, pointsEarned };
  }

  private checkBadges(animal: Animal, isNew: boolean): void {
    // First discovery
    if (isNew && this.gameState.collectedAnimals.size === 1) {
      this.unlockBadge('first_discovery');
    }

    // Collector
    if (this.gameState.collectedAnimals.size === this.animals.length) {
      this.unlockBadge('collector');
    }

    // Streak
    if (this.gameState.currentStreak >= 5) {
      this.unlockBadge('streak_5');
    }

    // Points
    if (this.gameState.totalPoints >= 1000) {
      this.unlockBadge('points_1000');
    }

    // Rare hunter
    if (animal.rarity === 'rare' || animal.rarity === 'legendary') {
      this.unlockBadge('rare_hunter');
    }
  }

  private unlockBadge(badgeId: string): void {
    const badge = this.gameState.badges.find(b => b.id === badgeId);
    if (badge && !badge.unlocked) {
      badge.unlocked = true;
      badge.unlockedAt = new Date();
    }
  }

  private updateLevel(): void {
    const newLevel = Math.floor(this.gameState.totalPoints / 500) + 1;
    if (newLevel > this.gameState.level) {
      this.gameState.level = newLevel;
    }
  }

  resetStreak(): void {
    this.gameState.currentStreak = 0;
    this.saveGameState();
    this.gameStateSubject.next(this.gameState);
  }

  resetGame(): void {
    this.gameState = {
      collectedAnimals: new Map(),
      totalPoints: 0,
      badges: this.initializeBadges(),
      level: 1,
      currentStreak: 0,
      bestStreak: 0,
      totalDetections: 0
    };
    this.saveGameState();
    this.gameStateSubject.next(this.gameState);
  }

  private saveGameState(): void {
    const serialized = {
      ...this.gameState,
      collectedAnimals: Array.from(this.gameState.collectedAnimals.entries())
    };
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(serialized));
  }

  private loadGameState(): void {
    const saved = localStorage.getItem(this.STORAGE_KEY);
    if (saved) {
      try {
        const parsed = JSON.parse(saved);
        this.gameState = {
          ...parsed,
          collectedAnimals: new Map(parsed.collectedAnimals || [])
        };
        this.gameStateSubject.next(this.gameState);
      } catch (e) {
        console.error('Error loading game state:', e);
      }
    }
  }

  getCompletionPercentage(): number {
    return (this.gameState.collectedAnimals.size / this.animals.length) * 100;
  }

  getBadgeCompletionPercentage(): number {
    const unlocked = this.gameState.badges.filter(b => b.unlocked).length;
    return (unlocked / this.gameState.badges.length) * 100;
  }
}
