from dotenv import load_dotenv
load_dotenv()

import os
os.environ["CORE_MODEL_SAM_ENABLED"] = "False"
os.environ["CORE_MODEL_SAM2_ENABLED"] = "False"
os.environ["CORE_MODEL_SAM3_ENABLED"] = "False"
os.environ["CORE_MODEL_GAZE_ENABLED"] = "False"
os.environ["CORE_MODEL_YOLO_WORLD_ENABLED"] = "False"
os.environ["ROBOFLOW_API_KEY"] = os.getenv("ROBOFLOW_API_KEY")

from inference import get_model
import supervision as sv
from PIL import Image

# Load Roboflow model
model = get_model(model_id="animal-detection-ioduj/2")

# ---- Load image via PIL (supports AVIF) ----
image_file = "images_de_test/moms5_4x3.avif"
image = Image.open(image_file).convert("RGB")  # convert ensures 3 channels

# ---- Run inference ----
results = model.infer(image)[0]

# ---- Convert to Supervision detections ----
detections = sv.Detections.from_inference(results)

# ---- Annotators ----
box_annotator = sv.BoxAnnotator()
label_annotator = sv.LabelAnnotator()

# ---- Annotate ----
annotated_image = box_annotator.annotate(scene=image, detections=detections)
annotated_image = label_annotator.annotate(scene=annotated_image, detections=detections)

# ---- Display ----
sv.plot_image(annotated_image)
