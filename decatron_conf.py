from detectron2.engine import DefaultTrainer
from detectron2.config import get_cfg
from detectron2 import model_zoo
import os

dataset_path = "data"

from detectron2.data.datasets import register_coco_instances

register_coco_instances("animal_train", {}, f"{dataset_path}/train/_annotations.coco.json", f"{dataset_path}/train")
register_coco_instances("animal_val", {}, f"{dataset_path}/valid/_annotations.coco.json", f"{dataset_path}/valid")

cfg = get_cfg()
cfg.merge_from_file(model_zoo.get_config_file("COCO-Detection/faster_rcnn_R_50_FPN_3x.yaml"))
cfg.DATASETS.TRAIN = ("animal_train",)
cfg.DATASETS.TEST = ("animal_val",)
cfg.DATALOADER.NUM_WORKERS = 2
cfg.MODEL.WEIGHTS = model_zoo.get_checkpoint_url("COCO-Detection/faster_rcnn_R_50_FPN_3x.yaml")
cfg.SOLVER.IMS_PER_BATCH = 2
cfg.SOLVER.BASE_LR = 0.00025
cfg.SOLVER.MAX_ITER = 500  # Adjust depending on dataset size
cfg.MODEL.ROI_HEADS.BATCH_SIZE_PER_IMAGE = 128

# Number of classes = number of categories in your dataset
import json
with open(f"{dataset_path}/train/_annotations.coco.json") as f:
    categories = json.load(f)["categories"]
cfg.MODEL.ROI_HEADS.NUM_CLASSES = len(categories)

# Set output folder in Drive
output_dir = "/content/drive/MyDrive/Projet_integre_2BA8/output"
os.makedirs(output_dir, exist_ok=True)
cfg.OUTPUT_DIR = output_dir