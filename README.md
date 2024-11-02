O_o

# interactive_novel_server

Interactive Novel의 백엔드 서버 프로젝트입니다.

- [Interactive Novel 독자 서비스](http://interactive-novel-web.s3-website.kr.object.ncloudstorage.com/)
- [Interactive Novel 작가 서비스](http://interactive-novel-creators-web.s3-website.kr.object.ncloudstorage.com/)

## 실행 절차

### 0. 개발 환경 구성

- JDK 17.x.x
- `git` (Optional)
- MariaDB
- Redis
- Kafka

### 1. 프로젝트 클론

```bash
git clone https://github.com/2023-inha-capstone-team4/interactive_novel_server.git
cd interactive_novel_server
```

### 2. 빌드

```bash
chmod +x gradlew
./gradlew build # Linux / Unix
gradlew build # Windows
```

### 3. 실행

```bash
cd /build/libs
java -jar interactive_novel-0.0.1-SNAPSHOT.jar
```
