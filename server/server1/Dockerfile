
FROM openjdk:17-alpine
WORKDIR /usr/src/app


# 외부에서 해주어야하는 작업 (gradle dependecy 재설치 안하려고 함)
# RUN chmod +x gradlew 
# RUN sudo ./gradlew clean bootJar -x test 

ARG JAR_FILE=./build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
CMD ["java","-jar","app.jar","--spring.datasource.url","jdbc:mysql://k8b305.p.ssafy.io:3306/vuddy?serverTimezone=UTC&characterEncoding=UTF-8&useSSL=true","--spring.datasource.username","$MYSQL_USERNAME","--spring.datasource.password","$MYSQL_PASSWORD"]


