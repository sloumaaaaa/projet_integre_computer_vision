# 3D Animal Models Setup Guide

## 🎯 Current Status
The 3D Model Viewer is **fully functional** but currently uses placeholder models from free CDNs. To get actual animal models, follow this guide.

## 📦 Quick Setup (Using Free CDN Models)
✅ **Already Working!** The app uses free models from:
- Google Model Viewer examples
- Khronos glTF Sample Models

These work immediately but aren't actual animals (they're robots, astronauts, etc.).

## 🦁 Getting Real Animal Models

### Option 1: Free Model Sites (Recommended)

#### **Sketchfab** (Best Quality)
1. Go to https://sketchfab.com/
2. Search for: "elephant low poly", "tiger 3d model", etc.
3. Filter by: **Free** and **Downloadable**
4. Look for models with **CC license** (Creative Commons)
5. Download as **glTF/GLB** format
6. Save to `frontend-standalone/assets/` folder

**Example searches:**
- https://sketchfab.com/search?q=elephant+low+poly&type=models&license=322a749bcfa841b29dff1e8a1bb74b0b
- https://sketchfab.com/search?q=tiger+3d&type=models&license=322a749bcfa841b29dff1e8a1bb74b0b

#### **Poly Pizza** (Google Poly Archive)
1. Go to https://poly.pizza/
2. Search for animals: elephant, tiger, lion, wolf, pig
3. Download as GLB format
4. Save to `frontend-standalone/assets/`

#### **CGTrader Free Section**
1. Go to https://www.cgtrader.com/free-3d-models
2. Search for animals
3. Filter by "Free"
4. Download GLB/glTF format

### Option 2: AI-Generated Models

#### **Meshy AI** (Free Tier)
1. Go to https://www.meshy.ai/
2. Use text-to-3D: "low poly elephant", etc.
3. Download as GLB
4. Free tier allows 200 credits/month

#### **Kaedim** (Trial)
1. Upload animal images
2. Convert to 3D
3. Download GLB

## 📁 File Structure

Once downloaded, place files here:
```
frontend-standalone/
├── assets/
│   ├── elephant.glb
│   ├── tiger.glb
│   ├── lion.glb
│   ├── wolf.glb
│   └── pig.glb
├── index-ar.html
└── ...
```

## 🔧 Update Code

After adding models to `/assets/`, update `index-ar.html`:

Find the `get3DModelUrl` function and uncomment local paths:

```javascript
function get3DModelUrl(animalName) {
    const models = {
        // Use local models
        'elephant': './assets/elephant.glb',
        'tiger': './assets/tiger.glb',
        'lion': './assets/lion.glb',
        'wolf': './assets/wolf.glb',
        'pig': './assets/pig.glb'
    };
    
    return models[animalName.toLowerCase()] || null;
}
```

## 📱 Testing AR Features

### Desktop Browser
1. Open `index-ar.html`
2. Switch to "AR Experience" mode
3. Upload an animal image
4. Click on collected animal to view 3D model
5. Model will rotate automatically

### Mobile Device (True AR)
1. Open on **Android** (Chrome) or **iOS** (Safari)
2. View 3D model
3. Tap **"View in AR"** button
4. Camera will open showing animal in your space!

**Requirements:**
- Android: ARCore supported device
- iOS: ARKit supported device (iPhone 6S+)

## 🎨 Model Optimization Tips

For best performance:
- **File size**: Keep under 5MB per model
- **Polygon count**: 5,000-20,000 triangles
- **Textures**: 1024x1024 or 2048x2048 max
- **Format**: GLB (binary glTF) preferred over glTF

## 🔗 Recommended Free Models

### Ready-to-Use Animal Packs:
1. **Quaternius** - https://quaternius.com/packs.html
   - Ultimate Animals Pack (free, low poly)
   - Already in GLB format!

2. **Kenney Assets** - https://kenney.nl/assets
   - Free animal models
   - May need conversion to GLB

3. **Free3D** - https://free3d.com/3d-models/animals
   - Various quality levels
   - Check license before use

## 🛠️ Convert Other Formats to GLB

If you have FBX, OBJ, or other formats:

### Online Converters:
- https://products.aspose.app/3d/conversion/fbx-to-glb
- https://anyconv.com/obj-to-glb-converter/

### Blender (Free Software):
1. Download Blender: https://www.blender.org/
2. File → Import → FBX/OBJ
3. File → Export → glTF 2.0 (.glb)
4. Export settings: Include animations, Apply modifiers

## ⚡ Quick Start with Quaternius

**Fastest way to get working:**

1. Download: https://quaternius.com/packs/ultimateanimals.html
2. Extract the ZIP
3. Copy these files to `assets/`:
   - Find elephant model → rename to `elephant.glb`
   - Find tiger/big cat → rename to `tiger.glb`
   - Find lion → rename to `lion.glb`
   - Find wolf → rename to `wolf.glb`
   - Find pig → rename to `pig.glb`
4. Update code as shown above
5. Done! 🎉

## 🐛 Troubleshooting

**Model not loading?**
- Check browser console (F12) for errors
- Verify file path is correct
- Try opening GLB directly in browser
- Test at: https://gltf-viewer.donmccurdy.com/

**AR button not working?**
- Needs HTTPS (won't work on http://)
- Use local server or deploy to hosting
- Check device AR compatibility

**Model too big/small in AR?**
- Edit GLB in Blender
- Scale to real-world size (elephant ≈ 3m tall)

## 📊 Current Model Mapping

| Animal   | Current Model | Ideal Replacement |
|----------|---------------|-------------------|
| Elephant | Fox (placeholder) | Real elephant GLB |
| Tiger    | Astronaut (placeholder) | Real tiger GLB |
| Lion     | Horse (placeholder) | Real lion GLB |
| Wolf     | Fox (placeholder) | Real wolf GLB |
| Pig      | Robot (placeholder) | Real pig GLB |

## 🎯 Next Steps

1. ✅ Model viewer is working with placeholders
2. ⏳ Download real animal models
3. ⏳ Place in `/assets/` folder
4. ⏳ Update `get3DModelUrl()` function
5. ⏳ Test on mobile for AR
6. ✅ Enjoy!

---

**Need Help?** 
The model viewer works right now with placeholder models. You can test all features except true AR viewing of actual animals.
