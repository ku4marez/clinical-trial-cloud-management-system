# Scripts for CI/CD Automation

## 1️⃣ Setup Local Environment
```bash
./scripts/setup-local-env.sh

./scripts/docker-build.sh

./scripts/deploy-k8s.sh

./scripts/jenkins-build.sh


---

|----------|-----------|-----------------|
| `setup-local-env.sh` | Sets up local env | Developer |
| `docker-build.sh` | Builds & pushes Docker image | Developer or Jenkins |
| `deploy-k8s.sh` | Deploys app to K8s | Developer or Jenkins |
| `jenkins-build.sh` | Runs CI/CD pipeline | Jenkins |

---

✅ Add these scripts to your `scripts/` directory.  
✅ Ensure they are **executable** (`chmod +x <script>.sh`).  
✅ Update `Jenkinsfile` to use `jenkins-build.sh`.  
✅ Run `kubectl` to deploy Kubernetes manifests.  

