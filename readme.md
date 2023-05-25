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

- Docker Engine & Docker Compose & NGINX
- Docker Container
- Kubernetes
- Argo CD
- Gitlab Runner
- IaC(Terraform)

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

### 회원가입/로그인

<p align="center">
	<img src="./readmeasset/01_signup.jpg" width="270px">
	<img src="./readmeasset/02_login.jpg" width="270px">
</p>

### 지도

<p align="center">
	<img src="./readmeasset/03_map_01.jpg" width="270px">
	<img src="./readmeasset/04_map_02.jpg" width="270px">
	<img src="./readmeasset/05_map_03.jpg" width="270px">
</p>

### 친구 목록

<p align="center">
	<img src="./readmeasset/friend_01.png" width="270px">
	<img src="./readmeasset/friend_02.png" width="270px">
	<img src="./readmeasset/friend_03.png" width="270px">
</p>

### 피드 작성

<p align="center">
	<img src="./readmeasset/feedwrite_01.png" width="270px">
	<img src="./readmeasset/feedwrite_03.png" width="270px">
	<img src="./readmeasset/feedwrite_04.png" width="270px">
</p>

### 피드 상세

<p align="center">
	<img src="./readmeasset/profile_04.png" width="270px">
	<img src="./readmeasset/profile_05.png" width="270px">
</p>

### 채팅

### 프로필

<p align="center">
	<img src="./readmeasset/profile_01.png" width="270px">
	<img src="./readmeasset/profile_02.png" width="270px">
	<img src="./readmeasset/profile_03.png" width="270px">
</p>

## 5️⃣ 팀원 소개

|     이름      | [정현석](https://github.com/HyunseokCheong) | [이진욱](https://github.com/ssafyer) | [송기라](https://github.com/Songkira) | [유덕균](https://github.com/dokkyunYU) | [류원창](https://github.com/ryuwc) | [김태훈](https://github.com/IT-magician) |
| :-----------: | :-----------------------------------------: | :----------------------------------: | :-----------------------------------: | :------------------------------------: | :--------------------------------: | :--------------------------------------: |
|    포지션     |       Team Leader<br/>Front-end<br/>        |              Front-end               |               Front-end               |                Back-end                |              Back-end              |                  Infra                   |
| 담당<br/>기능 |             Android <br/> 지도              |    Android <br/> 친구 <br/> 채팅     |    Android <br/> 피드 <br/> 프로필    |         Spring <br/> 소켓 서버         |       Spring <br/> API 서버        |          Kubernetes <br/> CI/CD          |

<p align="center">
	<img src="./readmeasset/team.jpg" width="800px">
</p>
