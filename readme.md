# 친구 위치 조회 SNS, Vuddy

## 목차

- [개요](#개요)
- [설계](#설계)
- [기능](#기능)
- [Android](#Android)
- [Backend](#Backend)
- [Infra](#Infra)

## 개요

### 서비스 개요

- Vuddy: 안드로이드 기반의 친구 위치 조회 SNS
- 기회의도: 친구의 현재 위치를 파악하고, 추억을 지도에 남길 수 있는 SNS 개발

### 핵심 기능

- 자신의 현재 위치를 조회하여 친구들에게 전송
- 전송받은 위치를 나의 지도에 표시
- 친구와 채팅 기능
- 피드를 작성하고, 지도에 해당 위치에 피드를 표시

## 설계

### 기술스택

#### 안드로이드

- Android
- Kotlin

#### 스프링

- Spring Boot
- Java
- Kafka
- Redis
- MySQL

#### 인프라

- Docker
- Kubernetes
- Argo

### 서비스 아키텍쳐

<p align="center">
	<img src="./readmeasset/service_architecture.jpg" width="800px">
</p>

### Figma [:link:](https://www.figma.com/file/zxIbmYm4JoglF6kdQL3J2q/B305?type=design&node-id=0-1&t=G1MX4301YSdhDi3q-0)

<p align="center">
	<img src="./readmeasset/figma.png" width="800px">
</p>

### ERD

<p align="center">
	<img src="./readmeasset/erd.png" width="800px">
</p>

### API 명세서 [:link:](https://documenter.getpostman.com/view/23901534/2s93eR5vmA)

<p align="center">
	<img src="./readmeasset/api.png" width="800px">
</p>

## 기능

- **** 기능별 동작 화면 추가 예정

<!-- <p align="center">
	<img src="./readmeasset/api.png" width="800px">
</p>

<p align="center">
	<img src="./readmeasset/api.png" width="270px">
	<img src="./readmeasset/api.png" width="270px">
	<img src="./readmeasset/api.png" width="270px">
</p> -->

## Android

- [Android](./client/)

## Backend

- [Backend](./server/)

## Infra

- [Infra](./exec/helm-chart/)