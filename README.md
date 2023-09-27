# Dividend
Jsoup을 사용해 스크래핑한 Yahoo Finance 페이지의 주식 배당금을 활용한 백엔드 서비스

## 개발 환경
- Spring boot: 2.5.6
- Java: 11
- IDEA: IntelliJ
- Database: H2 Database 인메모리

---

## 사용 기술
- Jsoup을 활용한 웹 스크래핑
- JWT를 사용한 회원 관리
- Redis를 사용해 데이터를 캐싱
- Slf4j + Logback을 이용해 로그처리

## 구현 API
1. GET - finance/dividend/{companyName}
    - 회사 이름을 input으로 받아서 해당 회사의 메타 정보와 배당금 정보를 반환
    - 잘못된 회사명이 입력으로 들어온 경우 400 에러 코드와 에러메시지를 반환한다.
2. GET - company/autocomplete
    - 자동 완성 기능을 위한 API
    - 검색하고자 하는 prefix를 입력으로 받으면 해당 prefix로 검색되는 회사명 리스트 중 10개를 반환한다.
3. GET - company
    - 서비스에서 관리하고 있는 모든 회사 목록을 반환한다.
    - 반환 결과는 Page 인터페이스 형태
4. POST - company
    - 새로운 회사 정보 추가 API
    - 추가하고자 하는 회사의 ticker를 입력으로 받아 해당 회사의 정보를 스크래핑 후 저장
    - 이미 보유하고 있는 회사 정보인 경우 400 에러 코드와 에러메시지를 반환한다.
    - 존재하지 않는 회사 ticker일 경우 400 에러 코드와 에러메시지를 반환한다.
5. DELETE - company/{ticker}
    - ticker에 해당하는 회사 정보 삭제
    - 삭제시 회사의 배당금 정보와 캐시도 모두 삭제되어야 한다.
6. POST - auth/signup
    - 회원가입 API
    - 중복 ID는 허용하지 않는다.
    - 패스워드는 암호화된 형태로 저장되어야 한다.
7. POST - auth/signin
    - 로그인 API
    - 회원가입이 되어있고, 아이디/패스워드 정보가 맞는 경우 JWT를 발급한다.