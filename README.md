# icd01-team05_06-commerce-be

## 기술 스택

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

## API Request URL
- 전체 Application 공통 : https://76ztyqn6fe.execute-api.ap-northeast-2.amazonaws.com/

## API 문서
- 인증 Application : https://76ztyqn6fe.execute-api.ap-northeast-2.amazonaws.com/docs/auth-api-guide.html
- 상품 Application : https://76ztyqn6fe.execute-api.ap-northeast-2.amazonaws.com/docs/product-api-guide.html
- 주문 Application : https://76ztyqn6fe.execute-api.ap-northeast-2.amazonaws.com/docs/order-api-guide.html

## 로컬 실행 방법

1. docker daemon 실행
2. 터미널에서 아래 명령어 입력하여 docker container 실행
    ```bash
    make
    ```
3. 아래 URL로 API 요청
    - 인증 Application : http://localhost:8080/
    - 상품 Application : http://localhost:8081/
    - 주문 Application : http://localhost:8082/

- 종료 시 아래 명령어 실행
    ```
    docker-compose down
    ```