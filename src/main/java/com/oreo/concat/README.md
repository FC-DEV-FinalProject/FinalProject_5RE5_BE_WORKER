

### 기능 요구사항

- 검증된 오디오 요청 정보롤 파라미터로 받는다.
- 오디오 요청 정보로 오디오 로딩
- 로드 된 오디오를 병합
- 병합된 오디오를 S3에 저장
- S3 url 리턴

### 부가적 요구사항

- 병합 1회의 결과물은 300MB + @ 로 제한
- 300MB 이상 병합을 요청하는 경우 n개로 나누어 병합의 결과를 생성
- 






## 람다 환경변수
- AWS_S3_ACCESSKEY
- AWS_S3_BUKET_NAME
- AWS_S3_REGION
- AWS_S3_SECRETKEY
- MYSQL_HOST
- MYSQL_PASSWORD
- MYSQL_PORT
- MYSQL_USER