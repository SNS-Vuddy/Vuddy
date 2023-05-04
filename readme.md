### git 컨벤션

# 브랜치명
네이밍 규칙 : ${group_name}/${devlop_type}/${function_name|service_name}
              예 : develop/android/gps, develop/backend/auth-microservice, feature/android/tracking  


    - group name
        * dev : 개발용 브랜치, CI가 개발 환경용 서버(네임스페이스)에 올려주는 브랜치로써 여러개의 feature 브랜치가 하나로 합쳐지는 레포지토리임.
        * feature : commit용 브랜치, 특정 기능이나 서비스를 개발해서 commit을 남기는 브랜치

- 브랜치는 자신의 성(KIM, LEE...)

- type 커밋유형 종류
```
feat : 새로운 기능 추가
fix : 버그 수정
docs : 문서 수정
chore : 그 외 자잘한 작업
test : 테스트 코드
build : 시스템 또는 외부 종속성에 영향을 미치는 변경사항 (npm, gulp, yarn 레벨)
ci : CI관련 설정
style : 코드 의미에 영향을 주지 않는 변경사항 (포맷, 세미콜론 누락, 공백 등)
refactor : 성능 개선
```

- subject 제목
```
- 64자를 넘기지 않는다.
- 마침표를 붙이지 않는다.
- 개조식 구문으로 작성 (ex. feat: 로그 출력 기능 추가)
- 문장형으로 끝나지 않게 작성 (ex. 추가함, 추가하였습니다 X)
```

- 예시
```
feat: 로그인 기능 구현
```
