# Hello World RESTful Web Service — spring-learn

A minimal Spring Boot REST service built for the "Hello World" assignment.
It exposes one endpoint, `GET /hello`, which returns the plain text
`Hello World!!`.

---

## 1. The idea, in plain terms

Think of a REST service like a **restaurant with a very specific menu**.
Each item on the menu is a URL (like `/hello`), and each way of ordering
it — dine-in, takeaway, delivery — is an HTTP method (GET, POST, PUT,
DELETE...). When you call `GET http://localhost:8083/hello`, you're
walking up to the counter and asking for the "hello" item using the
"GET" ordering method (GET always means "just give me something, don't
change anything on your end").

Spring Boot's job is to be the **kitchen staff**: it listens on a port
(8083 here), matches incoming orders to the right recipe
(`HelloController.sayHello()`), and hands back the response.

- `@RestController` tells Spring: "whatever this class's methods return,
  write it straight into the HTTP response body — don't try to render
  it as an HTML page."
- `@GetMapping("/hello")` tells Spring: "if a GET request comes in for
  `/hello`, route it to this method."
- The method itself just returns a hardcoded `String`. Spring Boot
  converts that String into the response body as-is (`text/plain`).

---

## 2. Project structure

```
spring-learn/
├── pom.xml
├── README.md
├── .gitignore
└── src
    ├── main
    │   ├── java/com/cognizant/springlearn/
    │   │   ├── SpringLearnApplication.java     (main class, starts the app)
    │   │   └── controller/HelloController.java (the /hello endpoint)
    │   └── resources/
    │       └── application.properties          (sets port 8083)
    └── test
        └── java/com/cognizant/springlearn/
            └── SpringLearnApplicationTests.java
```

> **Note on package naming:** the assignment specifies the package
> `com.cognizant.spring-learn.controller`. Java package names can't
> contain a hyphen (`-`) — the compiler treats it as a minus sign — so
> this project uses `com.cognizant.springlearn.controller` instead. The
> artifact/project name `spring-learn` (with the hyphen) is kept as-is
> in `pom.xml`, since Maven artifact IDs allow it. If your SME expects
> the literal hyphenated package, mention this constraint — it's a
> genuine Java language rule, not a shortcut.

---

## 3. How to run it

You'll need Java 17+ and Maven installed.

```bash
cd spring-learn
mvn spring-boot:run
```

Or build a jar and run it directly:

```bash
mvn clean package
java -jar target/spring-learn-0.0.1-SNAPSHOT.jar
```

You should see Spring Boot's startup logs, ending with something like:

```
Tomcat started on port 8083 (http) with context path ''
Started SpringLearnApplication in x.xxx seconds
```

---

## 4. Testing the endpoint

### In a browser (Chrome)

1. Open `http://localhost:8083/hello`
2. You should see `Hello World!!` printed on the page.
3. Open DevTools (`F12` or right-click → Inspect) → **Network** tab →
   reload the page → click the `hello` request → open the **Headers**
   section. You'll see two groups:
   - **Request Headers** — what Chrome sent (e.g. `Host`, `User-Agent`,
     `Accept`).
   - **Response Headers** — what the Spring Boot server sent back, for
     example:
     - `Content-Type: text/plain;charset=UTF-8` — tells the browser the
       body is plain text.
     - `Content-Length: 13` — size of `Hello World!!` in bytes.
     - `Date` — timestamp of the response.
     - `Connection: keep-alive`

### In Postman

1. Create a new **GET** request to `http://localhost:8083/hello`.
2. Click **Send**.
3. The response body panel shows `Hello World!!`.
4. Click the **Headers** tab (next to Body) on the response panel to see
   the same response headers described above (`Content-Type`,
   `Content-Length`, `Date`, etc.), plus Postman-added ones if any.

### Console logs

Because `sayHello()` logs at START and END, each call to `/hello`
prints two INFO lines to the console running the app, e.g.:

```
INFO ... c.c.springlearn.controller.HelloController : START - sayHello()
INFO ... c.c.springlearn.controller.HelloController : END - sayHello()
```

This is useful for confirming the method actually executed and for
timing/troubleshooting later on.

---

## 5. Why headers look the way they do

HTTP headers are metadata that ride alongside the actual response body
— like the label on a parcel versus the contents inside. `Content-Type`
tells the client how to interpret the bytes it's receiving (plain text
here, since we returned a raw `String`); if the controller had returned
an object instead, Spring would typically set
`Content-Type: application/json` and serialize it for you.
