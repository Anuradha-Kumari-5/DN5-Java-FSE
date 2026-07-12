# LibraryManagement (Exercise 4 — Maven project setup)

A fresh Maven project set up specifically for this exercise: get the
project skeleton and `pom.xml` right before any actual application code
gets written in later exercises.

> **Naming note:** this reuses the project name `LibraryManagement`
> from your earlier Dependency Injection exercise, but it's a
> **separate project** with a different dependency set (Spring AOP +
> WebMVC added, not just Context) and a different Java target (1.8 here
> vs 17 there). If both need to live in the same GitHub repo, rename
> one of the two folders before pushing — e.g.
> `LibraryManagement-DI` and `LibraryManagement-WebMVC` — so they don't
> collide.

---

## 1. What's in `pom.xml`, and why

**Dependencies, as specified:**
- `spring-context` — the IoC container itself: `ApplicationContext`,
  bean lifecycle management, dependency injection. Same role it played
  in the earlier exercise.
- `spring-aop` — Aspect-Oriented Programming support. This is what lets
  cross-cutting concerns (logging, security checks, transaction
  boundaries) be applied *declaratively* — e.g. via `@Transactional` —
  instead of being manually written into every single method that
  needs them. (`@Transactional` in your earlier `orm-learn` exercise is
  actually powered by Spring AOP under the hood — that annotation
  works by wrapping your method call in a proxy that starts/commits a
  transaction around it.)
- `spring-webmvc` — Spring's MVC framework, sitting on top of the Java
  Servlet API. This is what a later exercise will presumably use to add
  `@Controller` classes, request mappings, and view resolution — the
  web-facing layer this application doesn't have yet.

**Two additions beyond the literal ask, and why I made them:**
- `javax.servlet-api` (`provided` scope) — `spring-webmvc` needs the
  Servlet API on the compile classpath (things like
  `HttpServletRequest` come from here), but a servlet container like
  Tomcat supplies the actual implementation at deploy time. `provided`
  scope means: available while compiling, but *not* bundled into the
  final artifact — bundling it would conflict with the container's own
  copy.
- `packaging=war` — Spring WebMVC applications (without Spring Boot's
  embedded Tomcat) are deployed as WAR files to an external servlet
  container. Without this, Maven would default to `jar` packaging,
  which isn't deployable to Tomcat/similar containers the way a
  WebMVC app needs to be. I expect a later exercise to add a
  `DispatcherServlet` registration (via `web.xml` or a
  `WebApplicationInitializer`) to actually make this a working web app
  — this project intentionally doesn't add that yet, since it's outside
  today's three steps.

**Maven Compiler Plugin**, pinned explicitly to Java 1.8:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
    </configuration>
</plugin>
```
Without this, Maven would compile against whatever JDK version is
active on your machine by default, which may not match 1.8 — this
locks the *source and target bytecode level* regardless of which JDK
you have installed, so the build is reproducible across machines.

**Spring version:** `5.3.39`, not the `6.x` line used in your other
recent Spring projects. This is a hard constraint, not a stylistic
choice — Spring Framework 6 requires Java 17 as a baseline and switched
from `javax.*` to `jakarta.*` package namespaces; targeting Java 1.8
here means Spring 5.3.x (the last major line built for Java 8) is the
correct, compatible choice.

---

## Project structure

```
LibraryManagement/
├── pom.xml
├── README.md
├── .gitignore
└── src
    ├── main
    │   ├── java/com/library/       (empty for now — later exercises add controllers/config here)
    │   └── resources/               (empty for now — later exercises add XML/web.xml here)
    └── test
        └── java/com/library/        (empty for now)
```

---

## Verifying the setup

Java 8 (or a later JDK invoked with `-source 8 -target 8` support) and
Maven required.

```bash
cd LibraryManagement
mvn clean compile
```

A clean `BUILD SUCCESS` confirms: the three Spring dependencies resolve
correctly, and the compiler plugin is correctly enforcing Java 1.8 as
the source/target level. There's no application code yet for this
build to actually run — that arrives in later exercises.
