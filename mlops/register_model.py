import mlflow
from mlflow.tracking import MlflowClient

MLFLOW_TRACKING_URI = "sqlite:///mlflow/mlflow.db"

def register_model(run_id, model_name="yolov8-animal-detection", stage="Production"):
    """
    Register a trained model from MLflow run to Model Registry
    
    Args:
        run_id: MLflow run ID
        model_name: Name for the registered model
        stage: Model stage (None, Staging, Production, Archived)
    """
    mlflow.set_tracking_uri(MLFLOW_TRACKING_URI)
    client = MlflowClient()
    
    # Get run info
    run = client.get_run(run_id)
    print(f"📦 Registering model from run: {run_id}")
    print(f"🏃 Run name: {run.data.tags.get('mlflow.runName', 'N/A')}")
    
    # Get model URI
    model_uri = f"runs:/{run_id}/model"
    
    # Register model
    model_version = mlflow.register_model(model_uri, model_name)
    print(f"✅ Model registered: {model_name}")
    print(f"📌 Version: {model_version.version}")
    
    # Transition to stage if specified
    if stage:
        client.transition_model_version_stage(
            name=model_name,
            version=model_version.version,
            stage=stage
        )
        print(f"🚀 Model transitioned to: {stage}")
    
    # Add description
    client.update_model_version(
        name=model_name,
        version=model_version.version,
        description=f"YOLOv8 Animal Detection Model - Trained on {run.data.params.get('dataset', 'N/A')}"
    )
    
    print(f"\n🎉 Model registration completed!")
    print(f"🌐 View model: http://localhost:5000/#/models/{model_name}")
    
    return model_version

def list_registered_models():
    """List all registered models"""
    mlflow.set_tracking_uri(MLFLOW_TRACKING_URI)
    client = MlflowClient()
    
    models = client.search_registered_models()
    
    print("\n📚 Registered Models:")
    print("=" * 80)
    
    for model in models:
        print(f"\n📦 Model: {model.name}")
        print(f"   Description: {model.description}")
        print(f"   Versions:")
        
        for version in model.latest_versions:
            print(f"     - Version {version.version}: {version.current_stage}")
            print(f"       Run ID: {version.run_id}")
            print(f"       Created: {version.creation_timestamp}")

def get_production_model(model_name="yolov8-animal-detection"):
    """Get the production model"""
    mlflow.set_tracking_uri(MLFLOW_TRACKING_URI)
    client = MlflowClient()
    
    # Get production model
    versions = client.get_latest_versions(model_name, stages=["Production"])
    
    if versions:
        version = versions[0]
        print(f"🏭 Production Model: {model_name}")
        print(f"   Version: {version.version}")
        print(f"   Run ID: {version.run_id}")
        print(f"   Stage: {version.current_stage}")
        
        # Load model
        model_uri = f"models:/{model_name}/Production"
        model = mlflow.pytorch.load_model(model_uri)
        print(f"✅ Model loaded successfully!")
        
        return model, version
    else:
        print(f"❌ No production model found for: {model_name}")
        return None, None

if __name__ == "__main__":
    import argparse
    
    parser = argparse.ArgumentParser(description='Manage MLflow Model Registry')
    parser.add_argument('--action', type=str, choices=['register', 'list', 'get-production'], 
                        default='list', help='Action to perform')
    parser.add_argument('--run-id', type=str, help='MLflow run ID (for register action)')
    parser.add_argument('--model-name', type=str, default='yolov8-animal-detection', 
                        help='Model name')
    parser.add_argument('--stage', type=str, default='Production', 
                        choices=['None', 'Staging', 'Production', 'Archived'],
                        help='Model stage')
    
    args = parser.parse_args()
    
    if args.action == 'register':
        if not args.run_id:
            print("❌ Error: --run-id required for register action")
        else:
            register_model(args.run_id, args.model_name, args.stage)
    
    elif args.action == 'list':
        list_registered_models()
    
    elif args.action == 'get-production':
        get_production_model(args.model_name)
