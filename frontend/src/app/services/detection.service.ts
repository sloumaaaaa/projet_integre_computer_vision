import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { DetectionResponse } from '../models/detection.model';
import { Animal } from '../models/game.model';

@Injectable({
  providedIn: 'root'
})
export class DetectionService {
  private apiUrl = environment.apiUrl + '/detection';
  private backendUrl = 'http://localhost:5000';

  constructor(private http: HttpClient) {}

  uploadAndDetect(
    file: File, 
    modelType: string, 
    threshold: number, 
    showDescriptions: boolean
  ): Observable<DetectionResponse> {
    const formData = new FormData();
    formData.append('file', file);
    
    const params = new HttpParams()
      .set('modelType', modelType)
      .set('threshold', threshold.toString())
      .set('showDescriptions', showDescriptions.toString());

    return this.http.post<DetectionResponse>(
      `${this.apiUrl}/upload`, 
      formData, 
      { params }
    );
  }

  detectObjects(
    file: File,
    modelType: string,
    threshold: number,
    showDescriptions: boolean
  ): Observable<DetectionResponse> {
    const formData = new FormData();
    formData.append('image', file);
    formData.append('model', modelType);
    formData.append('threshold', threshold.toString());

    return this.http.post<DetectionResponse>(
      `${this.backendUrl}/api/detect-with-classes`,
      formData
    );
  }

  getAnimals(): Observable<{ status: string; animals: Animal[] }> {
    return this.http.get<{ status: string; animals: Animal[] }>(
      `${this.backendUrl}/api/animals`
    );
  }

  getImageUrl(filename: string): string {
    return `${this.apiUrl}/image/${filename}`;
  }

  getAvailableModels(): Observable<string[]> {
    // Return default models for now
    return new Observable(observer => {
      observer.next(['roboflow', 'yolov8']);
      observer.complete();
    });
  }
}
