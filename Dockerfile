FROM openjdk:11-jre-slim
WORKDIR /usr/src/app

ENV JAVA_OPTS="-Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true"


# 외부에서 해주어야하는 작업 (gradle dependecy 재설치 안하려고 함)
# RUN chmod +x gradlew 
# RUN sudo ./gradlew clean bootJar -x test 

ARG JAR_FILE=./build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
CMD ["java","$JAVA_OPTS","-jar","app.jar"]
