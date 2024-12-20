# icd01-team05_06-commerce-be
<img width="779" alt="image" src="https://github.com/user-attachments/assets/ddf08a52-1a92-40a1-99e9-df6c606b70e5">


- 🚚 개발 기간 (2024.08.17 ~ 2024.11.07)
- 📝 팀 위키페이지 | [팀 위키페이지](https://github.com/FC-InnerCircle/icd01-team05_06-commerce-be/wiki)

## 프로젝트 소개 📝
이너서클 1기 `온라인 쇼핑몰(이너북스)` 프로젝트 개발

## 구성원 👨‍👨‍👧‍👧👩‍👦‍👦
|  [`팀장` 이호준](https://github.com/psh10066)|  [이재훈](https://github.com/hun-cloud)  |  [장현호](https://github.com/hyunolike)  | 
| :----------: |  :--------:  |  :--------:  |  
| <img src="https://avatars.githubusercontent.com/psh10066" width=100px alt="이호준"/>| <img src="https://avatars.githubusercontent.com/hun-cloud" width=100px alt="이재훈"/>  | <img src="https://avatars.githubusercontent.com/hyunolike" width=100px alt="장현호"/> |
|<a href="https://github.com/FC-InnerCircle/icd01-team05_06-commerce-be/commits/main?author=psh10066" title="Code">작업 내용 💻</a>|<a href="https://github.com/FC-InnerCircle/icd01-team05_06-commerce-be/commits/main?author=hun-cloud" title="Code">작업 내용 💻</a>|<a href="https://github.com/FC-InnerCircle/icd01-team05_06-commerce-be/commits/main?author=hyunolike" title="Code">작업 내용 💻</a>|

## 프로젝트 기술스택 💡
### 백엔드 
<img width="700" alt="be stack" src="https://github.com/user-attachments/assets/9c8fe3b2-bd59-4942-a926-97e665ff5455">

### 인프라 
<img width="700" alt="infra stack" src="https://github.com/user-attachments/assets/236f249b-2311-4c8b-b9f6-08883a663f3f">

---

- 버그 제보를 환영합니다. 버그를 발견하신 경우 [GitHub Issue](https://github.com/FC-InnerCircle/icd01-team05_06-commerce-be/issues)에 편하게
  남겨주세요.

<!-- ## 기술 스택

```
Kotlin 1.9.24 & JDK 21
Spring Boot 3.3.2
Gradle 8.8

MySQL
Spring Data JPA
Kotlin JDSL
Redis

JUnit5
RestDocs
```
--> 

## API Request URL
- 전체 Application 공통 : https://76ztyqn6fe.execute-api.ap-northeast-2.amazonaws.com/

## API 문서
- 인증 Application : https://fc-innercircle-icd1.github.io/icd01-team05_06-commerce-be/auth-api-guide.html
- 상품 Application : https://fc-innercircle-icd1.github.io/icd01-team05_06-commerce-be/product-api-guide.html
- 주문 Application : https://fc-innercircle-icd1.github.io/icd01-team05_06-commerce-be/order-api-guide.html

## 로컬 실행 방법

```
실제 프로젝트 운영 시에는 AWS API Gateway를 통해
3개의 Application을 하나의 URL로 구성하도록 사용했습니다.
또한 AWS RDS MySQL과 AWS ElastiCache Redis를 사용했습니다. 

로컬에서도 동일한 동작을 테스트할 수 있도록 docker-compose로
Application 3개를 띄우고 nginx로 Application들을 포워딩합니다.
또한 docker-compose로 MySQL과 Redis를 띄워서 실행하도록 구성했습니다. 
```

1. 로컬 컴퓨터에 JDK 21 설치
2. Docker Daemon 실행 (Docker Desktop 등)
3. 터미널에서 아래 명령어 입력하여 Application Build 및 Docker Container 실행
    ```bash
    make
    ```
4. 아래 URL로 API 요청
    - http://localhost:8080/

- 종료 시 아래 명령어 실행
    ```
    docker-compose down
    ```
