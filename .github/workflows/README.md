# GitHub Actions Workflows

This directory contains CI/CD pipelines for the Animal Detection project.

## Available Workflows

### 1. 🤖 MLOps Pipeline (`mlops-pipeline.yml`)
Trains and tracks ML models with MLflow.

**Triggers:**
- Push to `main`, `develop`, `feature/mlops` branches
- Changes in `data/`, `mlops/`, or training scripts
- Manual trigger with custom parameters

**Features:**
- Trains YOLOv8 model
- Tracks experiments with MLflow
- Validates model performance
- Uploads model artifacts
- Comments PR with results

**Manual Run:**
```bash
# GitHub Actions UI > Actions > MLOps Pipeline > Run workflow
# Parameters: epochs, batch_size
```

### 2. 🏗️ Backend CI (`backend-ci.yml`)
Builds and tests Spring Boot backend.

**Triggers:**
- Push/PR to `main`, `develop`
- Changes in `backend/`

**Features:**
- Sets up PostgreSQL test database
- Builds with Maven
- Runs unit tests
- Generates test reports
- Uploads JAR artifact

### 3. 📊 DVC Pipeline (`dvc-pipeline.yml`)
Manages data versioning and ML pipelines.

**Triggers:**
- Push to `main`
- Changes in `mlops/dvc.yaml` or `mlops/params.yaml`
- Manual trigger

**Features:**
- Runs DVC pipeline (`dvc repro`)
- Shows metrics
- Commits DVC changes
- Pushes data to remote (if configured)

### 4. 🔄 Complete CI/CD (`ci-cd-pipeline.yml`)
Full pipeline with all stages.

**Stages:**
1. **Lint & Format**: Code quality checks
2. **Backend Build**: Compile Spring Boot app
3. **ML Training**: Train YOLOv8 model
4. **Model Registry**: Register models in MLflow
5. **Integration Tests**: End-to-end testing
6. **Deployment Notification**: Success notification

## Workflow Visualization

```
┌─────────────────────────────────────────────────────────────┐
│                      Git Push / PR                          │
└──────────────────────┬──────────────────────────────────────┘
                       │
         ┌─────────────┴─────────────┐
         │                           │
    ┌────▼────┐                 ┌────▼────┐
    │  Lint   │                 │  Build  │
    │ & Format│                 │ Backend │
    └────┬────┘                 └────┬────┘
         │                           │
         └──────────┬────────────────┘
                    │
         ┌──────────┴──────────┐
         │                     │
    ┌────▼────┐           ┌────▼────┐
    │   ML    │           │ Backend │
    │Training │           │  Tests  │
    └────┬────┘           └────┬────┘
         │                     │
         │    ┌────────────────┘
         │    │
    ┌────▼────▼────┐
    │ Integration  │
    │    Tests     │
    └────┬─────────┘
         │
    ┌────▼────┐
    │ Deploy  │
    └─────────┘
```

## Secrets Configuration

Required GitHub Secrets:
- `GITHUB_TOKEN` (automatically provided)

Optional Secrets (for advanced features):
- `DVC_REMOTE_URL` - DVC remote storage URL
- `GDRIVE_CREDENTIALS` - Google Drive credentials for DVC
- `MLFLOW_TRACKING_URI` - Remote MLflow server (if using)
- `SLACK_WEBHOOK` - Slack notifications
- `DOCKER_USERNAME` - Docker Hub username
- `DOCKER_PASSWORD` - Docker Hub password

## Setup Instructions

1. **Enable GitHub Actions**:
   - Repository Settings > Actions > General
   - Allow all actions

2. **Configure DVC Remote** (optional):
   ```yaml
   # In dvc-pipeline.yml, uncomment and configure:
   dvc remote add -d myremote gdrive://your-folder-id
   ```

3. **Add Secrets**:
   - Repository Settings > Secrets and variables > Actions
   - Add required secrets

4. **Test Workflows**:
   ```bash
   git add .github/
   git commit -m "Add GitHub Actions workflows"
   git push
   ```

## Usage Examples

### Run MLOps Pipeline Manually
1. Go to **Actions** tab
2. Select **MLOps - Train and Track Model**
3. Click **Run workflow**
4. Set parameters (epochs, batch_size)
5. Click **Run workflow** button

### Monitor Training Progress
1. Check **Actions** tab for workflow status
2. View logs for each step
3. Download artifacts after completion
4. Access MLflow UI locally to see tracked experiments

### View Test Results
1. Click on workflow run
2. Go to **Backend Test Results** section
3. View detailed test reports

## Troubleshooting

**Workflow fails at training step?**
- Check if dataset is accessible
- Verify DVC remote configuration
- Check GPU/memory requirements

**Backend tests failing?**
- Verify PostgreSQL service is healthy
- Check application.properties configuration
- Review test logs for specific errors

**DVC pipeline errors?**
- Ensure `dvc.yaml` syntax is correct
- Verify all dependencies in `params.yaml`
- Check DVC remote credentials

## Next Steps

- [ ] Set up remote MLflow tracking server
- [ ] Configure DVC remote storage (S3, GDrive, Azure)
- [ ] Add deployment to production (Docker, Kubernetes)
- [ ] Set up monitoring and alerting
- [ ] Add model serving workflow
- [ ] Implement A/B testing pipeline
