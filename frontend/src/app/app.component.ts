import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DetectionService } from './services/detection.service';
import { DetectionResponse } from './models/detection.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Animal Detection App';
  
  selectedFile: File | null = null;
  modelType: string = 'yolov8';
  threshold: number = 0.5;
  showDescriptions: boolean = true;
  
  loading: boolean = false;
  result: DetectionResponse | null = null;
  error: string | null = null;
  
  availableModels: string[] = ['roboflow', 'yolov8'];

  constructor(private detectionService: DetectionService) {
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

    this.detectionService.uploadAndDetect(
      this.selectedFile,
      this.modelType,
      this.threshold,
      this.showDescriptions
    ).subscribe({
      next: (response) => {
        this.loading = false;
        if (response.status === 'success') {
          this.result = response;
        } else {
          this.error = response.message || 'Detection failed';
        }
      },
      error: (err) => {
        this.loading = false;
        this.error = 'Error uploading file: ' + (err.error?.message || err.message);
        console.error('Upload error:', err);
      }
    });
  }

  getImageUrl(url: string): string {
    return 'http://localhost:8080' + url;
  }

  getMetricsArray(): { key: string, value: any }[] {
    if (!this.result?.metrics) return [];
    return Object.entries(this.result.metrics).map(([key, value]) => ({ key, value }));
  }

  getDescriptionsArray(): { key: string, value: string }[] {
    if (!this.result?.descriptions) return [];
    return Object.entries(this.result.descriptions).map(([key, value]) => ({ key, value }));
  }
}
