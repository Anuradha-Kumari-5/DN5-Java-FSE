# JPA vs Hibernate vs Spring Data JPA

## 1. The idea, in plain terms

Think of it as three layers stacked on top of each other, each solving
a different problem:

- **JPA** is a *contract* — a specification (JSR 338) that says "any
  library that wants to call itself a Java persistence provider must
  support these annotations and interfaces" (`@Entity`, `@Id`,
  `EntityManager`, and so on). A specification is just a rulebook — it
  has no code of its own you can run. Think of it like a blueprint for
  a power outlet: it defines the shape and voltage, but a blueprint
  doesn't power anything by itself.
- **Hibernate** is one *implementation* of that contract — the actual
  outlet manufactured to the blueprint's spec. It's the ORM
  (Object-Relational Mapping) tool that does the real work: turning
  your Java objects into SQL `INSERT`/`SELECT`/`UPDATE` statements and
  back again. (Other implementations exist too — EclipseLink is
  another one — but Hibernate is by far the most common, and it's what
  Spring Boot uses by default.)
- **Spring Data JPA** sits *on top of* a JPA implementation like
  Hibernate. It doesn't talk to the database itself — it doesn't
  implement JPA. What it does is eliminate the repetitive plumbing code
  (opening sessions, managing transactions, catching
  `HibernateException`) you'd otherwise write by hand every single time
  you need a repository. It's a power strip built to fit into that
  outlet, so you don't have to wire a new plug for every single device.

So the dependency direction is: **Spring Data JPA → (uses) → a JPA
implementation like Hibernate → (implements) → the JPA specification.**
You can use Hibernate directly without Spring Data JPA (that's the
"raw Hibernate" style below), and you can theoretically swap Hibernate
for a different JPA provider underneath Spring Data JPA without
rewriting your repositories — that's the whole point of coding against
a specification instead of a specific tool.

---

## 2. Quick comparison

| | JPA | Hibernate | Spring Data JPA |
|---|---|---|---|
| **What it is** | A specification (JSR 338) | An ORM tool / library | A Spring abstraction layer |
| **Has runnable code?** | No — just interfaces/annotations | Yes — a full implementation | Yes — but delegates the actual persistence work to a JPA provider underneath |
| **Talks to the database?** | N/A | Yes, directly | No — indirectly, through Hibernate (or another provider) |
| **Manages transactions?** | Defines the concept | Requires manual `Transaction` handling unless wrapped | Yes — handles it for you via `@Transactional` |
| **Boilerplate** | N/A | You write session/transaction/exception-handling code yourself | Mostly eliminated — extend an interface, get CRUD for free |

---

## 3. The code comparison, explained

**Raw Hibernate** — every piece of the transaction lifecycle is your
responsibility:
```java
public Integer addEmployee(Employee employee){
   Session session = factory.openSession();
   Transaction tx = null;
   Integer employeeID = null;

   try {
      tx = session.beginTransaction();
      employeeID = (Integer) session.save(employee);
      tx.commit();
   } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      e.printStackTrace();
   } finally {
      session.close();
   }
   return employeeID;
}
```
Notice everything you're on the hook for here: opening a `Session`,
starting a `Transaction`, committing it, rolling it back on failure,
and closing the session in a `finally` block so it doesn't leak. Get
any one of those steps wrong across a large codebase, and you've got a
connection leak or a silently uncommitted transaction.

**Spring Data JPA** — the same operation, with all of that handled for
you:
```java
// EmployeeRepository.java
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}

// EmployeeService.java
@Autowired
private EmployeeRepository employeeRepository;

@Transactional
public void addEmployee(Employee employee) {
    employeeRepository.save(employee);
}
```
`JpaRepository<Employee, Integer>` — no method body at all — already
gives you `save()`, `findById()`, `findAll()`, `deleteById()`, and more.
Spring generates the implementation of that interface at runtime.
`@Transactional` replaces the entire manual `try/beginTransaction/
commit/rollback/finally` block from the Hibernate example: Spring
wraps the method in a transaction automatically, commits on success,
and rolls back on an unchecked exception.

---

## 4. Tying this to the `orm-learn` project

This maps directly onto what's already in your `orm-learn` repo:

- `CountryRepository extends JpaRepository<Country, String>` — this
  *is* the Spring Data JPA layer. No SQL, no `Session`, no
  `Transaction` object anywhere in that file.
- `CountryService.getAllCountries()` calls
  `countryRepository.findAll()` and is annotated `@Transactional` —
  same pattern as `EmployeeService.addEmployee()` above, just for a
  read instead of a write.
- Underneath both of those, at runtime, it's **Hibernate** doing the
  actual work: turning `findAll()` into
  `SELECT co_code, co_name FROM country`, and turning each row back
  into a `Country` object. That's exactly what the
  `logging.level.org.hibernate.SQL=trace` line in
  `application.properties` reveals in your console logs — those trace
  logs are Hibernate showing you the real SQL it generated on your
  behalf.
- `javax.persistence.Entity`, `@Id`, `@Column` used on `Country.java`
  are the **JPA specification's** annotations — the contract both
  Hibernate and (if you ever swapped providers) something like
  EclipseLink would honor identically.

So in one request/response cycle through your app: your code calls
Spring Data JPA → Spring Data JPA delegates to Hibernate → Hibernate
generates and executes SQL against MySQL using the mappings JPA's
annotations described.

---

## References

- [DZone — What is the Difference Between Hibernate and Spring Data JPA](https://dzone.com/articles/what-is-the-difference-between-hibernate-and-sprin-1)
- [JavaWorld — What is JPA? Introduction to the Java Persistence API](https://www.javaworld.com/article/3379043/what-is-jpa-introduction-to-the-java-persistence-api.html)
