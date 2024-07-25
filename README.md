# Spring Security 소셜 로그인 기능 구현하기

---

## **🚀 ERD**
> ** 도메인은 users, onboarding, refresh_token으로 분리하였습니다. **
<img width="1000" alt="스크린샷 2024-07-25 오후 2 55 42" src="https://github.com/user-attachments/assets/b20aeed6-2700-4c02-ab41-ffce5c747ef9">

## **🚀 기능 요구 사항 구현 내용 및 설명**

> 컨트롤러에 /login API가 없어 소셜로그인을 구현하지 않은 것처럼 보일 수 있는데. <br/>클라이언트에서 하이퍼링크로 로그인 링크로 이동만 하면 모두 백엔드에서 소셜로그인 과정을 진행하여 /login API를 이용하지 않습니다.

### 소셜 로그인 과정

> 기본 프론트와 백엔드에서의 역할 책임 분배와 다르게 백엔드에서의 소셜로그인은 code를 통해 소셜 토큰을 받아오는 과정, 토큰을 통해 소셜 유저 정보를 받아오는 과정이 자동으로 이루어집니다.

1. 클라이언트 측에서 "구글 로그인", "네이버 로그인" 과 같은 소셜 로그인 버튼을 누를 시 하이퍼링크로 소셜 로그인 url 링크로 접속 시도하여 소셜 서버에 로그인 페이지를 요청한다.
2. 클라이언트에 로그인 페이지를 반환한다.
3. 로그인 계정을 지정하여 클라이언트 측에서 로그인 시도를 한다.
4. 로그인 성공 시 소셜 서버에서 어플레이케이션 서버에 인증 코드(code)를 반환해준다.
5. 어플리케이션 서버에서 인증코드를 이용하여 소셜 서버에게 AccessToken 발급 요청을 한다.
6. 소셜 서버에서 어플리케이션 서버에 AccessToken을 반환해준다.
7. 어플리케이션 서버에서 AccessToken을 이용하여 CustomOAuth2UserService의 loadUser를 통하여 소셜 User정보를 획득한다.
8. 소셜 User 정보를 정상적으로 받을 시 LoginSuccessHandler를 실행한다.
9. 위의 LoginSuccessHandler에서 JWT토큰을 발급하여 쿠키에 담아 클라이언트에 전달해주고 로그인 처리를 해준다.
   (httpOnly 속성이라 클라이언트 측에선 쿠키 내용을 빼서 쓸 수가 없습니다.(확실친 않음) -> 이후 클라이언트 측에서 쿠키를 가지고 백엔드에 다른 엔드포인트로 get요청을 하여 accessToken받는 과정이 필요)
11. 이후 클라이언트 측에서 발급받은 JWT토큰을 헤더에 넣어 API 통신을 하고, 어플리케이션 서버에서는 받은 JWT토큰을 이용해 권한 인가를 확인한다.

### 기능 구현 사진 설명 (로그인)

1. 클라이언트 측에서 해당 소셜로그인 버튼을 눌러 해당 소셜로그인 창으로 이동한다. (5173포트로 간단하게 만든 클라이언트 페이지입니다.)
<img width="550" alt="스크린샷 2024-07-25 오후 3 49 38" src="https://github.com/user-attachments/assets/506b5954-1517-4343-be90-d9b2e3a7a4cd">
<img width="550" alt="스크린샷 2024-07-25 오후 3 49 55" src="https://github.com/user-attachments/assets/fd4d0490-dc53-4b78-b0da-4f4bec3ca6ee">

2. 구글 소셜로그인 진행 <br/>
   - 처음 로그인을 하여 온보딩을 하지 않은 유저일 경우<br/>
   http://localhost:5173/onboarding?userId=" + userId 로 이동되고 userId와 request(닉네임, 나이, 직업) 을 이용하여 온보딩을 진행한다 (토큰 아직 발급 x, 데이터베이스에 온보딩을 제외한 기본 user 정보만 들어감)<br/>
   - 이미 온보딩까지 진행한 유저일 경우<br/>
   http://localhost:5173으로 바로 이동 (쿠키에 토큰 발급)<br/>
<img width="450" alt="스크린샷 2024-07-25 오후 3 55 24" src="https://github.com/user-attachments/assets/d13777fb-ff2d-4990-adad-008dc579b946"><br/>
- 온보딩을 하지 않은 User -<br/>
<img width="600" alt="스크린샷 2024-07-25 오후 4 04 03" src="https://github.com/user-attachments/assets/313cf6fd-34ac-4fa6-808d-08938a522124"> <br/>
- 온보딩을 한 User - <br/>
<img width="600" alt="스크린샷 2024-07-25 오후 4 14 33" src="https://github.com/user-attachments/assets/8f195a1f-c32a-4a28-80dd-83ce6caea5ec"><br/>
<img width="600" alt="스크린샷 2024-07-25 오후 4 06 36" src="https://github.com/user-attachments/assets/372c2a69-90a8-4375-b459-225892260f8f">

---

### 기능 구현 사진 설명 (온보딩)

1. 포스트맨에서 userId와 request를 이용하여 온보딩을 진행합니다.
<img width="1079" alt="스크린샷 2024-07-25 오후 4 10 44" src="https://github.com/user-attachments/assets/6fdbc9b1-99b2-46e9-bd48-d01ddf6be9ab">

2. 데이터베이스에서 온보딩 정보와, 발급된 리프레쉬 토큰을 확인할 수 있습니다. 
<img width="733" alt="스크린샷 2024-07-25 오후 4 11 59" src="https://github.com/user-attachments/assets/d3894957-dc0a-4051-891f-925269741e00">
<img width="639" alt="스크린샷 2024-07-25 오후 4 12 24" src="https://github.com/user-attachments/assets/2d680b17-7ce2-4181-9f06-c2a3ca76a789">

### 기능 구현 사진 설명 (마이페이지)

1. 마이페이지 조회 - 포스트맨에서 userId와 AccessToken으로 Get요청을 하여 온보딩 정보를 조회할 수 있습니다. (어세스토큰 권한 인가 필요)
<img width="1086" alt="스크린샷 2024-07-25 오후 4 14 53" src="https://github.com/user-attachments/assets/6f2271c1-a128-4612-a785-3992eb79adfe">

2. 마이페이지 수정 - 포스트맨에서 userId, AccessToken과 수정하고자 하는 값이 request(닉네임, 나이, 직업)으로 온보딩 정보를 수정할 수 있습니다. (어세스 토큰 권한 인가 필요)
<img width="1086" alt="스크린샷 2024-07-25 오후 4 18 28" src="https://github.com/user-attachments/assets/fcccca48-d6c4-4136-80dd-e55b73c59b02">
<img width="736" alt="스크린샷 2024-07-25 오후 4 18 39" src="https://github.com/user-attachments/assets/1581a5a9-4e7a-413f-949d-03c84ff0193e">

### 기능 구현 사진 설명 (토큰 재발급)

1. 쿠키의 리프레쉬 토큰을 이용하여 어세스토큰을 재발급 합니다.(보안을 위하여 어세스토큰과 리프레쉬 토큰을 같이 재발급, 헤더의 Cookies에 리프레쉬 토큰을 넣어줘야 합니다. 어세스토큰 권한 인가는 필요없습니다.)
<img width="1083" alt="스크린샷 2024-07-25 오후 4 21 34" src="https://github.com/user-attachments/assets/ef014115-609d-460a-a947-a281856bb0bb">
<img width="638" alt="스크린샷 2024-07-25 오후 4 21 55" src="https://github.com/user-attachments/assets/36bd51e5-0f42-4128-bca2-71fdb5482c48">


## ** 데이터베이스 설정(MYSQL)**
<br/>

🎯유저
<br/>
CREATE TABLE users (<br/>
            id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY ,<br/>
            name VARCHAR(255) NOT NULL,<br/>
            provider VARCHAR(255) NOT NULL,<br/>
            provider_id VARCHAR(255) NOT NULL UNIQUE<br/>
);<br/><br/>
🎯온보딩
<br/>
CREATE TABLE onboarding (<br/>
            id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY ,<br/>
            user_id BIGINT NOT NULL UNIQUE,<br/>
            nickname VARCHAR(255) NOT NULL,<br/>
            age INT NOT NULL,<br/>
            job VARCHAR(255) NOT NULL,<br/>
            CONSTRAINT fk_onboarding FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE<br/>
);<br/><br/>
🎯리프레쉬 토큰
<br/>
CREATE TABLE refresh_token (<br/>
            id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY ,<br/>
            user_id BIGINT NOT NULL UNIQUE ,<br/>
            token VARCHAR(512) NOT NULL,<br/>
            CONSTRAINT fk_refresh_token FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE<br/>
);<br/>
<br/>

## **🎯 프로그래밍 설정**

- Java 17 버전을 기준으로 진행합니다.
