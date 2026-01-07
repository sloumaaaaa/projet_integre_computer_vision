CLASS_DESCRIPTIONS = {
    "elephant": (
        "Throughout history, elephants have been revered in many cultures for their size, strength, and intelligence. "
        "They played important roles in ancient warfare, royal processions, and religious symbolism, especially in Asia and Africa. "
        "Today, elephants are recognized for their complex social structures and remarkable memory."
    ),
    "lion": (
        "Lions have symbolized courage and royalty since ancient times, appearing in the art and mythology of civilizations such as Egypt, Greece, and Persia. "
        "Known as the 'king of the jungle,' lions live in social groups called prides and have been subjects of fascination and reverence for millennia."
    ),
    "pig": (
        "Domesticated over 9,000 years ago, pigs have been a vital source of food and cultural significance in societies worldwide. "
        "They are featured in ancient Chinese zodiac, European folklore, and are known for their intelligence and adaptability."
    ),
    "tiger": (
        "Tigers have been admired and feared throughout history, often representing power and majesty in Asian cultures. "
        "They are the largest wild cats and have inspired countless legends, artworks, and conservation efforts due to their beauty and strength."
    ),
    "wolf": (
        "Wolves have played a central role in human folklore, mythology, and history, symbolizing both danger and loyalty. "
        "From the founding myth of Rome to Native American legends, wolves are known for their pack behavior, intelligence, and adaptability."
    )
}
from flask import Flask, render_template, request, redirect, url_for, send_file
import os
from dotenv import load_dotenv

app = Flask(__name__)
app.secret_key = os.environ.get('FLASK_SECRET_KEY', 'your_default_secret_key_here')

# Load environment variables
load_dotenv()

UPLOAD_FOLDER = 'uploads'
os.makedirs(UPLOAD_FOLDER, exist_ok=True)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

@app.route('/')
def index():
    return render_template('index.html')


# Image upload and inference route
from werkzeug.utils import secure_filename
from PIL import Image
import io


# Combined upload route for image or video
@app.route('/upload', methods=['POST'])
def upload():
    image_file = request.files.get('image')
    video_file = request.files.get('video')
    if (not image_file or image_file.filename == '') and (not video_file or video_file.filename == ''):
        return redirect(url_for('index'))

    # Get threshold parameter from form
    try:
        threshold = float(request.form.get('threshold', 0.5))
    except Exception:
        threshold = 0.5
    model_choice = request.form.get('model', 'roboflow')
    show_descriptions = request.form.get('show_descriptions') == 'on'
    detected_classes = set()

    if image_file and image_file.filename != '':
        filename = secure_filename(image_file.filename)
        filepath = os.path.join(app.config['UPLOAD_FOLDER'], filename)
        image_file.save(filepath)
        # ...existing image detection code...
        if model_choice == 'roboflow':
            from inference import get_model
            import supervision as sv
            model = get_model(model_id="animal-detection-ioduj/2")
            image = Image.open(filepath).convert("RGB")
            results = model.infer(image)[0]
            detections = sv.Detections.from_inference(results)
            mask = detections.confidence >= threshold
            detections = detections[mask]
            box_annotator = sv.BoxAnnotator()
            label_annotator = sv.LabelAnnotator()
            annotated_image = box_annotator.annotate(scene=image, detections=detections)
            annotated_image = label_annotator.annotate(scene=annotated_image, detections=detections)
            annotated_path = os.path.join(app.config['UPLOAD_FOLDER'], f"annotated_{filename}.png")
            annotated_image.save(annotated_path)
            num_detections = len(detections)
            avg_confidence = float(detections.confidence.mean()) if num_detections > 0 else 0.0
            metrics = {
                'Number of Detections': num_detections,
                'Average Confidence': f"{avg_confidence:.2f}"
            }
            if num_detections > 0:
                for label in detections.class_id:
                    class_name = model.classes[label] if hasattr(model, 'classes') else str(label)
                    class_name_norm = class_name.strip().lower()
                    detected_classes.add(class_name_norm)
        else:
            from ultralytics import YOLO
            import numpy as np
            image = Image.open(filepath).convert("RGB")
            model_path = os.path.join('output_yolo_labeled', 'yolov8_exp', 'weights', 'best.pt')
            model = YOLO(model_path)
            results = model.predict(image, conf=threshold, save=False, verbose=False)[0]
            annotated_image = np.array(image).copy()
            for box, score, cls in zip(results.boxes.xyxy.cpu().numpy(), results.boxes.conf.cpu().numpy(), results.boxes.cls.cpu().numpy()):
                if score >= threshold:
                    x1, y1, x2, y2 = map(int, box)
                    label = model.names[int(cls)]
                    label_norm = label.strip().lower()
                    detected_classes.add(label_norm)
                    import cv2
                    cv2.rectangle(annotated_image, (x1, y1), (x2, y2), (0,255,0), 2)
                    cv2.putText(annotated_image, f'{label} {score:.2f}', (x1, y1-10), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0,255,0), 2)
            annotated_image = Image.fromarray(annotated_image)
            annotated_path = os.path.join(app.config['UPLOAD_FOLDER'], f"annotated_{filename}.png")
            annotated_image.save(annotated_path)
            num_detections = len(results.boxes)
            avg_confidence = float(np.mean(results.boxes.conf.cpu().numpy())) if num_detections > 0 else 0.0
            metrics = {
                'Number of Detections': num_detections,
                'Average Confidence': f"{avg_confidence:.2f}"
            }
        descriptions = None
        if show_descriptions and detected_classes:
            descriptions = {cls.capitalize(): CLASS_DESCRIPTIONS.get(cls, "No description available.") for cls in detected_classes}
        return render_template(
            'index.html',
            result_url=url_for('uploaded_file', filename=os.path.basename(annotated_path)),
            metrics=metrics,
            descriptions=descriptions
        )

    # Video upload and detection
    if video_file and video_file.filename != '':
        filename = secure_filename(video_file.filename)
        filepath = os.path.join(app.config['UPLOAD_FOLDER'], filename)
        video_file.save(filepath)
        # Start streaming, then after streaming, redirect to results
        return render_template(
            'index.html',
            video_stream=True,
            video_path=filepath,
            threshold=threshold,
            metrics={'Streaming': 'In progress...'},
            descriptions=None,
            show_video_results=True,
            video_filename=filename,
            video_threshold=threshold
        )

# Video results route (metrics after streaming)
@app.route('/video_results')
def video_results():
    video_path = request.args.get('video_path')
    threshold = float(request.args.get('threshold', 0.5))
    from ultralytics import YOLO
    import cv2
    import numpy as np
    model_path = os.path.join('output_yolo_labeled', 'yolov8_exp', 'weights', 'best.pt')
    model = YOLO(model_path)
    cap = cv2.VideoCapture(video_path)
    frame_count = 0
    detected_classes = set()
    while True:
        ret, frame = cap.read()
        if not ret:
            break
        results = model.predict(frame, conf=threshold, save=False, verbose=False)[0]
        for box, score, cls in zip(results.boxes.xyxy.cpu().numpy(), results.boxes.conf.cpu().numpy(), results.boxes.cls.cpu().numpy()):
            if score >= threshold:
                label = model.names[int(cls)]
                label_norm = label.strip().lower()
                detected_classes.add(label_norm)
        frame_count += 1
    cap.release()
    metrics = {
        'Frames Processed': frame_count,
        'Unique Classes Detected': ', '.join([cls.capitalize() for cls in detected_classes])
    }
    descriptions = None
    if detected_classes:
        descriptions = {cls.capitalize(): CLASS_DESCRIPTIONS.get(cls, "No description available.") for cls in detected_classes}
    return render_template(
        'index.html',
        metrics=metrics,
        descriptions=descriptions,
        video_results_done=True
    )

# Video streaming route
from flask import Response, request
import cv2
from ultralytics import YOLO
import numpy as np
def gen_frames(video_path, threshold):
    model_path = os.path.join('output_yolo_labeled', 'yolov8_exp', 'weights', 'best.pt')
    model = YOLO(model_path)
    cap = cv2.VideoCapture(video_path)
    while True:
        ret, frame = cap.read()
        if not ret:
            break
        results = model.predict(frame, conf=threshold, save=False, verbose=False)[0]
        for box, score, cls in zip(results.boxes.xyxy.cpu().numpy(), results.boxes.conf.cpu().numpy(), results.boxes.cls.cpu().numpy()):
            if score >= threshold:
                x1, y1, x2, y2 = map(int, box)
                label = model.names[int(cls)]
                cv2.rectangle(frame, (x1, y1), (x2, y2), (0,255,0), 2)
                cv2.putText(frame, f'{label} {score:.2f}', (x1, y1-10), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0,255,0), 2)
        ret2, buffer = cv2.imencode('.jpg', frame)
        frame = buffer.tobytes()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')
    cap.release()

@app.route('/video_feed')
def video_feed():
    video_path = request.args.get('video_path')
    threshold = float(request.args.get('threshold', 0.5))
    return Response(gen_frames(video_path, threshold), mimetype='multipart/x-mixed-replace; boundary=frame')


# Serve uploaded/annotated files
@app.route('/uploads/<filename>')
def uploaded_file(filename):
    return send_file(os.path.join(app.config['UPLOAD_FOLDER'], filename))

if __name__ == '__main__':
    app.run(debug=True)
