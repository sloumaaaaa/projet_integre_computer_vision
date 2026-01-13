# AR Experience - Setup Guide

## 🎮 New Features Added

### 1. **3D Animal Model Placement**
- Interactive 3D models appear when animals are detected
- Rotate, zoom, and view animals from all angles
- "View in AR" button for mobile devices (ARCore/ARKit support)
- Educational information cards with fun facts

### 2. **Educational AR Scavenger Hunt**
- Gamified animal collection system
- Target animals to find
- Points system based on rarity:
  - Common animals: 100 points
  - Uncommon animals: 150-175 points
  - Rare animals: 200-250 points
- Progress tracking and streak system
- Achievement badges

## 📁 Assets Needed

To get the full AR experience working, you need to add 3D models and thumbnails:

### Required Directory Structure:
```
frontend/src/assets/
├── models/
│   ├── elephant.glb
│   ├── lion.glb
│   ├── tiger.glb
│   ├── wolf.glb
│   └── pig.glb
├── thumbnails/
│   ├── elephant.png
│   ├── lion.png
│   ├── tiger.png
│   ├── wolf.png
│   └── pig.png
├── badges/
│   ├── first.png
│   ├── collector.png
│   ├── streak.png
│   ├── points.png
│   └── rare.png
└── placeholder.png
```

### Where to Get 3D Models (Free Resources):

1. **Sketchfab** (https://sketchfab.com/)
   - Search for "elephant", "lion", etc.
   - Filter by "Downloadable" and "CC License"
   - Download as GLB/GLTF format

2. **Poly Haven** (https://polyhaven.com/)
   - High-quality free 3D models
   - Download as GLB format

3. **Google Poly Archive** (via third-party mirrors)
   - Many animal models available

4. **CGTrader** (https://www.cgtrader.com/free-3d-models)
   - Free section with animal models

### Creating Placeholder Assets (Quick Start):

If you want to test without full 3D models, create a simple placeholder:

```bash
# Create directories
mkdir -p frontend/src/assets/models
mkdir -p frontend/src/assets/thumbnails
mkdir -p frontend/src/assets/badges
```

For now, the app will use placeholder images when assets are missing.

## 🚀 How to Use

### Classic Mode:
1. Click "Classic Mode" button
2. Upload an image with animals
3. View detection results as before

### AR Experience Mode:
1. Click "AR Experience" button
2. Choose between:
   - **Detection Mode**: Upload images to detect and collect animals
   - **Scavenger Hunt**: Find specific target animals for bonus points

3. Upload an image
4. If animals are detected:
   - See animated discovery notification
   - Earn points based on rarity
   - View 3D model of the animal
   - Read fun facts and educational info
   - On mobile: Click "View in AR" to place in your space

### Game Features:
- **Level System**: Gain levels every 500 points
- **Streak System**: Find animals consecutively without errors
- **Collection**: Track which animals you've discovered
- **Badges**: Unlock achievements
  - First Steps: Discover your first animal
  - Collector: Find all 5 animal types
  - On a Roll: 5 animal streak
  - Point Master: Earn 1000 points
  - Rare Hunter: Find a rare/legendary animal

## 🎯 Game Mechanics

### Points System:
- **Pig** (Common): 100 points
- **Elephant** (Uncommon): 150 points
- **Wolf** (Uncommon): 175 points
- **Lion** (Rare): 200 points
- **Tiger** (Rare): 250 points

### Rarity Colors:
- 🟤 Common: Gray
- 🟢 Uncommon: Green
- 🔵 Rare: Blue
- 🟠 Legendary: Orange

### Progress Tracking:
- All progress saved in browser localStorage
- Survives page refreshes
- Reset button available if you want to start over

## 📱 Mobile AR Features

On compatible mobile devices (iOS 12+, Android 8+):
1. Detect an animal
2. View the 3D model
3. Tap "View in AR 📱" button
4. Point camera at floor/table
5. Place life-size animal in your space
6. Walk around it, take photos!

## 🔧 Technical Details

### Technologies Used:
- **Model Viewer**: Google's web component for 3D models
- **WebXR**: Cross-platform AR on web
- **GLB/GLTF**: Efficient 3D model format
- **Local Storage**: Persistent game state

### Browser Compatibility:
- Chrome/Edge: Full support
- Safari iOS: Full AR support
- Firefox: 3D models only (no AR)

### API Endpoints Added:
- `GET /api/animals`: Get animal metadata
- `POST /api/detect-with-classes`: Enhanced detection with class names

## 🎨 Customization

You can easily customize:

1. **Add more animals** in [game.service.ts](frontend/src/app/services/game.service.ts)
2. **Change point values** in the animals array
3. **Add new badges** in `initializeBadges()`
4. **Modify rarity tiers** and colors
5. **Add more fun facts** to each animal

## 🐛 Troubleshooting

**3D models not loading?**
- Check that GLB files are in `frontend/src/assets/models/`
- Ensure file names match exactly: `elephant.glb`, `lion.glb`, etc.

**AR button not working?**
- Ensure you're on a mobile device with ARCore/ARKit
- Check browser supports WebXR
- Models must be under 10MB for AR

**Detection not tracking animals?**
- Check Flask backend is running on port 5000
- Verify `/api/detect-with-classes` endpoint is working
- Check browser console for errors

## 📚 Future Enhancements

Potential additions:
- Leaderboard with backend storage
- Multiplayer challenges
- More animal species
- Daily quests
- Social sharing
- Photo capture with AR animals
- Sound effects
- Animations for discoveries

## 🎉 Enjoy!

Have fun collecting animals and learning about wildlife through AR!
