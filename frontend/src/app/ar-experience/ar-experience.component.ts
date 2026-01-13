import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DetectionService } from '../services/detection.service';
import { GameService } from '../services/game.service';
import { Animal, GameState } from '../models/game.model';
import { DetectionResponse } from '../models/detection.model';

@Component({
  selector: 'app-ar-experience',
  standalone: true,
  imports: [CommonModule, FormsModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  templateUrl: './ar-experience.component.html',
  styleUrls: ['./ar-experience.component.css']
})
export class ArExperienceComponent implements OnInit {
  // AR Mode
  arMode: 'detection' | 'scavenger' = 'scavenger';
  
  // Camera and detection
  selectedFile: File | null = null;
  modelType: string = 'yolov8';
  threshold: number = 0.5;
  loading: boolean = false;
  result: DetectionResponse | null = null;
  error: string | null = null;

  // AR 3D Model
  currentAnimal: Animal | null = null;
  showAnimalCard: boolean = false;
  show3DModel: boolean = false;
  
  // Game state
  gameState: GameState | null = null;
  showNewDiscovery: boolean = false;
  lastDiscovery: { isNew: boolean; animal: Animal; pointsEarned: number } | null = null;
  
  // Scavenger hunt
  targetAnimal: Animal | null = null;
  allAnimals: Animal[] = [];
  availableModels: string[] = ['roboflow', 'yolov8'];

  constructor(
    private detectionService: DetectionService,
    private gameService: GameService
  ) {}

  ngOnInit(): void {
    this.gameService.gameState$.subscribe(state => {
      this.gameState = state;
    });
    
    this.allAnimals = this.gameService.getAllAnimals();
    this.pickRandomTarget();
    this.loadAvailableModels();
  }

  loadAvailableModels(): void {
    this.detectionService.getAvailableModels().subscribe({
      next: (models) => this.availableModels = models,
      error: (err) => console.error('Error loading models:', err)
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.result = null;
      this.error = null;
    }
  }

  onSubmit(): void {
    if (!this.selectedFile) {
      this.error = 'Please select a file first';
      return;
    }

    this.loading = true;
    this.error = null;
    this.result = null;
    this.showNewDiscovery = false;

    this.detectionService.detectObjects(
      this.selectedFile,
      this.modelType,
      this.threshold,
      false
    ).subscribe({
      next: (response) => {
        this.loading = false;
        this.result = response;
        
        if (response.status === 'success') {
          this.processDetectionResult(response);
        }
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Detection failed. Please try again.';
        console.error('Detection error:', err);
      }
    });
  }

  private processDetectionResult(response: DetectionResponse): void {
    // Extract detected animal classes from the response
    const detectedClasses = this.extractDetectedClasses(response);
    
    if (detectedClasses.length > 0) {
      // Take the first detected animal
      const detectedAnimalName = detectedClasses[0];
      const discovery = this.gameService.addDetection(detectedAnimalName);
      
      this.lastDiscovery = discovery;
      this.currentAnimal = discovery.animal;
      
      // Show discovery notification
      this.showNewDiscovery = true;
      setTimeout(() => {
        this.showNewDiscovery = false;
      }, 5000);
      
      // Show 3D model
      this.show3DModel = true;
      
      // Check if this was the target in scavenger mode
      if (this.arMode === 'scavenger' && this.targetAnimal) {
        if (this.targetAnimal.id === discovery.animal.id) {
          // Found the target! Pick a new one
          setTimeout(() => {
            this.pickRandomTarget();
          }, 3000);
        }
      }
    } else {
      this.error = 'No animals detected. Try another image!';
      this.gameService.resetStreak();
    }
  }

  private extractDetectedClasses(response: DetectionResponse): string[] {
    // This assumes the backend returns detected classes
    // Adjust based on your actual response structure
    if (response.detectedClasses) {
      return response.detectedClasses;
    }
    
    // Fallback: try to extract from descriptions
    if (response.classDescriptions) {
      return Object.keys(response.classDescriptions);
    }
    
    return [];
  }

  pickRandomTarget(): void {
    const uncollected = this.allAnimals.filter(
      a => !this.gameState?.collectedAnimals.has(a.id)
    );
    
    if (uncollected.length > 0) {
      const randomIndex = Math.floor(Math.random() * uncollected.length);
      this.targetAnimal = uncollected[randomIndex];
    } else {
      // All collected, pick any random
      const randomIndex = Math.floor(Math.random() * this.allAnimals.length);
      this.targetAnimal = this.allAnimals[randomIndex];
    }
  }

  viewAnimalDetails(animal: Animal): void {
    this.currentAnimal = animal;
    this.showAnimalCard = true;
    this.show3DModel = true;
  }

  closeAnimalCard(): void {
    this.showAnimalCard = false;
    this.show3DModel = false;
    this.currentAnimal = null;
  }

  switchMode(mode: 'detection' | 'scavenger'): void {
    this.arMode = mode;
    if (mode === 'scavenger') {
      this.pickRandomTarget();
    }
  }

  resetGame(): void {
    if (confirm('Are you sure you want to reset your progress?')) {
      this.gameService.resetGame();
      this.pickRandomTarget();
    }
  }

  getImageUrl(path: string): string {
    return `http://localhost:8080${path}`;
  }

  getCollectedAnimals(): Animal[] {
    if (!this.gameState) return [];
    return Array.from(this.gameState.collectedAnimals.values())
      .map(ca => ca.animal);
  }

  isCollected(animalId: string): boolean {
    return this.gameState?.collectedAnimals.has(animalId) || false;
  }

  getRarityColor(rarity: string): string {
    switch (rarity) {
      case 'common': return '#9CA3AF';
      case 'uncommon': return '#10B981';
      case 'rare': return '#3B82F6';
      case 'legendary': return '#F59E0B';
      default: return '#6B7280';
    }
  }
}
