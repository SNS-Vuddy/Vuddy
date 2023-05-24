# 👭 지도를 통해 소통하는 위치기반 SNS 🚩 - Vuddy

## 목차

- [개요](#개요)
- [설계](#설계)
- [기술특장점](#기술특장점)
- [기능](#기능)
- [팀원 소개](#팀원-소개)


## 1️⃣ 개요

### ⌛ 프로젝트 기간 및 인원
- 기간: 2023.04.10 ~ 2023.05.18 (6주)
- 인원(6명) : 정현석, 이진욱, 송기라, 류원창, 유덕균, 김태훈


### 🎞 [UCC](https://www.youtube.com/watch?v=N_vFnSwea6M)

<br />

### 🔎 서비스 개요

- Vuddy: 안드로이드 기반의 친구 위치 조회 SNS
- 기회의도: 친구의 현재 위치를 파악하고, 추억을 지도에 남길 수 있는 SNS 개발
- 

### 🛠 핵심 기능

- 자신의 현재 위치를 조회하여 친구들에게 전송
- 전송받은 위치를 나의 지도에 표시
- 현재 위치를 중심으로 내 상태 파악(회사, 집, 출근중, 등..)
- 친구와 채팅 기능
- 피드를 작성하고, 지도에 해당 위치에 피드를 표시

<br />

## 2️⃣ 설계

### 🔧 기술스택

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

### 🧫 서비스 아키텍쳐

<p align="center">
	<img src="./readmeasset/service_architecture.jpg" width="800px">
</p>

<p align="center">
	<img src="./readmeasset/03.PNG" width="800px">
</p>

### 💻 [Figma](https://www.figma.com/file/zxIbmYm4JoglF6kdQL3J2q/B305?type=design&node-id=0-1&t=G1MX4301YSdhDi3q-0)

<p align="center">
	<img src="./readmeasset/figma.png" width="800px">
</p>

### 🕶 ERD

<p align="center">
	<img src="./readmeasset/erd.png" width="800px">
</p>

### [API 명세서](https://documenter.getpostman.com/view/23901534/2s93eR5vmA)

<p align="center">
	<img src="./readmeasset/api.png" width="800px">
</p>

<br />

## 3️⃣ 기술 특장점

### 사용자 위치 조회

<p align="center">
	<img src="./readmeasset/01.jpg" width="800px">
</p>

- 안정적이고 정확한 위치 조회 가능
- 배터리 소모 최소화


### 사용자 상태 파악

<p align="center">
	<img src="./readmeasset/02.jpg" width="800px">
</p>

- DBSCAN 알고리즘을 이용하여 사용자의 위치를 토대로 상태를 파악

<br />

## 4️⃣ 기능

- **** 기능별 동작 화면 추가 예정

<!-- <p align="center">
	<img src="./readmeasset/api.png" width="800px">
</p>

<p align="center">
	<img src="./readmeasset/api.png" width="270px">
	<img src="./readmeasset/api.png" width="270px">
	<img src="./readmeasset/api.png" width="270px">
</p> -->

<br />

## 5️⃣ 팀원 소개

|   이름   |     [강동관](https://github.com/whatistheMatter823)         |     [정윤해](https://github.com/JEONGYOONHAE)     |     [장수영](https://github.com/dearsyjang)     |     [배근혜](https://github.com/hyehye66)     |     [이현정](https://github.com/Hyunbird)     |     [최혁주](https://github.com/spaceforvincent)     |
| :------------: | :----------: | :----------: | :----------: | :----------: | :----------: | :----------: |
|  포지션  |                 Team Leader<br/>Data<br/>CI/CD                 |         Back-end<br/>CI/CD          |            Front-end<br/>UI/UX            | Front-end<br/>UI/UX | Front-end<br/>UI/UX | Front-end<br/>UI/UX |
| 담당<br/>기능 | 데이터 처리<br/>Spring Boot<br> Dockerize & 배포 | Spring Boot <br/>Dockerize & 배포 | 회원<br/>산 지도 & 정보<br/>등산 챌린지 |       채팅        |     등산 기록     |     등산 달력     |
