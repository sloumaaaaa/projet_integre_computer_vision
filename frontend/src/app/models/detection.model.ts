export interface DetectionRequest {
  modelType: string;
  threshold: number;
  showDescriptions: boolean;
}

export interface DetectionResponse {
  annotatedImageUrl: string;
  metrics: { [key: string]: any };
  descriptions?: { [key: string]: string };
  classDescriptions?: { [key: string]: string };
  detectedClasses?: string[];
  status: string;
  message: string;
}
