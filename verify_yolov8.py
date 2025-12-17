try:
    from ultralytics import YOLO
    import torch
    print("YOLOv8 (ultralytics) is installed.")
    print("PyTorch version:", torch.__version__)
    print("CUDA available:", torch.cuda.is_available())
except Exception as e:
    print("YOLOv8 or PyTorch not installed or GPU issue:", e)
