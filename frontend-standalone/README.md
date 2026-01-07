# Standalone HTML Frontend

This is a standalone HTML/JavaScript frontend that doesn't require Node.js, npm, or Angular.

## Features
- ✅ No build tools required
- ✅ No npm/Node.js dependencies
- ✅ Works directly in browser
- ✅ Same functionality as Angular version
- ✅ Bootstrap 5 UI
- ✅ Communicates with Spring Boot backend via REST API

## How to Use

1. **Start the Spring Boot backend first**:
   ```cmd
   cd backend
   mvn spring-boot:run
   ```

2. **Open the frontend**:
   - Simply double-click `index.html` in this folder
   - Or run `run-frontend.bat`
   - Or open `index.html` in any web browser

3. **Use the application**:
   - Upload an image
   - Select model type
   - Adjust threshold
   - Click "Upload and Detect"
   - View results

## File Structure
```
frontend-standalone/
├── index.html          # Complete frontend application
└── run-frontend.bat    # Helper script to open in browser
```

## Requirements
- Modern web browser (Chrome, Firefox, Edge)
- Spring Boot backend running on port 8080

## CORS Note
The backend is configured to allow requests from any origin, so this standalone HTML file will work even when opened directly from the file system.

## Advantages
- No installation needed
- No build process
- Easy to modify
- Works offline (except API calls)
- Can be hosted on any web server

## API Endpoints Used
- `POST http://localhost:8080/api/detection/upload` - Upload and detect
- `GET http://localhost:8080/api/detection/image/{filename}` - Get result image
- `GET http://localhost:8080/api/detection/models` - Get available models
