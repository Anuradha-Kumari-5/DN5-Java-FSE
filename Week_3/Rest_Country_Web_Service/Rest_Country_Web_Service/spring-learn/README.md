# spring-learn

A small Spring Boot project built up across two assignments. It currently
exposes two endpoints:

- `GET /hello` — returns the plain text `Hello World!!`
- `GET /country` — returns India's details as JSON, loaded from an XML
  bean definition:
  ```json
  {
    "code": "IN",
    "name": "India"
  }
  ```

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

## 2. The Country service, specifically

`HelloController` builds its response by hand — it's just a String
literal. `CountryController` works differently, on purpose, because the
assignment wants the India data defined in **Spring XML configuration**
rather than in Java code:

1. **`beans.xml`** (in `src/main/resources`) defines a bean with id
   `india`, of type `Country`, and sets its `code` and `name` properties
   via `<property>` tags. This is the classic pre-annotation way of
   telling Spring "build me one of these, configured like this."
2. **`SpringLearnApplication`** is annotated with
   `@ImportResource("classpath:beans.xml")`. Spring Boot's context is
   normally built from annotations (`@Component`, `@RestController`,
   etc.), so this annotation is the bridge that tells it to *also* load
   bean definitions from that XML file at startup.
3. **`CountryController.getCountryIndia()`** doesn't construct a
   `Country` object itself. It has the `ApplicationContext` injected
   (`@Autowired`), and calls
   `applicationContext.getBean("india", Country.class)` to pull the
   already-built bean out of the container.
4. Whatever the method returns, Spring hands to its configured
   `HttpMessageConverter`s to turn into bytes for the response. Because
   `spring-boot-starter-web` pulls in **Jackson** by default, and the
   return type is a plain object (not a `String`), Spring picks the
   Jackson JSON converter automatically. Jackson reflects over
   `Country`'s getters (`getCode()`, `getName()`) and writes one JSON
   key per getter — `code` and `name` — producing exactly the response
   shape the assignment asks for. This is also why `Country` needs
   public getters: Jackson relies on the JavaBean getter convention, not
   on the private fields directly.

So the full request lifecycle for `GET /country` is: Tomcat receives the
request → `DispatcherServlet` matches it to
`CountryController.getCountryIndia()` (via `@RequestMapping`) → the
method fetches the pre-built `india` bean from the context → Spring
serializes that bean to JSON via Jackson → the JSON is written to the
response body with `Content-Type: application/json`.

---

## 3. Project structure

```
spring-learn/
├── pom.xml
├── README.md
├── .gitignore
└── src
    ├── main
    │   ├── java/com/cognizant/springlearn/
    │   │   ├── SpringLearnApplication.java       (main class; @ImportResource loads beans.xml)
    │   │   ├── controller/
    │   │   │   ├── HelloController.java           (GET /hello)
    │   │   │   └── CountryController.java          (GET /country)
    │   │   └── model/
    │   │       └── Country.java                    (POJO: code, name)
    │   └── resources/
    │       ├── application.properties              (sets port 8083)
    │       └── beans.xml                            (defines the "india" bean)
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

## 4. How to run it

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

## 5. Testing the endpoints

### In a browser (Chrome)

1. Open `http://localhost:8083/hello` — you should see `Hello World!!`.
2. Open `http://localhost:8083/country` — you should see:
   ```json
   {"code":"IN","name":"India"}
   ```
   Chrome renders JSON in its built-in JSON viewer by default (with
   collapsible fields); you can switch to "Raw" to see the plain text.
3. Open DevTools (`F12` or right-click → Inspect) → **Network** tab →
   reload the page → click the request (`hello` or `country`) → open
   the **Headers** section. You'll see two groups:
   - **Request Headers** — what Chrome sent (e.g. `Host`, `User-Agent`,
     `Accept`).
   - **Response Headers** — what the Spring Boot server sent back. For
     `/hello`:
     - `Content-Type: text/plain;charset=UTF-8`
     - `Content-Length: 13`
     For `/country`:
     - `Content-Type: application/json` — this is the key difference
       from `/hello`; it's set automatically because Spring detected
       the response body was serialized by the Jackson JSON converter,
       not written as raw text.
     - `Content-Length` — byte size of the JSON string.
     - `Date`, `Connection: keep-alive`, etc. — same as `/hello`.

### In Postman

1. Create a **GET** request to `http://localhost:8083/hello` (or
   `/country`) and click **Send**.
2. The response body panel shows the text/JSON accordingly.
3. Click the **Headers** tab on the response panel to see the same
   response headers described above — this is where you'll visibly see
   `Content-Type: application/json` for `/country` versus
   `Content-Type: text/plain;charset=UTF-8` for `/hello`.

### Console logs

Both controller methods log at START and END, so each call prints two
INFO lines to the console running the app, e.g. for `/country`:

```
INFO ... c.c.springlearn.controller.CountryController : START - getCountryIndia()
INFO ... c.c.springlearn.controller.CountryController : END - getCountryIndia()
```

This is useful for confirming the method actually executed and for
timing/troubleshooting later on.

---

## 6. Why headers look the way they do

HTTP headers are metadata that ride alongside the actual response body
— like the label on a parcel versus the contents inside. `Content-Type`
tells the client how to interpret the bytes it's receiving (plain text
here, since we returned a raw `String`); if the controller had returned
an object instead, Spring would typically set
`Content-Type: application/json` and serialize it for you.
