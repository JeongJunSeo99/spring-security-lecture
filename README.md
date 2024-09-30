# spring-security-pratice

- OAuth2 회원가입 시나리오
1. Web Security에서 OAuth(oauth2/authorization/google)로 들어온 요청 가로챔
2. 각 플랫폼 로그인 화면을 리다이렉트
3. 클라이언트에서 로그인 정보 입력
4. Web Security의 OAuth2SuccessHandler 호출
5. CustomOAuth2User 객체를 생성
7. CustomOAuth2UserService의 loadUser동작
8. 플랫폼에서 소셜 id, 닉네임, 프로필 이미지 등 가져옴
9. 자체 DB에 임시로 회원정보 저장 (이메일 값이나 role값 임시)
10. 프론트엔드의 추가 회원가입 페이지로 리다이렉트
11. 프론트엔드에서 추가 정보 입력
12. 임시로 저장된 회원의 정보 수정(이메일 값, role값(GUEST -> USER로))
13. 클라이언트로 200 response
<br>

- OAuth2 로그인 시나리오
1. 클라이언트에서 OAuth2 로그인 정보 입력
2. Web Security의 OAuth2SuccessHandler 호출
3. CustomOAuth2User 객체를 생성
4. CustomOAuth2UserService의 loadUser동작
5. 플랫폼에서 소셜 id, 닉네임, 프로필 이미지 등 가져옴
6. 이미 DB에 저장된 정보가 있으므로 소셜 id로 조회 후 CustomOAuth2User 객체 생성 
7. OAuth2SuccessHandler에서 Role Type에 따라 회원가입 or 로그인을 진행 (해당 시나리오에서는 로그인)
8. 클라이언트에게는 Access Token Response, 서버의 Redis에는 Access Token과 Refresh Token 쌍 저장 (만료시간 정해서)
<br>

- 자체 회원가입 시나리오
1. 클라이언트에서 정보 입력 후 정의해둔 API로 회원가입 요청
2. password는 Bcrypt로 암호화해서 저장
3. 클라이언트로 200 response
<br>

- 자체 로그인 시나리오
1. 클라이언트에서 로그인 정보 입력 
2. Authentication filter를 통해 입력된 정보와 DB 정보 비교
3. 클라이언트에게는 Access Token Response, 서버의 Redis에는 Access Token과 Refresh Token 쌍 저장 (만료시간 정해서)
<br>

- 자동 로그인 시나리오
1. 클라이언트에는 Access Token이 있고, Redis에 Access Token과 Refresh Token 쌍이 저장되어 있음 
2. RefreshFilter에서 들고 온 Access Token 만료 여부 확인
    - 만료 시, Redis에 저장된 Refresh Token을 Access Token으로 조회 후 
      <br>
      (Access Token 재발급 && Request Header의 Access Token 갈아끼우기 && Response Header에 Access Token 추가 && Redis에 Access Token 업데이트
    -  만료가 아니라면 3번으로
3. JwtFilter에서 Access Token의 유효성 검사 진행
4. 서비스 이용
