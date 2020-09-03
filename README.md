Kubernetes HPA Experiment
===

Kubernetes の Horizontal Pod Autoscaler のいろいろな実験を行うリポジトリ

実験環境のデプロイ
---

### Nginx Ingress Controller

```bash
$ kubectl create -f ingress-nginx-namespace.yaml

$ helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx

$ helm install ingress-nginx ingress-nginx/ingress-nginx \
    --version 2.15.0 \
    --namespace ingress-nginx \
    --values ingress-nginx/overriding-chart-values.yaml
```

### Prometheus

```
$ kustomize build ./prometheus/ | kubectl apply -f -
```

### サンプルアプリケーションのデプロイ

```
$ kubectl apply -f ./cowweb/
```

### Kube Metrics Adapter

```
$ kustomize build ./kube-metrics-adapter/ | kubectl apply -f -
```
