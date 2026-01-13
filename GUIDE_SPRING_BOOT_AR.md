# 🎮 AR Experience avec Spring Boot Backend

## ✅ Configuration Correcte

**Backend:** Spring Boot (Java) sur port 8080  
**Frontend:** HTML/JS standalone (pas de Node.js nécessaire)

## 🚀 DÉMARRAGE RAPIDE

### Option 1: Automatique (Recommandé)
```
Double-cliquez: START-SPRING-AR.bat
```
Cela démarre le backend ET ouvre le frontend automatiquement!

### Option 2: Manuel

**Étape 1 - Démarrer le Backend:**
```bash
cd backend
mvn spring-boot:run
```
Attendez le message: `Started AnimalDetectionApplication`

**Étape 2 - Ouvrir le Frontend:**
```
Double-cliquez: frontend-standalone\index-ar.html
```

## 🧪 Tester la Connexion

Avant d'utiliser l'app AR, vérifiez que tout fonctionne:
```
Ouvrez: frontend-standalone\test-connection.html
Cliquez sur "Tester la Connexion"
```

Si vous voyez "✓ CONNEXION RÉUSSIE", tout est prêt!

## 🎮 Utilisation

1. **Classic Mode**: Detection classique d'animaux
2. **AR Experience**: Jeu de collection avec points et badges

### Mode AR:
- 🎯 Trouvez l'animal cible affiché en haut
- 📸 Uploadez une image contenant cet animal
- ⭐ Gagnez des points (100-250 selon la rareté)
- 🔥 Construisez des streaks
- 🏆 Débloquez 5 badges

## 📂 Structure

```
PI/
├── backend/                    ← Spring Boot (Java)
│   └── mvn spring-boot:run    ← Port 8080
│
├── frontend-standalone/        ← HTML/JS pur
│   ├── index-ar.html          ← Application AR
│   └── test-connection.html   ← Test diagnostic
│
└── START-SPRING-AR.bat        ← Lanceur automatique
```

## 🔧 En Cas de Problème

### "Failed to fetch"
- ✅ Vérifiez que Spring Boot tourne (`mvn spring-boot:run`)
- ✅ Attendez 20-30 secondes que le serveur démarre
- ✅ Testez avec `test-connection.html`

### Backend ne démarre pas
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### CORS errors
Le backend Spring Boot devrait avoir CORS activé par défaut.
Si problème, vérifiez `WebConfig.java` dans le backend.

## 🎯 Différences vs Flask

❌ **N'utilisez PAS:** `app.py` (Flask - Python)  
✅ **Utilisez:** Backend Spring Boot (Java)

Le frontend a été mis à jour pour pointer vers:
- `http://localhost:8080/api/detection` (Spring Boot)
- Au lieu de `http://localhost:5000` (Flask)

## 📊 Endpoints Spring Boot Utilisés

- `POST /api/detection/upload` - Upload et détection
- `GET /api/detection/models` - Liste des modèles
- `GET /api/detection/image/{filename}` - Récupération d'images

## 🎉 C'est Tout!

Lancez `START-SPRING-AR.bat` et commencez à collecter des animaux!

---

**Note:** Flask (app.py) n'est plus nécessaire. Tout fonctionne avec Spring Boot.
