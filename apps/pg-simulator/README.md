## PG-Simulator (PaymentGateway)

### Description
Loopback BE 과정을 위해 PaymentGateway 를 시뮬레이션하는 App Module 입니다.
`local` 프로필로 실행 권장하며, 커머스 서비스와의 동시 실행을 위해 서버 포트가 조정되어 있습니다.
- server port : 8082
- actuator port : 8083

### Getting Started
부트 서버를 아래 명령어 혹은 `intelliJ` 통해 실행해주세요.
```shell
./gradlew :apps:pg-simulator:bootRun
```

API 는 아래와 같이 주어지니, 커머스 서비스와 동시에 실행시킨 후 진행해주시면 됩니다.
- 결제 요청 API
- 결제 정보 확인 `by transactionKey`
- 결제 정보 목록 조회 `by orderId`

```http request
### 결제 요청
POST {{pg-simulator}}/api/v1/payments
X-USER-ID: 135135
Content-Type: application/json

{
  "orderId": "1351039135",
  "cardType": "SAMSUNG",
  "cardNo": "1234-5678-9814-1451",
  "amount" : "5000",
  "callbackUrl": "http://localhost:8080/api/v1/examples/callback"
}

### 결제 정보 확인
GET {{pg-simulator}}/api/v1/payments/20250816:TR:9577c5
X-USER-ID: 135135

### 주문에 엮인 결제 정보 조회
GET {{pg-simulator}}/api/v1/payments?orderId=1351039135
X-USER-ID: 135135

```

```
PG API
[POST /payments]
200
ApiResponse[meta=Metadata[result=SUCCESS, errorCode=null, message=null], data=Response[transactionKey=20250826:TR:332386, status=PENDING, reason=null]]
400
feign.FeignException$BadRequest
[400] during [POST] to [http://localhost:8082/api/v1/payments] [PgSimulatorCommandClient#requestPayment(Request,String)]: [{“meta”:{“result”:“FAIL”,“errorCode”:“Bad Request”,“message”:“유저 ID 헤더는 필수입니다.“}}]
[400] during [POST] to [http://localhost:8082/api/v1/payments] [PgSimulatorCommandClient#requestPayment(Request,String)]: [{“meta”:{“result”:“FAIL”,“errorCode”:“Bad Request”,“message”:“필드 ‘cardType’의 값 ‘SAM’이(가) 예상 타입(CardTypeDto)과 일치하지 않습니다. 사용 가능한 값 : [SAMSUNG, KB, HYUNDAI]“}}]
[400] during [POST] to [http://localhost:8082/api/v1/payments] [PgSimulatorCommandClient#requestPayment(Request,String)]: [{“meta”:{“result”:“FAIL”,“errorCode”:“Bad Request”,“message”:“주문 ID는 6자리 이상 문자열이어야 합니다.“}}]
[400] during [POST] to [http://localhost:8082/api/v1/payments] [PgSimulatorCommandClient#requestPayment(Request,String)]: [{“meta”:{“result”:“FAIL”,“errorCode”:“Bad Request”,“message”:“카드 번호는 xxxx-xxxx-xxxx-xxxx 형식이어야 합니다.“}}]
500
feign.FeignException$InternalServerError
[500] during [POST] to [http://localhost:8082/api/v1/payments] [PgSimulatorCommandClient#requestPayment(Request,String)]: [{“meta”:{“result”:“FAIL”,“errorCode”:“Internal Server Error”,“message”:“현재 서버가 불안정합니다. 잠시 후 다시 시도해주세요.“}}]
그외
feign.RetryableException → feign에서 wrap해서 던지는 예외로 getCause() 메서드 호출해보면 아래 예외를 확인 가능합니다.
java.net.ConnectException
java.net.SocketTimeoutException
[Callback]
정상 승인
Payment Callback Received: Request[transactionKey=20250826:TR:324484, orderId=123456, cardType=SAMSUNG, cardNo=1234-5678-9012-3456, amount=1000, status=SUCCESS, reason=정상 승인되었습니다.]
한도 초과
Payment Callback Received: Request[transactionKey=20250826:TR:2a5592, orderId=123456, cardType=SAMSUNG, cardNo=1234-5678-9012-3456, amount=1000, status=FAILED, reason=한도초과입니다. 다른 카드를 선택해주세요.]
잘못된 카드
Payment Callback Received: Request[transactionKey=20250826:TR:6c74ca, orderId=123456, cardType=SAMSUNG, cardNo=1234-5678-9012-3456, amount=1000, status=FAILED, reason=잘못된 카드입니다. 다른 카드를 선택해주세요.]
```