import mlflow
import mlflow.pytorch
from ultralytics import YOLO
import os
import yaml
from pathlib import Path

# MLflow configuration
MLFLOW_TRACKING_URI = "sqlite:///./mlflow.db"
EXPERIMENT_NAME = "animal-detection-yolov8"

def setup_mlflow():
    """Initialize MLflow tracking"""
    mlflow.set_tracking_uri(MLFLOW_TRACKING_URI)
    mlflow.set_experiment(EXPERIMENT_NAME)
    print(f"✅ MLflow tracking URI: {MLFLOW_TRACKING_URI}")
    print(f"✅ Experiment: {EXPERIMENT_NAME}")

def train_yolov8_with_mlflow(
    data_yaml='../data.yaml',
    model_name='yolov8n.pt',
    epochs=50,
    imgsz=640,
    batch=16,
    project='../output',
    name='yolov8_mlflow_exp'
):
    """
    Train YOLOv8 model with MLflow tracking
    """
    setup_mlflow()
    
    # Load data config
    with open(data_yaml, 'r') as f:
        data_config = yaml.safe_load(f)
    
    # Start MLflow run
    with mlflow.start_run(run_name=f"{model_name}_e{epochs}") as run:
        print(f"\n🚀 Starting MLflow Run: {run.info.run_id}\n")
        
        # Log parameters
        mlflow.log_param("model", model_name)
        mlflow.log_param("epochs", epochs)
        mlflow.log_param("image_size", imgsz)
        mlflow.log_param("batch_size", batch)
        mlflow.log_param("num_classes", data_config.get('nc', 5))
        mlflow.log_param("dataset", os.path.basename(data_yaml))
        
        # Log dataset info
        mlflow.log_dict(data_config, "dataset_config.yaml")
        
        # Load and train model
        model = YOLO(model_name)
        
        # Train
        results = model.train(
            data=data_yaml,
            epochs=epochs,
            imgsz=imgsz,
            batch=batch,
            project=project,
            name=name,
            verbose=True
        )
        
        # Log metrics
        # MLflow will track metrics during training if available
        metrics_file = Path(project) / name / 'results.csv'
        if metrics_file.exists():
            import pandas as pd
            df = pd.read_csv(metrics_file)
            
            # Log final metrics
            final_metrics = df.iloc[-1]
            mlflow.log_metric("final_mAP50", float(final_metrics.get('metrics/mAP50(B)', 0)))
            mlflow.log_metric("final_mAP50-95", float(final_metrics.get('metrics/mAP50-95(B)', 0)))
            mlflow.log_metric("final_precision", float(final_metrics.get('metrics/precision(B)', 0)))
            mlflow.log_metric("final_recall", float(final_metrics.get('metrics/recall(B)', 0)))
            
            # Log metrics CSV as artifact
            mlflow.log_artifact(str(metrics_file))
        
        # Validate model
        val_results = model.val()
        mlflow.log_metric("val_mAP50", val_results.box.map50)
        mlflow.log_metric("val_mAP50-95", val_results.box.map)
        
        # Log model artifacts
        weights_path = Path(project) / name / 'weights' / 'best.pt'
        if weights_path.exists():
            mlflow.log_artifact(str(weights_path), "weights")
            print(f"✅ Logged model weights: {weights_path}")
        
        # Log confusion matrix and other plots
        results_dir = Path(project) / name
        for img_file in results_dir.glob('*.png'):
            mlflow.log_artifact(str(img_file), "plots")
        
        # Log the entire results directory
        mlflow.log_artifacts(str(results_dir), "training_results")
        
        # Log model to MLflow Model Registry
        mlflow.pytorch.log_model(
            model.model,
            "model",
            registered_model_name=f"yolov8-animal-detection"
        )
        
        print(f"\n✅ Training completed!")
        print(f"📊 View results: http://localhost:5000/#/experiments/{run.info.experiment_id}/runs/{run.info.run_id}")
        print(f"🏷️  Run ID: {run.info.run_id}")
        
        return run.info.run_id

if __name__ == "__main__":
    import argparse
    
    parser = argparse.ArgumentParser(description='Train YOLOv8 with MLflow tracking')
    parser.add_argument('--data', type=str, default='../data.yaml', help='Path to data.yaml')
    parser.add_argument('--model', type=str, default='yolov8n.pt', help='YOLOv8 model variant')
    parser.add_argument('--epochs', type=int, default=50, help='Number of epochs')
    parser.add_argument('--imgsz', type=int, default=640, help='Image size')
    parser.add_argument('--batch', type=int, default=16, help='Batch size')
    parser.add_argument('--project', type=str, default='../output', help='Project directory')
    parser.add_argument('--name', type=str, default='yolov8_mlflow_exp', help='Experiment name')
    
    args = parser.parse_args()
    
    run_id = train_yolov8_with_mlflow(
        data_yaml=args.data,
        model_name=args.model,
        epochs=args.epochs,
        imgsz=args.imgsz,
        batch=args.batch,
        project=args.project,
        name=args.name
    )
    
    print(f"\n🎉 MLflow Run ID: {run_id}")
    print(f"🌐 Open MLflow UI: http://localhost:5000")
