# 포팅 메뉴얼(백엔드 파트)
## 실행방법
```sh
docker compose --env-file environment/.env -f dataserver-docker-compose.yml up -d

### MySQL 실행이 완료될때까지 3분 정도 대기

docker compose --env-file environment/.env -f aps-docker-compose.yml up -d
```
## 설정변경 방법
---
: environtment/.env의 내용을 수정 후에 명령어를 실행한다.  
(단, 자동으로 생성된 'mysql 디렉토리'를 삭제해야 DB설정이 재반영된다.)
> ### 설정변수
> >1) MYSQL_URL : MySQL JDBC URL을 의미한다
> >2) MYSQL_USERNAME : MySQL의 새로운 관리자 계정명을 의미한다.
> >3) MYSQL_PASSWORD : MySQL의 비밀번호(root, 새로운 관리자 계정)을 의미한다.
> >4) REDIS_HOST_NAME: Redis의 주소르 의미한다.
> >5) REDIS_PORT : Redis의 포트번호를 의미한다.
> >6) REDIS_PASSWORD : Redis의 비밀번호를 의미한다.
> >7) SERVER_HOSTNAME : Backend 서비스들이 모여있는 서버의 주소(IPv4) 혹은 도메인명을 의미한다.
> >8) KAFKA_HOST_NAME : Kafka의 호스트 네임을 의미한다.
## 사전요구사항
1. docker
2. docker composey