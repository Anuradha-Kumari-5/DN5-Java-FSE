# jwt-auth-service

A standalone authentication service. It exposes one endpoint,
`GET /authenticate`, which takes Basic Auth credentials and returns a
signed JWT:

```
curl -s -u user:pwd http://localhost:8090/authenticate
```
```json
{"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTcwMzc5NDc0LCJleHAiOjE1NzAzODA2NzR9.t3LRvlCV-hwKfoqZYlaVQqEUiBloWcWn0ft3tgv0dL0"}
```

The demo user is `user` / `pwd` (see `AuthenticationController.USER_STORE`
— hardcoded intentionally for this exercise; see the note at the bottom
on what a production version would need instead).

---

## The three exercises, as built here

### Exercise 1 — Controller + SecurityConfig

**`AuthenticationController`** exposes `GET /authenticate`.
**`SecurityConfig`** registers a `SecurityFilterChain` bean and
explicitly `permitAll()`s the `/authenticate` path.

That `permitAll()` is the detail worth understanding: Spring Security
ships with its *own* built-in Basic Auth handling
(`http.httpBasic()`), which would normally intercept any request
carrying an `Authorization: Basic ...` header before your controller
code ever runs. Since this exercise specifically wants *you* to read
and decode that header by hand (exercise 2), the security config has to
get out of the way for this one path and let the raw header through
untouched. Everything else falls through to `.anyRequest().authenticated()`,
so this permissive rule is scoped only to `/authenticate`.

Sessions are set to `STATELESS` and CSRF is disabled — both standard
for token-based auth, since there's no server-side session for CSRF to
protect and no cookie being set.

### Exercise 2 — Reading and decoding the Authorization header

Inside `authenticate()`:

```java
String base64Credentials = authHeader.substring("Basic ".length());
byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
String decoded = new String(decodedBytes, StandardCharsets.UTF_8);   // "user:pwd"
String[] parts = decoded.split(":", 2);                              // ["user", "pwd"]
```

What `curl -u user:pwd` actually sends over the wire is a header:
```
Authorization: Basic dXNlcjpwd2Q=
```
`dXNlcjpwd2Q=` is just `"user:pwd"` run through Base64 — **encoding,
not encryption**. Anyone intercepting this request (e.g. over plain
HTTP) can decode it as easily as this code does; Basic Auth is only
considered safe over HTTPS, where TLS is doing the actual protecting.
That's worth knowing plainly rather than assuming the Base64 step adds
any security by itself.

The split uses a limit of `2` (`split(":", 2)`) rather than an unbounded
split, specifically so a password containing a colon doesn't get cut
into extra pieces.

### Exercise 3 — Generating the token

Once `username`/`password` are validated against `USER_STORE`,
`JwtUtil.generateToken(username)` is called. `JwtUtil` builds the token
with the `jjwt` library:

```java
Jwts.builder()
    .setSubject(username)                 // "sub" claim
    .setIssuedAt(issuedAt)                // "iat" claim
    .setExpiration(expiresAt)             // "exp" claim
    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
    .compact();
```

A JWT is three Base64URL-encoded, dot-separated segments:
`header.payload.signature`.
- **Header** — `{"alg":"HS256"}`, naming the signing algorithm.
- **Payload** — the claims: here, `sub` (who this token is for), `iat`
  (issued-at time), `exp` (expiry time). This project's expiry is 20
  minutes (`jwt.expiration-ms=1200000` in `application.properties`),
  matching the 1200-second gap between `iat` and `exp` in the sample
  token from the assignment.
- **Signature** — HMAC-SHA256 over the header and payload, computed
  using a secret key (`jwt.secret`) that only this server knows. This
  is what makes the token tamper-evident: change the payload without
  the secret, and the signature won't match anymore.

**Important:** the header and payload are readable by anyone who has
the token — they are *not* encrypted, just encoded. Never put secrets
(passwords, credit card numbers, etc.) inside a JWT's claims. Paste any
JWT into [jwt.io](https://jwt.io) to see this yourself — the "Decoded"
panel needs no secret key to show you the header and payload.

---

## Project structure

```
jwt-auth-service/
├── pom.xml
├── README.md
├── .gitignore
└── src
    ├── main
    │   ├── java/com/cognizant/jwtauth/
    │   │   ├── JwtAuthServiceApplication.java
    │   │   ├── config/SecurityConfig.java        (exercise 1)
    │   │   ├── controller/AuthenticationController.java  (exercises 1 & 2)
    │   │   ├── util/JwtUtil.java                 (exercise 3)
    │   │   └── dto/TokenResponse.java
    │   └── resources/
    │       └── application.properties            (port 8090, jwt.secret, jwt.expiration-ms)
    └── test
        └── java/com/cognizant/jwtauth/
            └── JwtAuthServiceApplicationTests.java
```

---

## How to run it

Java 17+ and Maven required.

```bash
cd jwt-auth-service
mvn spring-boot:run
```

You should see Spring Boot's startup logs end with something like:
```
Tomcat started on port 8090 (http) with context path ''
Started JwtAuthServiceApplication in x.xxx seconds
```

---

## Testing it

### curl

```bash
curl -s -u user:pwd http://localhost:8090/authenticate
```
Expect: `{"token":"eyJhbGciOi..."}`

Wrong credentials:
```bash
curl -i -u user:wrongpassword http://localhost:8090/authenticate
```
Expect: `401 Unauthorized` with a JSON error body and a
`WWW-Authenticate: Basic realm="jwt-auth-service"` header.

### Postman

1. Create a **GET** request to `http://localhost:8090/authenticate`.
2. Go to the **Authorization** tab, choose type **Basic Auth**, and
   enter username `user` and password `pwd`. (Postman builds the
   `Authorization: Basic ...` header for you — same header curl's `-u`
   flag builds.)
3. Click **Send** — the body should show the `token` field.
4. Click the **Headers** tab on the response to see the standard
   response headers (`Content-Type: application/json`, etc.).

### Verify the token's contents

Copy the `token` value and paste it into [jwt.io](https://jwt.io) — the
decoded panel will show `{"alg":"HS256"}` as the header and
`{"sub":"user","iat":...,"exp":...}` as the payload, without needing
the signing secret.

---

## What this deliberately leaves out (for the next exercise)

This service only *issues* tokens — it doesn't yet *validate* them on
incoming requests. A typical next step in a JWT tutorial sequence adds
a `JwtRequestFilter` that reads an `Authorization: Bearer <token>`
header on protected endpoints, verifies the signature and expiry using
the same secret, and sets up the Spring Security context accordingly.

Also worth flagging: `USER_STORE` is a hardcoded, single-user, plain-text
map — fine for a learning exercise, but a real service would use a
`UserDetailsService` backed by a database, with passwords hashed (e.g.
via `BCryptPasswordEncoder`), never stored or compared in plain text.
