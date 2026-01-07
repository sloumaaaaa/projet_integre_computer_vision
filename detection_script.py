"""
Python script to perform YOLO detection and return results as JSON
Called from Java Spring Boot backend
"""
import sys
import json
import os
from PIL import Image
import numpy as np

CLASS_DESCRIPTIONS = {
    "elephant": "Throughout history, elephants have been revered in many cultures for their size, strength, and intelligence. They played important roles in ancient warfare, royal processions, and religious symbolism, especially in Asia and Africa. Today, elephants are recognized for their complex social structures and remarkable memory.",
    "lion": "Lions have symbolized courage and royalty since ancient times, appearing in the art and mythology of civilizations such as Egypt, Greece, and Persia. Known as the 'king of the jungle,' lions live in social groups called prides and have been subjects of fascination and reverence for millennia.",
    "pig": "Domesticated over 9,000 years ago, pigs have been a vital source of food and cultural significance in societies worldwide. They are featured in ancient Chinese zodiac, European folklore, and are known for their intelligence and adaptability.",
    "tiger": "Tigers have been admired and feared throughout history, often representing power and majesty in Asian cultures. They are the largest wild cats and have inspired countless legends, artworks, and conservation efforts due to their beauty and strength.",
    "wolf": "Wolves have played a central role in human folklore, mythology, and history, symbolizing both danger and loyalty. From the founding myth of Rome to Native American legends, wolves are known for their pack behavior, intelligence, and adaptability."
}

def detect_with_yolov8(image_path, threshold):
    """Perform detection using YOLOv8 model"""
    from ultralytics import YOLO
    import cv2
    
    image = Image.open(image_path).convert("RGB")
    # Get the script's directory and construct absolute path
    script_dir = os.path.dirname(os.path.abspath(__file__))
    model_path = os.path.join(script_dir, 'output_yolo_labeled', 'yolov8_exp', 'weights', 'best.pt')
    model = YOLO(model_path)
    
    results = model.predict(image, conf=threshold, save=False, verbose=False)[0]
    annotated_image = np.array(image).copy()
    detected_classes = set()
    
    for box, score, cls in zip(
        results.boxes.xyxy.cpu().numpy(), 
        results.boxes.conf.cpu().numpy(), 
        results.boxes.cls.cpu().numpy()
    ):
        if score >= threshold:
            x1, y1, x2, y2 = map(int, box)
            label = model.names[int(cls)]
            detected_classes.add(label.strip().lower())
            
            cv2.rectangle(annotated_image, (x1, y1), (x2, y2), (0, 255, 0), 2)
            cv2.putText(
                annotated_image, 
                f'{label} {score:.2f}', 
                (x1, y1-10), 
                cv2.FONT_HERSHEY_SIMPLEX, 
                0.7, 
                (0, 255, 0), 
                2
            )
    
    annotated_image = Image.fromarray(annotated_image)
    num_detections = len(results.boxes)
    avg_confidence = float(np.mean(results.boxes.conf.cpu().numpy())) if num_detections > 0 else 0.0
    
    return annotated_image, num_detections, avg_confidence, detected_classes

def detect_with_roboflow(image_path, threshold):
    """Perform detection using Roboflow model"""
    from inference import get_model
    import supervision as sv
    
    model = get_model(model_id="animal-detection-ioduj/2")
    image = Image.open(image_path).convert("RGB")
    
    raw_results = model.infer(image)
    results = raw_results[0] if isinstance(raw_results, list) else raw_results
    detections = sv.Detections.from_inference(results)
    
    # Apply threshold filter
    mask = detections.confidence >= threshold
    detections = detections[mask]
    
    # Annotate image
    box_annotator = sv.BoxAnnotator()
    label_annotator = sv.LabelAnnotator()
    annotated_image = box_annotator.annotate(scene=image, detections=detections)
    annotated_image = label_annotator.annotate(scene=annotated_image, detections=detections)
    
    num_detections = len(detections)
    avg_confidence = float(detections.confidence.mean()) if num_detections > 0 else 0.0
    
    detected_classes = set()
    if num_detections > 0:
        # Extract class names from the predictions attribute (not .get())
        if hasattr(results, 'predictions'):
            predictions = results.predictions
            for prediction in predictions:
                if prediction.confidence >= threshold:
                    class_name = prediction.class_name if hasattr(prediction, 'class_name') else str(getattr(prediction, 'class', 'unknown'))
                    detected_classes.add(class_name.strip().lower())
    
    return annotated_image, num_detections, avg_confidence, detected_classes

def main():
    if len(sys.argv) < 5:
        print(json.dumps({"error": "Invalid arguments"}))
        sys.exit(1)
    
    image_path = sys.argv[1]
    model_type = sys.argv[2]
    threshold = float(sys.argv[3])
    show_descriptions = sys.argv[4].lower() == 'true'
    
    try:
        # Perform detection based on model type
        if model_type == "roboflow":
            annotated_image, num_detections, avg_confidence, detected_classes = detect_with_roboflow(image_path, threshold)
        else:  # yolov8
            annotated_image, num_detections, avg_confidence, detected_classes = detect_with_yolov8(image_path, threshold)
        
        # Save annotated image
        base_name = os.path.basename(image_path)
        name_without_ext = os.path.splitext(base_name)[0]
        annotated_filename = f"annotated_{name_without_ext}.png"
        script_dir = os.path.dirname(os.path.abspath(__file__))
        annotated_path = os.path.join(script_dir, 'uploads', annotated_filename)
        annotated_image.save(annotated_path)
        
        # Prepare response
        response = {
            "annotated_filename": annotated_filename,
            "metrics": {
                "Number of Detections": num_detections,
                "Average Confidence": f"{avg_confidence:.2f}"
            },
            "descriptions": {}
        }
        
        # Add descriptions if requested
        if show_descriptions and detected_classes:
            response["descriptions"] = {
                cls.capitalize(): CLASS_DESCRIPTIONS.get(cls, "No description available.")
                for cls in detected_classes
            }
        
        # Output JSON result
        print(json.dumps(response))
        
    except Exception as e:
        print(json.dumps({"error": str(e)}))
        sys.exit(1)

if __name__ == "__main__":
    main()
