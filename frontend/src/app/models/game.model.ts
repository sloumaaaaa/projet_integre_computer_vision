export interface Animal {
  id: string;
  name: string;
  displayName: string;
  modelUrl: string;
  thumbnailUrl: string;
  description: string;
  rarity: 'common' | 'uncommon' | 'rare' | 'legendary';
  points: number;
  funFacts: string[];
}

export interface CollectedAnimal {
  animal: Animal;
  discoveredAt: Date;
  timesFound: number;
}

export interface Badge {
  id: string;
  name: string;
  description: string;
  iconUrl: string;
  requirement: string;
  unlocked: boolean;
  unlockedAt?: Date;
}

export interface GameState {
  collectedAnimals: Map<string, CollectedAnimal>;
  totalPoints: number;
  badges: Badge[];
  level: number;
  currentStreak: number;
  bestStreak: number;
  totalDetections: number;
}

export interface Leaderboard {
  username: string;
  points: number;
  animalsCollected: number;
  rank: number;
}
