# Cartel Admin Console

A deliberately small Spring Boot resource-server example that works with the supplied `cartel-auth-server`.

## What it demonstrates

- Login is performed by the separate auth server at `http://localhost:3000/auth/login`.
- The auth server returns a JWT.
- This app validates the JWT with Spring Security.
- Spring automatically maps the standard `scope` claim to authorities such as `SCOPE_admin`.
- `/cartel/**` and `/api/cartel/**` require `SCOPE_admin`.
- The protected page reads all users from the same MongoDB used by the auth server.
- MongoDB is expected at `localhost:27018`, database `cartel-user-database`.

The supplied auth-server seed data gives `marcus` the scopes:

    client admin advisor analyst

so `marcus / abc123` can open the cartel page.

## Run

1. Start the auth server first. It owns the embedded MongoDB process on port `27018`.
2. Start this app:

    mvn spring-boot:run

3. Open:

    http://localhost:8080/

4. Login with:

    username: marcus
    password: abc123

5. The browser is redirected to:

    http://localhost:8080/cartel/index.html

## Why the JWT is also put in a cookie

A JavaScript `fetch()` call can add:

    Authorization: Bearer <jwt>

but a normal browser navigation to `/cartel/index.html` cannot add that custom header.

For this learning demo, the login page stores the JWT in an `access_token` cookie. `SecurityConfig` contains a small
`BearerTokenResolver` that accepts either the normal Authorization header or that cookie.

This is intentionally simple teaching code, not a recommended production login/session design.

## Important auth-server fix: JWT header encoding

Your current auth server defines the JWT header as JSON text:

```java
private static final String HEADER = """
        {"alg":"HS256","typ":"JWT"}""";
```

but currently does this:

```java
String unsignedHeaderAndClaims = encodeJson(HEADER) + "." + encodeJson(claims);
```

`encodeJson(HEADER)` asks Jackson to serialize the Java `String`, so the decoded JWT header becomes a JSON string
containing JSON instead of a JSON object. Spring's JWT decoder expects the header itself to be a JSON object.

Keep your literal header, but Base64URL-encode the raw text:

```java
String unsignedHeaderAndClaims = encodeText(HEADER) + "." + encodeJson(claims);
```

and add:

```java
private String encodeText(String value) {
    return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
}
```

Your claims code can stay as it is:

```java
claims.put("iss", "http://localhost:3000");
claims.put("aud", "cartel-control");
claims.put("sub", username);
claims.put("scope", roles);
claims.put("exp", Instant.now().getEpochSecond() + 86400);
```

## Important auth-server seed-data fix

The supplied `App` currently checks:

```java
if (!users.existsById("alice")) {
```

but then inserts `marcus`, `robert`, and `jimbo`, not `alice`.

With persistent MongoDB, that can attempt to insert the same users again on a later startup. A simple teaching-friendly
version is:

```java
if (!users.existsById("marcus")) {
    users.insert(new UserAccount("marcus", passwordEncoder.encode("abc123"), "client admin advisor analyst", null));
}

if (!users.existsById("robert")) {
    users.insert(new UserAccount("robert", passwordEncoder.encode("abc123"), "admin", null));
}

if (!users.existsById("jimbo")) {
    users.insert(new UserAccount("jimbo", passwordEncoder.encode("abc123"), "advisor", null));
}
```

## What the protected user page shows

The MongoDB `users` documents currently contain:

- `username`
- `passwordHash`
- `roles`
- `refreshTokenHash`

The current auth server does not store issued access JWTs in MongoDB. Therefore the table cannot show an access token
for every user. It does show the current browser's access JWT separately at the top of the page.

Showing password hashes and refresh-token hashes is intentionally educational and should not be copied into a real
administration UI.
