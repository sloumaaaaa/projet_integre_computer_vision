export interface DetectionRequest {
  modelType: string;
  threshold: number;
  showDescriptions: boolean;
}

export interface DetectionResponse {
  annotatedImageUrl: string;
  metrics: { [key: string]: any };
  descriptions?: { [key: string]: string };
  status: string;
  message: string;
}
