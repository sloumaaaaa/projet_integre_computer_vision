import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { DetectionResponse } from '../models/detection.model';

@Injectable({
  providedIn: 'root'
})
export class DetectionService {
  private apiUrl = environment.apiUrl + '/detection';

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

  getImageUrl(filename: string): string {
    return `${this.apiUrl}/image/${filename}`;
  }

  getAvailableModels(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/models`);
  }
}
