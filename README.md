# Animal Detection Application - Spring Boot + Angular

This project is a full-stack animal detection application using YOLOv8 and Roboflow models, built with Spring Boot backend and Angular frontend.

## Project Structure

```
PI/
├── backend/                 # Spring Boot backend
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   └── pom.xml
├── frontend/               # Angular frontend
│   ├── src/
│   │   └── app/
│   ├── angular.json
│   ├── package.json
│   └── tsconfig.json
├── detection_script.py    # Python script for YOLO detection
├── data/                  # Training data
├── output_yolo_labeled/   # Trained model weights
└── uploads/               # Uploaded files
```

## Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **Node.js 18+** and **npm**
- **Python 3.8+**
- **Angular CLI** (`npm install -g @angular/cli`)

## Backend Setup (Spring Boot)

### 1. Navigate to backend directory
```bash
cd backend
```

### 2. Install dependencies and build
```bash
mvn clean install
```

### 3. Run the backend
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### API Endpoints

- `POST /api/detection/upload` - Upload and detect objects in image/video
  - Parameters: `file`, `modelType`, `threshold`, `showDescriptions`
- `GET /api/detection/image/{filename}` - Get annotated image
- `GET /api/detection/models` - Get available models

## Frontend Setup (Angular)

### 1. Navigate to frontend directory
```bash
cd frontend
```

### 2. Install dependencies
```bash
npm install
```

### 3. Run the development server
```bash
npm start
# or
ng serve
```

The frontend will start on `http://localhost:4200`

## Python Environment Setup

The Python detection script requires the following packages:

```bash
pip install ultralytics inference roboflow supervision pillow numpy opencv-python
```

Ensure the trained model weights are available at:
```
output_yolo_labeled/yolov8_exp/weights/best.pt
```

## Running the Application

1. **Start the Backend** (Terminal 1):
   ```bash
   cd backend
   mvn spring-boot:run
   ```

2. **Start the Frontend** (Terminal 2):
   ```bash
   cd frontend
   npm start
   ```

3. **Access the application**: Open browser to `http://localhost:4200`

## Features

- Upload images or videos for animal detection
- Choose between YOLOv8 and Roboflow models
- Adjust detection confidence threshold
- View annotated results with bounding boxes
- Display detection metrics (number of detections, average confidence)
- Show historical descriptions of detected animal classes

## Technology Stack

### Backend
- Spring Boot 3.2.1
- Java 17
- Maven
- Python integration for ML models

### Frontend
- Angular 17
- TypeScript
- Bootstrap 5
- RxJS

### ML/AI
- YOLOv8 (Ultralytics)
- Roboflow API
- OpenCV
- PIL (Pillow)

## Configuration

### Backend Configuration (`application.properties`)
```properties
server.port=8080
spring.servlet.multipart.max-file-size=100MB
app.upload.dir=uploads
app.cors.allowed-origins=http://localhost:4200
```

### Frontend Configuration (`environment.ts`)
```typescript
export const environment = {
  apiUrl: 'http://localhost:8080/api'
};
```

## Building for Production

### Backend
```bash
cd backend
mvn clean package
java -jar target/animal-detection-1.0.0.jar
```

### Frontend
```bash
cd frontend
ng build --configuration production
```

The production build will be in `frontend/dist/`

## Troubleshooting

### Backend Issues
- **Port 8080 already in use**: Change `server.port` in `application.properties`
- **Python script not found**: Check `app.python.script.dir` path
- **Upload directory issues**: Ensure write permissions for `uploads/` folder

### Frontend Issues
- **API connection refused**: Ensure backend is running on port 8080
- **CORS errors**: Check CORS configuration in backend

### Python Issues
- **Module not found**: Install required Python packages
- **Model not found**: Ensure model weights are in correct path

## License

This project is for educational purposes.
