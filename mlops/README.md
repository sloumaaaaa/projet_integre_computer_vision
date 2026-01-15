# MLOps Setup for Animal Detection Project

This folder contains MLOps infrastructure using MLflow and DVC for experiment tracking, model versioning, and data management.

## Components

### 1. MLflow
- **Experiment Tracking**: Track YOLOv8 training runs, hyperparameters, metrics
- **Model Registry**: Version and manage trained models
- **Model Serving**: Deploy models for inference

### 2. DVC (Data Version Control)
- **Data Versioning**: Track dataset changes
- **Pipeline Management**: Automate ML workflows
- **Remote Storage**: Store large files efficiently

## Directory Structure

```
mlops/
├── mlflow/
│   ├── mlruns/           # MLflow experiment runs
│   └── models/           # Registered models
├── dvc/
│   ├── data/             # DVC-tracked datasets
│   └── models/           # DVC-tracked model files
├── pipelines/
│   └── train_pipeline.py # DVC pipeline for training
├── config/
│   └── mlflow_config.py  # MLflow configuration
├── scripts/
│   ├── train_with_mlflow.py  # Training script with MLflow tracking
│   └── register_model.py     # Model registration script
└── README.md
```

## Setup Instructions

### Prerequisites
```bash
pip install mlflow dvc dvc-gdrive ultralytics
```

### Initialize DVC
```bash
cd mlops
dvc init
```

### Start MLflow Server
```bash
mlflow server --backend-store-uri sqlite:///mlflow/mlflow.db --default-artifact-root ./mlflow/mlruns --host 0.0.0.0 --port 5000
```

Access MLflow UI at: http://localhost:5000

## Quick Start

1. **Track Training Experiment**:
   ```bash
   python scripts/train_with_mlflow.py
   ```

2. **View Experiments**:
   - Open http://localhost:5000
   - Browse experiments, compare metrics, view artifacts

3. **Version Data with DVC**:
   ```bash
   dvc add data/train
   dvc add data/valid
   git add data/train.dvc data/valid.dvc
   git commit -m "Track dataset with DVC"
   ```

4. **Register Best Model**:
   ```bash
   python scripts/register_model.py --run-id <mlflow_run_id>
   ```

## Integration with Existing Project

This MLOps setup integrates with:
- `yolov8_train_eval.py` - Enhanced with MLflow tracking
- `data/` - Dataset versioned with DVC
- `output/` - Model artifacts tracked by MLflow
