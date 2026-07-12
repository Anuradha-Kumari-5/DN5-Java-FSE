# Account & Loan Microservices

Two independent Spring Boot projects — `account` and `loan` — each with
its own `pom.xml`, each runnable on its own, demonstrating the core
microservices idea: **separate deployable units instead of one monolith**.

- `account` → `GET /accounts/{number}` → port **8080** (default)
- `loan` → `GET /loans/{number}` → port **8081** (explicitly set, since
  8080 is already taken by `account`)

I generated both of these exactly as `start.spring.io` would (Spring
Boot 3.3.2, Java 17, `spring-boot-starter-web` + `spring-boot-devtools`)
and implemented the two controllers. A few steps in your exercise are
inherently things that happen on *your* machine — creating folders on
your `D:` drive, clicking through the start.spring.io UI, importing into
Eclipse — I can't do those for you, but here's exactly how to slot these
projects into that workflow.

---

## What you still need to do locally

1. **Create your folders.**
   ```
   D:\<your-employee-id>\microservices\
   ```
2. **Unzip both projects** (`account.zip` and `loan.zip`, provided
   below) directly into that `microservices` folder, so you end up
   with:
   ```
   D:\<employee-id>\microservices\account\
   D:\<employee-id>\microservices\loan\
   ```
   This replaces "open start.spring.io, fill the form, click Generate,
   extract the zip" — the end result (an unzipped Maven project folder
   with the right group/artifact IDs and dependencies) is the same.
3. **Build each one** from a command prompt opened in that folder:
   ```
   cd D:\<employee-id>\microservices\account
   mvn clean package

   cd D:\<employee-id>\microservices\loan
   mvn clean package
   ```
4. **Import both into Eclipse**: File → Import → Maven → Existing Maven
   Projects, and browse to each folder in turn.
5. **Run each one** the normal Eclipse way: right-click the `*Application.java`
   class → Run As → Spring Boot App (or Java Application). Start
   `account` first, then `loan` — see the port note below.

---

## Why the port conflict happens, and how it's already fixed here

Every Spring Boot app defaults to port **8080**. If `account` is
already running and bound to 8080, launching `loan` with no port
configured will fail with something like:

```
***************************
APPLICATION FAILED TO START
***************************

Description:
Web server failed to start. Port 8080 was already in use.
```

`loan`'s `application.properties` already has this fix applied for you:
```properties
server.port=8081
```
So if you run `account` first (claims 8080) and then `loan` (claims
8081), both start cleanly side by side. If you want to reproduce the
failure yourself for the exercise, comment out that `server.port` line,
start `account`, then try starting `loan` — you'll see the exact error
above — then uncomment it and re-run to see it succeed.

In Eclipse's **Console** view, once both apps are running you'll have
two separate console sessions; use the small monitor/display icon in
the top-right of the Console view to switch between them (exactly as
your exercise notes).

---

## Testing both services

Once both are running:

```
http://localhost:8080/accounts/00987987973432
```
```json
{"number":"00987987973432","type":"savings","balance":234343}
```

```
http://localhost:8081/loans/H00987987972342
```
```json
{"number":"H00987987972342","type":"car","loan":400000,"emi":3258,"tenure":18}
```

A design note: both controllers echo back whatever `{number}` you put
in the URL (via `@PathVariable`), while the rest of the fields stay
fixed dummy values — so `/accounts/anything-you-type` will return that
same number back to you with the sample `type`/`balance`. This shows
`@PathVariable` genuinely working, rather than the endpoint ignoring
its own path entirely.

Try both in a browser and in Postman, and check the **Network** tab /
Postman **Headers** tab as in earlier exercises — you'll see
`Content-Type: application/json` on both, since each controller returns
an object that Jackson serializes automatically.

---

## Why this matters (the microservices point of the exercise)

Right now the split might feel arbitrary — two tiny services that don't
even talk to each other. But this is the core shape of microservices:
each one is its own Maven project, its own JAR, its own deployable
process, its own port, independently startable, buildable, and
scalable. A monolith would have one `pom.xml`, one JAR, one process
serving both `/accounts/*` and `/loans/*`; if you needed to update just
the loan logic, you'd have to redeploy the entire application. Here,
you could restart, redeploy, or scale `loan` without touching `account`
at all — that independence is the whole point, even before any real
inter-service communication (like `account` calling `loan`, or a
gateway routing between them) gets introduced later.
