Kubernetes HPA Experiment
===

Kubernetes の Horizontal Pod Autoscaler のいろいろな実験を行うリポジトリ

Nginx Ingress Controller とその監視を行う Prometheus のデプロイ
---

```bash
$ kubectl create -f ingress-nginx-namespace.yaml

$ helm install ingress-nginx ingress-nginx/ingress-nginx \
    --version 2.15.0 \
    --namespace ingress-nginx \
    --values ingress-nginx/overriding-chart-values.yaml

$ kubectl apply --kustomize github.com/kubernetes/ingress-nginx/deploy/prometheus/

# TODO: deploy
```

アプリケーション（HPAリソース含む）のデプロイ
---

```
sed -i -e "s/DOCKER_CONFIG_JSON/[Your Docker Config JSON]"

kubectl apply -f cowweb/
```
