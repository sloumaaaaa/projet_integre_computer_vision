from flask import Flask, render_template, request, redirect, url_for, send_file
import os
from dotenv import load_dotenv

app = Flask(__name__)

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

@app.route('/upload', methods=['POST'])
def upload():
    if 'image' not in request.files:
        return redirect(url_for('index'))
    file = request.files['image']
    if file.filename == '':
        return redirect(url_for('index'))
    filename = secure_filename(file.filename)
    filepath = os.path.join(app.config['UPLOAD_FOLDER'], filename)
    file.save(filepath)


    # Get threshold parameter from form
    try:
        threshold = float(request.form.get('threshold', 0.5))
    except Exception:
        threshold = 0.5

    # --- Inference and annotation ---
    from inference import get_model
    import supervision as sv
    model = get_model(model_id="animal-detection-ioduj/2")
    image = Image.open(filepath).convert("RGB")
    results = model.infer(image)[0]
    detections = sv.Detections.from_inference(results)

    # Filter detections by threshold
    mask = detections.confidence >= threshold
    detections = detections[mask]

    box_annotator = sv.BoxAnnotator()
    label_annotator = sv.LabelAnnotator()
    annotated_image = box_annotator.annotate(scene=image, detections=detections)
    annotated_image = label_annotator.annotate(scene=annotated_image, detections=detections)

    # Save annotated image
    annotated_path = os.path.join(app.config['UPLOAD_FOLDER'], f"annotated_{filename}.png")
    annotated_image.save(annotated_path)

    # Basic evaluation metrics
    num_detections = len(detections)
    avg_confidence = float(detections.confidence.mean()) if num_detections > 0 else 0.0
    metrics = {
        'Number of Detections': num_detections,
        'Average Confidence': f"{avg_confidence:.2f}"
    }

    return render_template('index.html', result_url=url_for('uploaded_file', filename=os.path.basename(annotated_path)), metrics=metrics)


# Serve uploaded/annotated files
@app.route('/uploads/<filename>')
def uploaded_file(filename):
    return send_file(os.path.join(app.config['UPLOAD_FOLDER'], filename))

if __name__ == '__main__':
    app.run(debug=True)
