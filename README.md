Kubernetes HPA Experiment
===

Kubernetes の Horizontal Pod Autoscaler のいろいろな実験を行うリポジトリ

実験環境のデプロイ
---

### Nginx Ingress Controller

```bash
$ kubectl create -f ingress-nginx/ingress-nginx-namespace.yaml

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

負荷試験の実行
---
`gatling/scripts/run.sh` を編集して、アプリケーションのエンドポイントの、ホストパート部分までの文字列を `BASE URL` に記述します。
また、必要に応じて負荷試験用の各種パラメータを変更しておきます。

```
$ vim gatling/scripts/run.sh
```

```diff
- BASE_URL=http://localhost:8080
+ BASE_URL=http://128.128.128.128
```

負荷試験の実行はコンテナ化されたGatringから実行します。

```
$ cd gatling
$ docker-compose up
```

メトリクスの取得
---
負荷試験中のメトリクスの推移を観察するには、 `kubectl get --raw` を使ってAPI Serverから取得します。

### rps@nginx-ingress-controller

```
while true; do date +"%Y/%m/%d %H:%M:%S"; kubectl get --raw "/apis/external.metrics.k8s.io/v1beta1/namespaces/cowweb/prometheus-query?labelSelector=query-name=processed-events-per-second" | jq '.items[].value'; sleep 1; done 
```

### cpu

```
while true; do date +"%Y/%m/%d %H:%M:%S"; kubectl get --raw "/apis/metrics.k8s.io/v1beta1/namespaces/cowweb/pods/" | jq -r '.items[] | .containers[].usage.cpu'; sleep 1; done
```

### memory

```
while true; do date +"%Y/%m/%d %H:%M:%S"; kubectl get --raw "/apis/metrics.k8s.io/v1beta1/namespaces/cowweb/pods/" | jq -r '.items[] | .containers[].usage.memory'; sleep 1; done
```