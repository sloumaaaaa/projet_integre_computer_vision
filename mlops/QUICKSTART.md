# MLOps Quick Start Guide

## Installation

```bash
cd mlops
pip install -r requirements.txt
```

## Step 1: Initialize DVC

```bash
# Initialize DVC in the project
dvc init

# Track your dataset with DVC
dvc add ../data/train
dvc add ../data/valid
dvc add ../data/test

# Commit DVC files to git
git add ../data/train.dvc ../data/valid.dvc ../data/test.dvc .dvc/.gitignore
git commit -m "Track dataset with DVC"
```

## Step 2: Start MLflow Server

```bash
# Start MLflow tracking server
mlflow server --backend-store-uri sqlite:///mlflow/mlflow.db --default-artifact-root ./mlflow/mlruns --host 0.0.0.0 --port 5000
```

Open MLflow UI: http://localhost:5000

## Step 3: Train Model with MLflow Tracking

```bash
# Basic training
python train_with_mlflow.py

# With custom parameters
python train_with_mlflow.py --epochs 100 --batch 32 --model yolov8s.pt

# Training options:
# --data: Path to data.yaml (default: ../data.yaml)
# --model: YOLOv8 variant (yolov8n.pt, yolov8s.pt, yolov8m.pt, yolov8l.pt, yolov8x.pt)
# --epochs: Number of training epochs
# --imgsz: Image size for training
# --batch: Batch size
# --project: Output directory
# --name: Experiment name
```

## Step 4: View Experiments in MLflow

1. Open http://localhost:5000
2. Click on "animal-detection-yolov8" experiment
3. Compare different runs:
   - Parameters (model, epochs, batch size, etc.)
   - Metrics (mAP50, mAP50-95, precision, recall)
   - Artifacts (model weights, plots, confusion matrix)

## Step 5: Register Best Model

```bash
# After training, register the best model
python register_model.py --action register --run-id <RUN_ID_FROM_MLFLOW>

# List all registered models
python register_model.py --action list

# Get production model
python register_model.py --action get-production
```

## Step 6: Run DVC Pipeline

```bash
# Run the entire pipeline
dvc repro

# Run specific stage
dvc repro evaluate

# View pipeline DAG
dvc dag
```

## DVC Commands Cheat Sheet

```bash
# Track data
dvc add data/file.csv

# Push data to remote
dvc push

# Pull data from remote
dvc pull

# Check data status
dvc status

# View pipeline
dvc dag

# Run pipeline
dvc repro

# Show metrics
dvc metrics show

# Compare experiments
dvc exp show
```

## MLflow Commands Cheat Sheet

```bash
# Start UI
mlflow ui

# Search runs
mlflow runs list --experiment-name animal-detection-yolov8

# Download artifacts
mlflow artifacts download --run-id <RUN_ID>

# Serve model
mlflow models serve -m models:/yolov8-animal-detection/Production -p 5001
```

## Example Workflow

1. **Experiment with hyperparameters**:
   ```bash
   python train_with_mlflow.py --epochs 50 --batch 16
   python train_with_mlflow.py --epochs 100 --batch 32
   python train_with_mlflow.py --model yolov8s.pt --epochs 75
   ```

2. **Compare results in MLflow UI**:
   - View all runs side by side
   - Compare metrics
   - Visualize training curves

3. **Register best model**:
   ```bash
   python register_model.py --action register --run-id <BEST_RUN_ID>
   ```

4. **Version control data changes**:
   ```bash
   # Updated dataset
   dvc add ../data/train
   git add ../data/train.dvc
   git commit -m "Updated training data v2"
   dvc push
   ```

5. **Reproduce experiments**:
   ```bash
   dvc repro
   ```

## Integration with Spring Boot Backend

The best model can be deployed and used by your Spring Boot backend:

```python
# Load production model
import mlflow
mlflow.set_tracking_uri("sqlite:///mlflow/mlflow.db")
model = mlflow.pytorch.load_model("models:/yolov8-animal-detection/Production")
```

## Troubleshooting

**MLflow UI not starting?**
```bash
# Check if port 5000 is already in use
netstat -ano | findstr :5000

# Use different port
mlflow server --port 5001
```

**DVC remote storage?**
```bash
# Configure Google Drive remote
dvc remote add -d gdrive gdrive://your-folder-id

# Configure local remote (for testing)
dvc remote add -d local /path/to/storage
```

## Next Steps

- Set up automated ML pipelines with DVC
- Integrate with CI/CD (GitHub Actions)
- Deploy models with MLflow Model Serving
- Monitor model performance in production
