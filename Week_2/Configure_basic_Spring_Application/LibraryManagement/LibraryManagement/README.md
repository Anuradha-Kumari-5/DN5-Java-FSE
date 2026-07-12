# LibraryManagement

A basic Spring Core application (no Spring Boot) demonstrating classic
XML-based dependency injection — Spring's original configuration style,
before annotations like `@Component`/`@Autowired` existed.

---

## 1. The idea, in plain terms

Every object in this project is a plain, unannotated Java class —
`BookService` and `BookRepository` don't know Spring exists. What makes
them "Spring beans" is entirely external: `applicationContext.xml`
lists them and describes how they connect. Spring reads that file, and
at startup:

1. Creates a `BookRepository` instance.
2. Creates a `BookService` instance.
3. Calls `bookService.setBookRepository(theBookRepositoryItJustMade)`.

This pattern — objects don't build their own dependencies, something
external hands the dependencies to them — is called **Dependency
Injection**, and the container that does the handing-over is doing
**Inversion of Control (IoC)**. "Inversion" because, without Spring,
`BookService` would normally do `new BookRepository()` itself; here,
that responsibility is inverted — moved out of `BookService` and into
the container.

---

## 2. How the four steps map to this project

**Step 1 — Maven project + Spring Core dependency.**
`pom.xml` declares `spring-context` (version 6.1.11), which transitively
pulls in `spring-core` and `spring-beans` — the three modules together
make up what people mean by "Spring Core." This is deliberately *not*
`spring-boot-starter-*` — there's no auto-configuration, no embedded
server, no component scanning by default. Everything is explicit.

**Step 2 — `applicationContext.xml`.**
Located at `src/main/resources/applicationContext.xml` (Maven copies
everything under `src/main/resources` onto the classpath root at build
time, which is why the main class can load it by filename alone). It
defines two `<bean>` elements — `bookRepository` and `bookService` —
and wires the second to the first via `<property name="bookRepository"
ref="bookRepository"/>`.

**Step 3 — Service and Repository classes.**
- `com.library.repository.BookRepository` — returns a hardcoded list
  of book titles (no real database; that's intentionally out of scope
  for this exercise).
- `com.library.service.BookService` — has a `BookRepository` field, a
  public setter for it (`setBookRepository`, which is what the XML's
  `<property>` tag calls into), and a `getAllBooks()` method that
  delegates to the repository.

**Step 4 — Main class.**
`com.library.LibraryManagementApplication` loads
`applicationContext.xml` via `ClassPathXmlApplicationContext`, retrieves
the `bookService` bean, calls `getAllBooks()`, and logs the result —
proving the whole wiring chain actually works.

---

## Project structure

```
LibraryManagement/
├── pom.xml
├── README.md
├── .gitignore
└── src
    ├── main
    │   ├── java/com/library/
    │   │   ├── LibraryManagementApplication.java   (main class)
    │   │   ├── service/BookService.java
    │   │   └── repository/BookRepository.java
    │   └── resources/
    │       └── applicationContext.xml               (bean definitions)
    └── test
        └── java/com/library/
            └── LibraryManagementApplicationTests.java
```

---

## How to run it

Java 17+ and Maven required.

**Option A — via the exec plugin (no packaging needed):**
```bash
cd LibraryManagement
mvn compile exec:java
```

**Option B — build a runnable jar and run it directly:**
```bash
mvn clean package
java -jar target/LibraryManagement-1.0-SNAPSHOT.jar
```

**Option C — from Eclipse:**
Import as an existing Maven project (File → Import → Maven → Existing
Maven Projects), then right-click `LibraryManagementApplication.java` →
Run As → Java Application.

Expected console output (via SLF4J's simple logger):
```
[main] INFO com.library.LibraryManagementApplication - START - loading applicationContext.xml
[main] INFO com.library.LibraryManagementApplication - Books retrieved via BookService -> BookRepository: [The Hobbit, 1984, Clean Code, Effective Java]
[main] INFO com.library.LibraryManagementApplication - END - context closed
```

---

## Why this matters, going forward

Every Spring Boot project you've built so far (`spring-learn`,
`orm-learn`, etc.) uses the *same underlying IoC container* as this
one — Spring Boot just replaces this hand-written XML with automatic
component scanning (`@Component`, `@Service`, `@Repository`) and
sensible defaults. Seeing the XML version first makes it clear what
those annotations are actually shorthand *for*: they're not magic,
they're just a more concise way of telling the same container the same
thing this file says explicitly.
