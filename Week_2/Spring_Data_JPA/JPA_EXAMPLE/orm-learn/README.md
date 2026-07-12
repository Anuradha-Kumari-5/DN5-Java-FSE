# orm-learn

Demo project for Spring Data JPA and Hibernate, reading a `country`
table from MySQL through a repository → service → main-method test
chain (no REST controller yet in this exercise).

---

## Two things I changed from the assignment text, and why

**1. Package name.** The assignment writes `com.cognizant.orm-learn.model`
— but Java package names can't contain a hyphen (the compiler reads `-`
as a minus sign). This project uses `com.cognizant.ormlearn.*`
throughout instead. Same issue as your earlier `spring-learn` project.

**2. Column names in `Country.java`.** This one's worth flagging
directly: the assignment's own two sections disagree with each other.
The **table creation SQL** defines columns `co_code` and `co_name`:
```sql
create table country(co_code varchar(2) primary key, co_name varchar(50));
```
but the **sample `Country.java`** maps to columns named `code` and
`name`:
```java
@Column(name="code")
@Column(name="name")
```
Those don't match — if you used the sample code as-written against the
sample table as-written, Hibernate's `ddl-auto=validate` would fail at
startup with a schema mismatch error, since `code`/`name` columns don't
exist in the actual table. This project's `Country.java` uses
`@Column(name="co_code")` and `@Column(name="co_name")` — matching the
actual table — so it will validate and run correctly. Worth mentioning
to your SME, since it's a genuine inconsistency in the brief rather
than a judgment call.

---

## Pre-requisites (unchanged from the assignment)

- MySQL Server 8.0, running locally
- MySQL Workbench 8 (optional, for a GUI)
- Eclipse IDE for Enterprise Java Developers
- Maven 3.6.2+
- Java 17

---

## Setting it up

### 1. Create the schema and table

```
mysql -u root -p
mysql> create schema ormlearn;
```

Then run everything in `sql/country.sql` (via MySQL Workbench, or
`mysql -u root -p ormlearn < sql/country.sql`). This creates the
`country` table and inserts the two sample rows (`IN`/India,
US/United States of America). `spring.jpa.hibernate.ddl-auto=validate`
in `application.properties` means **Hibernate will not create this
table for you** — it only checks the entity matches what's already
there, so this step is mandatory before running the app.

### 2. Check the datasource credentials

`src/main/resources/application.properties` currently has:
```properties
spring.datasource.username=root
spring.datasource.password=root
```
Update these to match your local MySQL root password if it differs.

### 3. Import into Eclipse

File → Import → Maven → Existing Maven Projects → browse to the
extracted `orm-learn` folder → Finish.

### 4. Build

```
mvn clean package
```
If you're on Cognizant's network and need the proxy, add the flags the
assignment specifies:
```
mvn clean package -Dhttp.proxyHost=proxy.cognizant.com -Dhttp.proxyPort=6050 -Dhttps.proxyHost=proxy.cognizant.com -Dhttps.proxyPort=6050 -Dhttp.proxyUser=123456
```
(Swap `123456` for your actual proxy username.)

### 5. Run

Run `OrmLearnApplication` as a Java Application in Eclipse, or:
```
mvn spring-boot:run
```

You should see, among the startup logs:
```
... INFO  ... OrmLearnApplication : Inside main
... INFO  ... OrmLearnApplication : Start
... DEBUG ... OrmLearnApplication : countries=[Country [code=IN, name=India], Country [code=US, name=United States of America]]
... INFO  ... OrmLearnApplication : End
```
That confirms: the app started, `main()` ran, and `CountryService` →
`CountryRepository` → the real `country` table round-tripped
successfully.

---

## SME walkthrough — the six points requested

**1. `src/main/java`** — all application code: `OrmLearnApplication`
(entry point), `model/Country.java` (JPA entity), `repository/CountryRepository.java`
(data access), `service/CountryService.java` (business logic layer).

**2. `src/main/resources`** — `application.properties`, holding
everything environment-specific: logging levels/pattern, the MySQL
connection URL/credentials, and Hibernate's dialect/ddl-auto settings.
Nothing here is compiled — Spring reads it at startup.

**3. `src/test/java`** — `OrmLearnApplicationTests`, a
`@SpringBootTest`-annotated class that boots the full Spring context to
confirm the app wires up correctly. This one specifically needs a live,
correctly-schema'd MySQL connection to pass, since the context includes
the datasource.

**4. `OrmLearnApplication.java`, walked through:**
```java
ApplicationContext context = SpringApplication.run(OrmLearnApplication.class, args);
LOGGER.info("Inside main");
countryService = context.getBean(CountryService.class);
testGetAllCountries();
```
`SpringApplication.run(...)` does the heavy lifting: it starts the
embedded application context, triggers component scanning, configures
the datasource and JPA/Hibernate machinery based on what's on the
classpath, and returns the live `ApplicationContext`. That context is
then used to explicitly pull out the `CountryService` bean
(`context.getBean(...)`) so the static `testGetAllCountries()` helper —
which lives outside the Spring-managed object graph, since it's called
from `main()` — can use it.

**5. Purpose of `@SpringBootApplication`** — it's shorthand for three
annotations stacked together: `@Configuration` (this class can define
beans), `@EnableAutoConfiguration` (Spring inspects what's on the
classpath — seeing `spring-boot-starter-data-jpa` plus a MySQL driver
is exactly what triggers Spring to auto-configure a working
`DataSource` and JPA setup without any of that being hand-written), and
`@ComponentScan` (Spring scans this package and its sub-packages for
`@Service`/`@Repository`/etc. and registers them as beans — this is how
`CountryService` and `CountryRepository` get discovered automatically).

**6. `pom.xml`:**
- The `<parent>` block pins the project to `spring-boot-starter-parent`
  version `2.7.18` — this is what supplies compatible, tested versions
  for every Spring Boot dependency without each one needing an explicit
  `<version>`.
- `spring-boot-starter-data-jpa` pulls in Hibernate (the JPA
  implementation), Spring's repository abstraction, and transaction
  management.
- `mysql-connector-java` is the actual JDBC driver — the thing that
  knows how to speak MySQL's wire protocol; without it, the JDBC URL in
  `application.properties` would have nothing to connect through.
- `spring-boot-devtools` (optional, runtime-scoped) gives automatic
  restarts on code changes during development.
- **Dependency Hierarchy**: in Eclipse, double-click `pom.xml` → the
  "Dependency Hierarchy" tab shows the full tree — e.g.
  `spring-boot-starter-data-jpa` pulling in `hibernate-core`,
  `spring-orm`, `spring-tx`, `jakarta.persistence-api` (well,
  `javax.persistence-api` in this 2.7.x version), and so on. It's worth
  opening this yourself once the project's imported, since the exact
  transitive versions depend on the Spring Boot BOM.

---

## Project structure

```
orm-learn/
├── pom.xml
├── README.md
├── .gitignore
├── sql/
│   └── country.sql                (table creation + sample data — run manually)
└── src
    ├── main
    │   ├── java/com/cognizant/ormlearn/
    │   │   ├── OrmLearnApplication.java
    │   │   ├── model/Country.java              (@Entity)
    │   │   ├── repository/CountryRepository.java  (extends JpaRepository)
    │   │   └── service/CountryService.java      (@Service, @Transactional)
    │   └── resources/
    │       └── application.properties
    └── test
        └── java/com/cognizant/ormlearn/
            └── OrmLearnApplicationTests.java
```
