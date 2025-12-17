# 1️⃣ Install dependencies
!pip install -U torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cu126
!pip install pyyaml==6.0
!apt-get install -y cmake
!pip install -U 'git+https://github.com/facebookresearch/fvcore'
!pip install -U iopath termcolor

# 2️⃣ Build Detectron2 from source (works with PyTorch 2.9+ / CUDA 12.6)
!pip install -U 'git+https://github.com/facebookresearch/detectron2.git'




