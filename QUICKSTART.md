# Quick Start Guide

## Setup Instructions

### 1. Backend (Spring Boot)

Run the setup script:
```cmd
setup-backend.bat
```

Or manually:
```cmd
cd backend
mvn clean install
```

### 2. Frontend (Angular)

Run the setup script:
```cmd
setup-frontend.bat
```

Or manually:
```cmd
cd frontend
npm install
```

### 3. Python Dependencies

Ensure Python packages are installed:
```cmd
pip install ultralytics inference roboflow supervision pillow numpy opencv-python
```

## Running the Application

### Option 1: Using Scripts (Recommended)

Open two terminal windows:

**Terminal 1 - Backend:**
```cmd
run-backend.bat
```

**Terminal 2 - Frontend:**
```cmd
run-frontend.bat
```

### Option 2: Manual Start

**Terminal 1 - Backend:**
```cmd
cd backend
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```cmd
cd frontend
npm start
```

## Access the Application

Open your browser and navigate to:
```
http://localhost:4200
```

The backend API will be running at:
```
http://localhost:8080
```

## Testing the Application

1. Click "Choose File" and select an image with animals
2. Select the detection model (YOLOv8 or Roboflow)
3. Adjust the detection threshold using the slider
4. Click "Upload and Detect"
5. View the annotated image with detected animals
6. Check the metrics and class descriptions

## Project Architecture

```
┌─────────────────┐
│  Angular        │
│  Frontend       │ http://localhost:4200
│  (Port 4200)    │
└────────┬────────┘
         │ HTTP/REST
         │
┌────────▼────────┐
│  Spring Boot    │
│  Backend        │ http://localhost:8080
│  (Port 8080)    │
└────────┬────────┘
         │ Process Execution
         │
┌────────▼────────┐
│  Python         │
│  Detection      │
│  Script         │
└─────────────────┘
```

## Key Files Modified/Created

### Backend Files
- `backend/pom.xml` - Maven configuration
- `backend/src/main/java/com/esprit/animaldetection/` - Java source files
  - `AnimalDetectionApplication.java` - Main Spring Boot app
  - `controller/DetectionController.java` - REST API endpoints
  - `service/DetectionService.java` - Business logic
  - `model/` - Data models

### Frontend Files
- `frontend/package.json` - npm dependencies
- `frontend/src/app/` - Angular application
  - `app.component.ts` - Main component
  - `services/detection.service.ts` - API communication
  - `models/detection.model.ts` - TypeScript interfaces

### Python Files
- `detection_script.py` - YOLO detection script (called from Java)

## Troubleshooting

### Backend won't start
- Check Java version: `java -version` (should be 17+)
- Check if port 8080 is free
- Review logs in terminal

### Frontend won't start
- Check Node.js version: `node -v` (should be 18+)
- Delete `node_modules` and run `npm install` again
- Check if port 4200 is free

### Detection fails
- Ensure Python packages are installed
- Check if model weights exist at: `output_yolo_labeled/yolov8_exp/weights/best.pt`
- Verify Python is in system PATH

## Next Steps

1. Customize the UI in `frontend/src/app/app.component.html`
2. Add more model options in backend
3. Implement video detection support
4. Add database persistence for results
5. Deploy to production environment
