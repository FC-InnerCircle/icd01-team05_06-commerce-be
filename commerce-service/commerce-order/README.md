## 주문 목록 조회 API

## 엔드포인트

```
GET http://localhost:8080/order/v1/orders
```

## 요청 파라미터

| 파라미터 | 타입 | 필수   | 설명 |
|----------|------|------|------|
| dateRange | string | 아니오  | 조회 기간 (`LAST_WEEK`, `LAST_MONTH`, `LAST_3_MONTHS`, `LAST_6_MONTHS`, `CUSTOM`) |
| status | string | 아니오  | 주문 상태 (`PENDING`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELLED`, `REFUND`, `EXCHANGE`) |
| sortBy | string | 아니오  | 정렬 옵션 (`RECENT`, `ORDER_STATUS`, `ALL`, 기본값: `RECENT`) |
| page | integer | 아니오  | 페이지 번호 (기본값: 0) |
| size | integer | 아니오  | 페이지 크기 (기본값: 20) |
| orderDate | string | 조건부  | 시작 날짜 (yyyy-MM-dd 형식, `dateRange`가 `CUSTOM`일 때 필수) |
| endDate | string | 조건부  | 종료 날짜 (yyyy-MM-dd 형식, `dateRange`가 `CUSTOM`일 때 필수) |

## 인증

이 API는 인증된 사용자만 접근 가능합니다. 요청 시 유효한 인증 토큰을 포함해야 합니다.

## 응답

### 성공 응답

**상태 코드:** 200 OK

```json
{
  "success": true,
  "data": {
    "products": [
      {
        // 주문 요약 정보
      }
    ],
    "totalElements": 0,
    "totalPages": 0
  }
}
```

### 오류 응답

**상태 코드:** 400 Bad Request

```json
{
  "success": false,
  "error": [
    {
      "code": "string",
      "message": "string"
    }
  ]
}
```

## 요청 예시

### 기본 요청

```
GET http://localhost:8080/order/v1/orders
```

### 커스텀 날짜 범위 요청

```
GET http://localhost:8080/order/v1/orders?dateRange=CUSTOM&orderDate=2024-01-01&endDate=2024-03-31&status=DELIVERED&sortBy=ORDER_STATUS&page=1&size=10
```

## 주의사항

0. 기본 요청은 지난 1주일 동안의 모든 주문을 조회합니다.
1. `dateRange`가 `CUSTOM`일 경우, `orderDate`와 `endDate`는 필수입니다.
2. `page`는 0 이상의 정수여야 합니다.
3. `size`는 양의 정수여야 합니다.
4. `orderDate`는 `endDate`보다 이전이어야 합니다.

## 다양한 요청 예시

0. 기본 요청 (지난 1주일 동안의 모든 주문 조회)
   ```
   GET http://localhost:8080/order/v1/orders
   ```

1. 지난 1주일 동안의 모든 주문 조회
   ```
   GET http://localhost:8080/order/v1/orders?dateRange=LAST_WEEK&sortBy=RECENT
   ```

2. 지난 3개월 동안의 배송 완료된 주문 조회
   ```
   GET http://localhost:8080/order/v1/orders?dateRange=LAST_3_MONTHS&status=DELIVERED&sortBy=RECENT
   ```

3. 지난 6개월 동안의 취소된 주문 조회 (주문 상태별 정렬)
   ```
   GET http://localhost:8080/order/v1/orders?dateRange=LAST_6_MONTHS&status=CANCELLED&sortBy=ORDER_STATUS
   ```

4. 특정 기간 동안의 처리 중인 주문 조회
   ```
   GET http://localhost:8080/order/v1/orders?dateRange=CUSTOM&orderDate=2024-01-01&endDate=2024-06-30&status=PROCESSING
   ```

5. 지난 달의 환불 주문 조회 (2페이지, 페이지당 15개 항목)
   ```
   GET http://localhost:8080/order/v1/orders?dateRange=LAST_MONTH&status=REFUND&page=1&size=15
   ```

6. 모든 주문 상태를 포함한 최근 1개월 주문 조회 (상태별 정렬)
   ```
   GET http://localhost:8080/order/v1/orders?dateRange=LAST_MONTH&sortBy=ORDER_STATUS
   ```

7. 특정 날짜의 모든 주문 조회
   ```
   GET http://localhost:8080/order/v1/orders?dateRange=CUSTOM&orderDate=2024-03-15&endDate=2024-03-15
   ```