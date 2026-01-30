"""
YOLOv8 training and evaluation script (replaces Detectron2)
"""
from ultralytics import YOLO
import os

def main():
	# Train YOLOv8 model
	model = YOLO('yolov8n.pt')  # You can change to yolov8s.pt, yolov8m.pt, etc.
	results = model.train(
		data='data.yaml',  # Path to your dataset config (to be created)
		epochs=50,
		imgsz=640,
		project='output',
		name='yolov8_exp',
		exist_ok=True
	)

	# Evaluate on validation set
	metrics = model.val(data='data.yaml')
	print('Evaluation metrics:', metrics)

if __name__ == "__main__":
	main()