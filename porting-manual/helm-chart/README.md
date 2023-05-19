# Vuddy 서비스의 Helm Chart (백엔드 부분)
## 실행방법
```sh
export HELM_PROJECT_NAME=<your project setting>
export PROJECT_NAMESPACE=<your project setting>
helm install $HELM_PROJECT_NAME . --namespace $PROJECT_NAMESPACE
```
## 사전요구사항
1. external dns & IAM on AWS Route53 설정