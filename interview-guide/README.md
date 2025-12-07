# Interview Guide

## What is Loose Coupling?

Lose Kopplung bedeutet, dass Klassen möglichst wenige direkte Abhängigkeiten voneinander haben.
Eine Klasse kennt idealerweise nur das Interface einer anderen Klasse, nicht deren konkrete
Implementierung.

Dadurch wird der Code:

- flexibler
- leichter testbar
- weniger fehleranfällig
- austauschbar und erweiterbar

Lose Kopplung ist ein zentrales Prinzip in Spring, weil Spring über Dependency Injection automatisch
dafür sorgt, dass Implementierungen austauschbar bleiben.

Beispiel:

```java
public interface MessageService {

  String getMessage();
}
```

```java

@Component
public class HelloMessageService implements MessageService {

  @Override
  public String getMessage() {
    return "Hello from loosely coupled service!";
  }
}
```

```java

@Component
public class MessagePrinter {

  private final MessageService messageService;

  public MessagePrinter(MessageService messageService) {
    this.messageService = messageService;
  }

  public void print() {
    System.out.println(messageService.getMessage());
  }
}
```

## What is a Dependency?

Eine Dependency ist eine andere Klasse, die eine Klasse benötigt, um ihre Aufgabe zu erfüllen.
Wenn eine Klasse ohne eine bestimmte andere Klasse nicht sinnvoll arbeiten kann, dann ist diese
andere Klasse eine Abhängigkeit.

```java
public class EmailService {

  public void sendMail(String text) {
  }
}
```

```java
public class OrderProcessor {

  private final EmailService emailService;

  public OrderProcessor(EmailService emailService) {
    this.emailService = emailService;
  }
}
```

EmailService ist hier eine Dependency des OrderProcessor.

Best Practice:
> Abhängigkeiten immer über den Konstruktor bereitstellen, nicht selbst erzeugen.

## ❓ Was ist IoC (Inversion of Control)?

IoC bedeutet, dass eine Klasse **nicht selbst steuert**, wie ihre Abhängigkeiten erzeugt oder
verwaltet werden.
Die Kontrolle darüber wird an ein **externes Framework** abgegeben – im Fall von Spring an den *
*IoC-Container** (ApplicationContext).

Statt dass eine Klasse ihre Abhängigkeiten selbst erstellt (`new`), erhält sie diese **von außen**.
Dadurch entsteht lose Kopplung, klarere Verantwortlichkeiten und austauschbarer Code.

### Beispielhaftes Prinzip

❌ Ohne IoC (klassische Programmierung):

```java
OrderService service = new OrderService(new EmailService());
```

✔ Mit IoC (Spring erzeugt und injiziert die Objekte):

```java

@Component
public class OrderService {

  private final EmailService emailService;

  public OrderService(EmailService emailService) {
    this.emailService = emailService;
  }
}
```

**Kernidee:**

> Nicht die Klasse entscheidet, welche Abhängigkeiten sie nutzt – der Container entscheidet.

### Kurze Zusammenfassung

* IoC = Framework übernimmt die Kontrolle über Objektlebenszyklen
* Klasse verwaltet ihre Abhängigkeiten nicht mehr selbst
* Grundlage für Dependency Injection

## ❓ Was ist Dependency Injection?

Dependency Injection (DI) bedeutet, dass eine Klasse ihre **Abhängigkeiten nicht selbst erzeugt**,
sondern sie **von außen** bereitgestellt bekommt.
Ein Framework wie Spring übernimmt das **Erzeugen, Konfigurieren und Injizieren** dieser
Abhängigkeiten automatisch.

Dadurch wird der Code:

* lose gekoppelt
* leichter testbar
* einfacher erweiterbar
* frei von `new`-Konstruktion für Services

### Beispiel

❌ Ohne DI – Klasse erzeugt ihre Abhängigkeit selbst:

```java
public class OrderService {

  private final EmailService emailService = new EmailService();
}
```

✔ Mit DI – Abhängigkeit wird von Spring injiziert:

```java

@Component
public class OrderService {

  private final EmailService emailService;

  public OrderService(EmailService emailService) {
    this.emailService = emailService;
  }
}
```

### Wie Spring DI umsetzt

Spring erkennt, dass `OrderService` ein `EmailService` benötigt, und:

1. erstellt `EmailService` als Bean
2. erstellt `OrderService`
3. injiziert `EmailService` in den Konstruktor

### Kurze Zusammenfassung

> DI ist die *Umsetzung* von IoC: nicht die Klasse selbst entscheidet über ihre Abhängigkeiten,
> sondern der Container.



Hier ist der **deutsche README-Eintrag** für *„Can you give few examples of Dependency
Injection?“* – klar, strukturiert und mit konkreten, kleinen Beispielen.

---

## ❓ Beispiele für Dependency Injection

Dependency Injection bedeutet, dass Spring die benötigten Objekte automatisch erstellt und einer
Klasse zur Verfügung stellt.
Es gibt drei typische Formen von DI, wobei in modernen Spring-Projekten **fast ausschließlich
Konstruktorinjektion** verwendet wird.

---

### 🔹 **1. Konstruktorinjektion (Best Practice)**

Spring erkennt die benötigten Abhängigkeiten im Konstruktor und injiziert sie beim Erzeugen der
Bean.

```java

@Component
public class OrderService {

  private final EmailService emailService;

  public OrderService(EmailService emailService) {
    this.emailService = emailService;
  }
}
```

✔ am klarsten
✔ unveränderliche Abhängigkeiten
✔ perfekt für Tests
✔ von Spring bevorzugt

---

### 🔹 **2. Setter-Injektion**

Spring ruft einen Setter auf, um die Abhängigkeit bereitzustellen.

```java

@Component
public class NotificationService {

  private EmailService emailService;

  @Autowired
  public void setEmailService(EmailService emailService) {
    this.emailService = emailService;
  }
}
```

✔ praktisch für optionale Abhängigkeiten
❌ schwerer zu testen
❌ Abhängigkeiten sind veränderbar

---

### 🔹 **3. Feldinjektion (nicht empfohlen)**

Spring schreibt die Abhängigkeit direkt ins Feld.

```java

@Component
public class ReportService {

  @Autowired
  private EmailService emailService;
}
```

❌ nicht testbar ohne Reflection
❌ keine Immutability
❌ keine Klarheit über Abhängigkeiten
✔ wird noch in Tutorials verwendet, aber in Produktivcode vermeiden

---

### 🔹 Beispielhafte Anwendung in Spring

Wenn du z. B. folgende Beans hast:

```java

@Component
public class EmailService {

}
```

```java

@Component
public class OrderProcessor {

  private final EmailService emailService;

  public OrderProcessor(EmailService emailService) {
    this.emailService = emailService;
  }
}
```

Spring übernimmt dann:

* Erstellen der `EmailService`-Instanz
* Erstellen der `OrderProcessor`-Instanz
* Verbinden der beiden Klassen im Konstruktor

---

### Kurzfazit

> DI kann über Konstruktor, Setter oder Felder erfolgen – aber Konstruktorinjektion ist der saubere,
> moderne Standard.

## ❓ Was ist Autowiring?

Autowiring bedeutet, dass Spring **automatisch entscheidet**, welche Bean einer Abhängigkeit
zugewiesen wird.
Der Entwickler muss keine Instanzen manuell erstellen oder in einer Konfiguration verdrahten —
Spring findet und injiziert passende Beans selbstständig.

Wenn eine Klasse eine Abhängigkeit im Konstruktor, Setter oder Feld deklariert, sucht Spring eine *
*passende Bean** im ApplicationContext und injiziert sie automatisch.

Beispiel:

```java

@Component
public class EmailService {

}
```

```java

@Component
public class OrderService {

  private final EmailService emailService;

  // Spring "verdrahtet" EmailService automatisch
  public OrderService(EmailService emailService) {
    this.emailService = emailService;
  }
}
```

### Wie Spring Autowiring entscheidet

Spring sucht nach einer Bean, die:

* dem benötigten Typ entspricht
* eindeutig ist (nur eine Bean des Typs)

Falls es mehrere passende Beans gibt:

* `@Primary` legt fest, welche bevorzugt wird
* `@Qualifier` legt fest, welche explizit genutzt werden soll

### Kurz zusammengefasst

> Autowiring ist die automatische Zuordnung von Beans durch Spring.
> Spring erkennt den benötigten Typ und liefert passende Instanzen ohne manuelles Verdrahten.

## ❓ Was sind die wichtigen Aufgaben eines IoC-Containers?

Der IoC-Container (in Spring z. B. der `ApplicationContext`) ist das zentrale Element des
Frameworks.
Er übernimmt die Verwaltung aller Objekte (Beans), deren Lebenszyklen und deren Abhängigkeiten.

### Die wichtigsten Aufgaben im Überblick

### **1. Erzeugen und Verwalten von Beans**

Der IoC-Container erstellt Instanzen aller als Beans definierten Klassen und hält sie zentral
verfügbar.

---

### **2. Auflösen und Injizieren von Abhängigkeiten (Dependency Injection)**

Spring erkennt automatisch, welche Bean welche Abhängigkeiten benötigt, und injiziert diese:

* automatisch via Typ
* bevorzugt via Konstruktorinjektion
* Steuerung über `@Primary`, `@Qualifier`

---

### **3. Lebenszyklusverwaltung von Beans**

Der Container verwaltet:

* Erstellung
* Initialisierung (`@PostConstruct`, InitializingBean)
* Zerstörung (`@PreDestroy`, DisposableBean)

Beans folgen festen Lebenszyklusregeln, die Spring kontrolliert.

---

### **4. Component Scanning**

Der Container durchsucht das Projekt (oder definierte Pakete) nach Komponenten:

* `@Component`
* `@Service`
* `@Repository`
* `@Controller`

Gefundene Klassen werden automatisch registriert.

---

### **5. Bereitstellung von Bean Scopes**

Der Container legt fest, wie oft eine Bean instanziert wird — z. B.:

* `singleton` (Standard)
* `prototype`
* `request`
* `session`
* `application`

---

### **6. Event-System**

Spring kann Ereignisse veröffentlichen und verarbeiten:

* `ApplicationEventPublisher`
* Listener mit `@EventListener`

---

### **7. Unterstützung für AOP (Aspektorientierte Programmierung)**

Der Container erstellt Proxies, um Querschnittsfunktionen wie Logging, Sicherheit oder Transaktionen
einzubinden.

---

### **8. Verwaltung der Konfiguration**

Der Container:

* lädt Konfigurationen (Properties, YAML, Environment)
* löst Platzhalter auf (`@Value`, `@ConfigurationProperties`)
* ermöglicht Profile (`@Profile`)

---

### Kurz zusammengefasst

> Der IoC-Container ist das Herz von Spring:
> Er erstellt, verwaltet und verdrahtet Beans, kontrolliert ihren Lebenszyklus, unterstützt AOP,
> löst Konfigurationen auf und bildet damit das gesamte Fundament der Spring-Architektur.

## ❓ Was sind BeanFactory und ApplicationContext?

**BeanFactory**

* Der grundlegende IoC-Container in Spring
* Verwaltet Beans und löst Abhängigkeiten
* Minimaler Funktionsumfang (wird in Spring Boot nicht direkt genutzt)

**ApplicationContext**

* Erweiterter Container, der auf BeanFactory aufbaut
* Unterstützt Component Scan, AOP, Events, Lifecycle-Callbacks
* Wird **immer** von Spring Boot verwendet

**Kurz:**

> ApplicationContext = BeanFactory + alle praktischen Spring-Features.

---

## 🧩 Kleines Codebeispiel (zeigt ApplicationContext-Verhalten)

**SimpleBean.java**

```java

@Component
public class SimpleBean {

  public SimpleBean() {
    System.out.println("SimpleBean erstellt");
  }
}
```

**Main Application**

```java

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(App.class, args);

    // Bean ist zu diesem Zeitpunkt bereits instanziiert worden
    ctx.getBean(SimpleBean.class);
  }
}
```

**Erkenntnis:**
ApplicationContext erstellt Beans beim Start und bietet alle modernen Spring-Funktionen.
BeanFactory wäre die „Low-Level“-Variante ohne diesen Komfort.

## ❓ Can you compare BeanFactory with ApplicationContext?

### **BeanFactory**

* Der grundlegende IoC-Container von Spring
* Stellt Dependency Injection bereit
* Minimaler Funktionsumfang
* Wird in Spring Boot praktisch **nie direkt verwendet**

### **ApplicationContext**

* Baut auf BeanFactory auf und erweitert sie
* Unterstützt Component Scanning, AOP, Events, Internationalisierung, Lifecycle-Callbacks,
  Environment/Profiles
* Ist der **Standardcontainer** in allen modernen Spring-Boot-Anwendungen

### **Kurzfazit**

> **ApplicationContext = BeanFactory + alle komfortablen Spring-Funktionen.**

## ❓ How do you create an application context with Spring?

In modernen Spring-Boot-Anwendungen wird der ApplicationContext automatisch über
`SpringApplication.run()` erzeugt und gestartet.

### **Beispiel**

```java

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(App.class, args);
  }
}
```

Damit wird:

* der ApplicationContext erstellt
* Component Scan ausgeführt
* alle Beans instanziiert
* AOP, Events, Profiles usw. aktiviert

### Kurzfazit

> In Spring Boot entsteht der ApplicationContext automatisch durch `SpringApplication.run()`.
> Eine manuelle Erstellung ist nur in Sonderfällen nötig.

## ❓ How does Spring know where to search for Components or Beans?

Spring durchsucht automatisch **das Paket der Hauptklasse** (mit `@SpringBootApplication`) **und
alle Unterpakete** nach Komponenten wie:

* `@Component`
* `@Service`
* `@Repository`
* `@Controller`

Der Component Scan startet also beim Paket der Anwendungsklasse und geht rekursiv nach unten.

### Beispiel

```java

@SpringBootApplication
public class App {

}
```

Liegt `App` z. B. unter `com.example.interview`, dann scannt Spring automatisch:

```
com.example.interview
com.example.interview.*
```

### Kurzfazit

> Spring findet Komponenten, weil `@SpringBootApplication` automatisch einen Component Scan ab dem
> Basis-Paket ausführt.

## ❓ What is a Component Scan?

Ein *Component Scan* ist der Mechanismus, mit dem Spring **automatisch Klassen findet**, die als
Beans registriert werden sollen.
Spring sucht dabei nach Annotationen wie:

* `@Component`
* `@Service`
* `@Repository`
* `@Controller`

und nimmt diese Klassen in den ApplicationContext auf.

In Spring Boot wird der Component Scan automatisch durch `@SpringBootApplication` aktiviert und
startet im Paket der Hauptklasse.

### Beispiel

```java

@SpringBootApplication  // aktiviert Component Scan ab diesem Paket
public class App {

}
```

### Kurzfazit

> Component Scan bedeutet, dass Spring deine Projektpakete durchsucht und alle annotierten Klassen
> automatisch als Beans registriert.

## ❓ How do you define a component scan in XML and Java Configurations?

Da XML in modernen Spring-Boot-Projekten **nicht mehr verwendet wird**, konzentriert man sich heute
ausschließlich auf **Java-Konfiguration**.

### **Java Configuration (modern, empfohlen)**

```java

@Configuration
@ComponentScan(basePackages = "com.example.project")
public class AppConfig {

}
```

Damit durchsucht Spring das angegebene Paket und erstellt automatisch Beans für alle Klassen mit
`@Component`, `@Service`, `@Controller`, usw.

---

### **Spring Boot Version (noch einfacher)**

`@SpringBootApplication` enthält bereits einen Component Scan:

```java

@SpringBootApplication  // scannt das Paket und alle Unterpakete der Klasse
public class App {

}
```

---

### Kurzfazit

> Früher definierte man Component Scan in XML, modern macht man es über `@ComponentScan` oder
> automatisch über `@SpringBootApplication`.

## ❓ How is it done with Spring Boot?

In Spring Boot wird der Component Scan **automatisch** aktiviert durch
`@SpringBootApplication`.

Die Annotation enthält intern `@ComponentScan` und scannt **das Paket der Hauptklasse und alle
Unterpakete**.

### Beispiel

```java

@SpringBootApplication
public class App {

}
```

Liegt `App` unter `com.example`, dann scannt Spring automatisch:

```
com.example
com.example.*
```

**Du musst nichts manuell konfigurieren.**

### Kurzfazit

> In Spring Boot erfolgt Component Scan automatisch durch `@SpringBootApplication`, ohne zusätzliche
> Konfiguration.

## ❓ What does @Component signify?

`@Component` markiert eine Klasse als **Spring-Bean**, damit sie vom Component Scan automatisch
erkannt und in den ApplicationContext aufgenommen wird.

Mit dieser Annotation sagt man Spring:

> „Erzeuge eine Instanz dieser Klasse und verwalte sie als Bean.“

### Beispiel

```java

@Component
public class EmailService {

}
```

Damit wird `EmailService` automatisch:

* gefunden
* instanziiert
* verwaltet
* für Dependency Injection bereitgestellt

### Kurzfazit

> `@Component` macht eine Klasse zu einer automatisch erkannten und verwalteten Spring-Bean.

## ❓ What does @Autowired signify?

`@Autowired` weist Spring an, eine **Bean automatisch zu injizieren**, die zum benötigten Typ passt.
Damit sagt man Spring:

> „Finde eine geeignete Bean und setze sie hier ein.“

### Beispiel

```java

@Component
public class OrderService {

  @Autowired
  private EmailService emailService;
}
```

Spring sucht nach einer passenden `EmailService`-Bean und füllt das Feld automatisch.

### Wichtiger Hinweis

In modernen Spring-Anwendungen nutzt man bevorzugt **Konstruktorinjektion**, bei der `@Autowired`
nicht einmal notwendig ist:

```java

@Component
public class OrderService {

  private final EmailService emailService;

  public OrderService(EmailService emailService) {
    this.emailService = emailService;
  }
}
```

### Kurzfazit

> `@Autowired` löst Dependency Injection aus, indem Spring automatisch die passende Bean einfügt.

## ❓ Unterschied zwischen @Controller, @Component, @Repository und @Service

Alle vier Annotationen markieren eine Klasse als **Spring-Bean**.
Der Unterschied liegt im **semantischen Zweck** und in einigen **Spezialfunktionen**:

---

### **@Component**

* Allgemeine Annotation für beliebige Beans
* Keine spezielle Funktion
* Basis für alle anderen Stereotypen

**Kurz:** „Diese Klasse ist eine Bean.“

---

### **@Service**

* Für Klassen mit **Business-Logik**
* Rein semantisch (bessere Lesbarkeit, klare Schichtentrennung)
* Technisch identisch zu `@Component`

**Kurz:** „Das ist eine fachliche Service-Klasse.“

---

### **@Repository**

* Für **Datenzugriffsschichten** (DAO, Repositories)
* Spring übersetzt Datenbankfehler automatisch in **DataAccessException**

**Kurz:** „Hier wird auf die Datenbank zugegriffen — inklusive Fehlerübersetzung.“

---

### **@Controller**

* Für **Web-Controller in Spring MVC**
* Kombiniert Bean-Erzeugung mit Web-spezifischen Funktionen
* Methoden liefern Views oder JSON (mit @ResponseBody)

**Kurz:** „Diese Klasse nimmt HTTP-Anfragen entgegen.“

---

## 🧩 Kurze Tabelle

| Annotation    | Zweck             | Besonderheit            |
|---------------|-------------------|-------------------------|
| `@Component`  | allgemeine Bean   | Basis aller Stereotypen |
| `@Service`    | Business-Logik    | rein semantisch         |
| `@Repository` | Datenzugriff      | Exception-Übersetzung   |
| `@Controller` | Web-/HTTP-Schicht | MVC-Funktionen          |

---

## ✔️ Kurzfazit

> Alle sind Beans, aber sie klären die **Rolle** einer Klasse.
> `@Service` für Logik, `@Repository` für DB-Zugriff, `@Controller` für Web,
> `@Component` für alles andere.

## ❓ What is the default scope of a bean?

Der Standard-Scope einer Spring-Bean ist **singleton**.

Das bedeutet:

* Es gibt **genau eine Instanz** der Bean pro ApplicationContext.
* Alle Stellen, die die Bean injizieren, erhalten **dieselbe Instanz**.

### Beispiel

```java

@Component
public class MyService {

}
```

→ Wird automatisch als Singleton erstellt.

### Kurzfazit

> Default-Scope = **singleton**. Es existiert nur eine gemeinsame Instanz pro Anwendungskontext.

## ❓ Are Spring beans thread safe?

**Nein, Spring-Beans sind nicht automatisch thread-sicher.**

Spring garantiert nur, dass eine Bean im Default-Scope **singleton** ist –
aber **Singleton bedeutet nicht Thread-Sicherheit**.

Wenn mehrere Threads (z. B. HTTP-Requests) gleichzeitig auf dieselbe Bean zugreifen, bist du selbst
für Thread-Sicherheit verantwortlich.

### Wann ist das ein Problem?

* Wenn die Bean **zustandsbehaftet** ist (z. B. Felder, die sich ändern)
* Wenn mehrere Requests gleichzeitig dieselben Felder verändern könnten

### Wann ist es unkritisch?

* Wenn die Bean **zustandslos** ist (`stateless`), also nur Methoden ausführt, aber keine
  veränderbaren Instanzfelder besitzt.

### Kurzfazit

> Spring kümmert sich nicht um Thread-Sicherheit.
> Beans im Singleton-Scope müssen **zustandslos** sein oder der Entwickler muss Synchronisation
> selbst sicherstellen.

## ❓ What are the other scopes available?

Neben dem Standard-Scope **singleton** bietet Spring folgende zusätzliche Bean-Scopes:

### **1. prototype**

* Neue Instanz **bei jeder Anforderung**
* Wird nicht durch Spring verwaltet, nachdem sie erzeugt wurde
* Gut für kurzlebige, zustandsbehaftete Objekte

### **2. request** *(nur in Web-Anwendungen)*

* Eine Instanz **pro HTTP-Request**

### **3. session** *(nur in Web-Anwendungen)*

* Eine Instanz **pro HTTP-Session**

### **4. application** *(Web)*

* Eine Instanz **pro ServletContext**

### **5. websocket** *(WebSocket-Kontexte)*

* Eine Instanz pro WebSocket-Session

---

### Kurzfazit

> Neben `singleton` gibt es `prototype`, `request`, `session`, `application` und `websocket` —
> jeweils mit unterschiedlichem Lebenszyklus.

## ❓ How is Spring’s singleton bean different from Gang of Four Singleton Pattern?

### 🧩 Gang of Four Singleton (GoF)

* Wird **in der Klasse selbst** implementiert
* Nutzt meist `private` Konstruktor + `static getInstance()`
* Es gibt **eine Instanz pro Classloader**
* Du bist selbst für Thread-Sicherheit verantwortlich
* Stark gekoppelt, schwer testbar (statische Zugriffe)

**Beispiel (vereinfacht):**

```java
public class GoFSingleton {

  private static final GoFSingleton INSTANCE = new GoFSingleton();

  private GoFSingleton() {
  }

  public static GoFSingleton getInstance() {
    return INSTANCE;
  }
}
```

---

### 🧩 Spring-Singleton

* **Kein** spezielles Pattern im Code nötig
* Bean ist einfach eine normale Klasse, z. B.:

```java

@Component
public class MyService {

}
```

* Spring erstellt **eine Instanz pro ApplicationContext**
* Du kannst mehrere ApplicationContexts haben → mehrere „Singletons“
* Einfach zu testen (kein statischer Zugriff, Mocking möglich)
* Scope kann leicht geändert werden (`@Scope("prototype")` etc.)

---

### Kurzfazit

> GoF-Singleton ist ein **statisch implementiertes** Pattern in der Klasse.
> Spring-Singleton ist eine **Container-Entscheidung**: eine Instanz pro ApplicationContext, ohne
> spezielles Pattern im Code und besser testbar.

## ❓ What are the different types of dependency injections?

Spring unterstützt drei Arten von Dependency Injection:

### **1. Konstruktorinjektion (empfohlen)**

Abhängigkeiten werden über den Konstruktor bereitgestellt.

```java

@Component
public class OrderService {

  private final EmailService emailService;

  public OrderService(EmailService emailService) {
    this.emailService = emailService;
  }
}
```

✔ klare Abhängigkeiten
✔ immutable
✔ ideal für Tests
✔ Standard in modernen Spring-Boot-Projekten

---

### **2. Setter-Injektion**

Abhängigkeiten werden über Setter-Methoden gesetzt.

```java

@Autowired
public void setEmailService(EmailService emailService) {
  this.emailService = emailService;
}
```

Geeignet für **optionale** Abhängigkeiten.

---

### **3. Feldinjektion** *(nicht empfohlen)*

Spring setzt die Abhängigkeit direkt ins Feld.

```java

@Autowired
private EmailService emailService;
```

Einfach, aber:
❌ schlecht testbar
❌ keine Immutability
❌ Abhängigkeiten sind versteckt

---

### Kurzfazit

> Spring bietet Konstruktor-, Setter- und Feldinjektion an.
> Konstruktorinjektion ist heute der Standard und die beste Wahl.

## ❓ How do you choose between setter and constructor injections?

### **Konstruktorinjektion (Standard, empfohlen)**

Wähle Konstruktorinjektion, wenn eine Abhängigkeit **zwingend erforderlich** ist.

**Warum?**

* Abhängigkeiten sind **klar sichtbar**
* Objekt ist nach Konstruktion **vollständig**
* Beans können **immutable** sein
* Ideal für Tests
* Spring kann Fehler früh erkennen (fehlende Abhängigkeit)

---

### **Setter-Injektion**

Wähle Setter-Injektion, wenn eine Abhängigkeit **optional** ist oder sich **ändern** können soll.

**Warum?**

* Setzen nach der Konstruktion möglich
* Optional (`@Autowired(required = false)`)
* Flexibel, aber weniger sicher

---

### Kurzfazit

> **Konstruktorinjektion** für notwendige Abhängigkeiten (Standard).
> **Setter-Injektion** nur für optionale oder austauschbare Abhängigkeiten.

## ❓ What are the different options available to create Application Contexts for Spring?

In modernen Spring-Anwendungen gibt es im Wesentlichen **drei** Möglichkeiten, einen
ApplicationContext zu erstellen:

### **1. Spring Boot (Standard)**

Automatisch über:

```java
SpringApplication.run(App .class, args);
```

→ Erstellt einen `ApplicationContext`, führt Component Scan aus und startet die App.
→ **Der heute übliche Weg.**

---

### **2. AnnotationConfigApplicationContext**

Manuell einen Kontext aus Java-Konfigurationen erzeugen.

```java
var ctx = new AnnotationConfigApplicationContext(AppConfig.class);
```

Geeignet für Tests oder kleine, nicht-Boot-basierte Setups.

---

### **3. AnnotationConfigWebApplicationContext**

Kontext für klassische Spring-MVC-Webanwendungen (selten in Spring Boot).

---

### Kurzfazit

> In Spring Boot entsteht der ApplicationContext fast immer automatisch durch
`SpringApplication.run()`.
> Manuelle Varianten wie `AnnotationConfigApplicationContext` nutzt man hauptsächlich für Tests oder
> spezielle Setups.

## ❓ How does Spring do Autowiring?

Spring führt Autowiring durch, indem es **den benötigten Typ** einer Abhängigkeit analysiert und
dann **automatisch eine passende Bean** aus dem ApplicationContext auswählt und injiziert.

### Wie funktioniert das?

1. Spring sieht eine Abhängigkeit (z. B. im Konstruktor).
2. Es sucht im ApplicationContext eine Bean **passenden Typs**.
3. Wenn genau eine gefunden wird → sie wird injiziert.
4. Wenn mehrere gefunden werden → Spring nutzt

    * `@Primary` oder
    * `@Qualifier`, um eindeutig zu entscheiden.

---

### Minimalbeispiel (Konstruktorinjektion)

```java

@Component
public class EmailService {

}

@Component
public class OrderService {

  private final EmailService emailService;

  public OrderService(EmailService emailService) { // Autowiring durch Typ
    this.emailService = emailService;
  }
}
```

Spring erkennt automatisch:

* „OrderService braucht ein EmailService“
* „Ich habe genau eine EmailService-Bean“
* → wird automatisch verdrahtet (autowired)

---

### Kurzfazit

> Spring autowiret Beans **durch Typ**.
> Wird mehr als eine passende Bean gefunden, helfen `@Primary` oder `@Qualifier`.

## ❓ What are the different kinds of matching used by Spring for Autowiring?

Spring verwendet beim Autowiring **verschiedene Matching-Strategien**, um herauszufinden, welche
Bean injiziert werden soll:

### **1. Matching by Type (Standard)**

Spring sucht eine Bean, deren **Typ** zum benötigten Parameter passt.

```java
public OrderService(EmailService emailService) {
}
```

→ Spring sucht eine `EmailService`-Bean.

---

### **2. Matching by Qualifier**

Wenn mehrere Beans desselben Typs existieren, entscheidet ein **@Qualifier**, welche verwendet wird.

```java
public OrderService(@Qualifier("smtp") EmailService emailService) {
}
```

---

### **3. Matching by Primary**

Wenn mehrere Beans passen, wird die mit **@Primary** bevorzugt.

```java

@Primary
@Component
public class DefaultEmailService implements EmailService {

}
```

---

### **4. Matching by Name (nur wenn Typ nicht eindeutig ist)**

Wenn mehrere Beans denselben Typ haben, versucht Spring zusätzlich den **Bean-Namen** abzugleichen.

Parametername:

```java
public OrderService(EmailService smtpEmailService) {
}
```

Bean-Name:

```java

@Component("smtpEmailService")
public class SmtpEmailService implements EmailService {

}
```

Wenn Typ und Name übereinstimmen → wird injiziert.

---

### Kurzfazit

> Spring verdrahtet zuerst **nach Typ**, dann — wenn nötig — nach **Qualifier**, **Primary** oder *
*Name**.

## ❓ How do you debug problems with Spring Framework?

Typische Probleme in Spring hängen meist mit **Bean-Erzeugung**, **Autowiring**, **Component Scan**
oder **Konfiguration** zusammen.
Die wichtigsten Debug-Methoden:

### **1. Enable Debug Logging**

Spring Boot lässt sich mit einem Debug-Flag starten:

```
--debug
```

oder in `application.properties`:

```properties
debug=true
```

→ Zeigt detaillierte Infos über Autoconfiguration und Bean-Erstellung.

---

### **2. Show the Condition Evaluation Report**

Im Spring Boot Startup-Log erscheint dann:

```
CONDITIONS EVALUATION REPORT
```

→ Sehr hilfreich, um zu verstehen, warum Beans erstellt wurden oder nicht.

---

### **3. Check Component Scan**

Sicherstellen, dass die Hauptklasse im **obersten Paket** liegt.

Wenn Spring eine Bean nicht findet, liegt es oft daran, dass:

* die Klasse nicht im scan-relevanten Paket liegt
* die Annotation fehlt (`@Component`, `@Service`, …)

---

### **4. Verify Constructor Injection**

Fehler wie „NoSuchBeanDefinition“ kommen oft von:

* falschen Typen
* fehlenden Implementierungen
* mehreren Implementierungen ohne `@Qualifier`

---

### **5. Use the Actuator Bean Endpoint**

Aktiviere Actuator:

```xml

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Dann:

```
/actuator/beans
```

→ Zeigt alle registrierten Beans und deren Abhängigkeiten.

---

### **6. Add Breakpoints and Inspect the ApplicationContext**

In IntelliJ kannst du:

```java
ApplicationContext ctx = ...
```

→ inspizieren und alle Beans durchsehen.

---

### **7. Check Stacktraces for Root Causes**

Spring-Fehler enthalten oft viele verschachtelte Exceptions.
Der **erste** „Caused by“ ist fast immer der entscheidende Hinweis.

---

### Kurzfazit

> Debugging in Spring = Debug Log aktivieren, Component Scan prüfen, Bean-Definitionen analysieren
> und Constructor-Injection überprüfen. Actuator kann zusätzlich bei Bean-Problemen helfen.

## ❓ How do you solve NoUniqueBeanDefinitionException?

Diese Exception tritt auf, wenn Spring **mehrere Beans desselben Typs** findet, aber **nicht weiß,
welche injiziert werden soll**.

Typische Lösungen:

---

### **1. Typ eindeutig machen mit @Primary**

Eine Bean als Standard markieren:

```java

@Primary
@Component
public class DefaultEmailService implements EmailService {

}
```

---

### **2. Bean explizit wählen mit @Qualifier**

Bei der Injection eine konkrete Bean benennen:

```java

@Component("smtpEmailService")
public class SmtpEmailService implements EmailService {

}

@Component
public class OrderService {

  private final EmailService emailService;

  public OrderService(@Qualifier("smtpEmailService") EmailService emailService) {
    this.emailService = emailService;
  }
}
```

---

### **3. Alle Beans injizieren (Liste oder Map)**

Wenn du bewusst mehrere Implementierungen nutzen willst:

```java
public NotificationManager(List<NotificationChannel> channels) {
}
```

---

### Kurzfazit

> NoUniqueBeanDefinitionException löst du durch **@Primary**, **@Qualifier**, Entfernen unnötiger
> Beans oder bewusste Sammlung aller Beans (Liste/Map).

## ❓ How do you solve NoSuchBeanDefinitionException?

Diese Exception tritt auf, wenn Spring eine Bean **nicht finden kann**, obwohl sie benötigt wird.
Typische Ursachen und Lösungen:

---

### **1. Bean ist nicht im Component Scan**

Die Klasse liegt außerhalb des Pakets, das Spring scannt.

**Lösung:**
Stelle sicher, dass deine Hauptklasse im **obersten Paket** liegt oder definiere ein eigenes
Scan-Paket:

```java
@SpringBootApplication(scanBasePackages = "com.example")
```

---

### **2. Bean ist nicht als Component annotiert**

Fehlende Annotation wie `@Component`, `@Service`, `@Repository`, `@Controller`.

**Lösung:**
Annotation hinzufügen.

---

### **3. Falscher Bean-Typ / falsches Interface**

Spring findet die Bean, aber sie passt **nicht zum erwarteten Typ**.

**Lösung:**
Typkorrektur oder Interface implementieren.

---

### **4. Konstruktor erwartet eine Bean, die nicht existiert**

Häufig bei Refactoring oder bei fehlerhaftem DI.

**Lösung:**
Passende Implementierung erstellen *oder* Injection optional machen:

```java
@Autowired(required = false)
```

---

### **5. Bean ist Scope-abhängig und zum Zeitpunkt der Anfrage nicht verfügbar**

Bsp.: request- oder session-scope außerhalb eines Web-Kontexts.

**Lösung:**
Scope prüfen oder `@Lazy` verwenden.

---

### Kurzfazit

> NoSuchBeanDefinitionException bedeutet: Spring konnte die erwartete Bean **nicht finden**.
> Lösung: Component Scan prüfen, Annotationen ergänzen, Typ korrigieren oder fehlende
> Implementierung anlegen.

## ❓ What is @Primary?

`@Primary` markiert eine Bean als **Standard-Bean**, wenn mehrere Beans desselben Typs existieren.
Spring verwendet dann automatisch diese Bean, sofern kein `@Qualifier` angegeben ist.

### Kurzfazit

> `@Primary` löst Konflikte bei mehreren Beans desselben Typs, indem es eine bevorzugte Bean
> festlegt.

## ❓ What is @Qualifier?

`@Qualifier` wird verwendet, um **bei mehreren Beans desselben Typs genau festzulegen**, welche Bean
injiziert werden soll.

### Kurzfazit

> `@Qualifier` macht die Injection eindeutig, wenn es mehrere passende Beans gibt.

## ❓ What is CDI (Contexts and Dependency Injection)?

CDI ist der **standardisierte Dependency-Injection- und Kontext-Management-Mechanismus** in Java
EE/Jakarta EE.
Es bietet ähnliche Funktionen wie Spring DI, aber basierend auf Java-Standards.

### Kurzfazit

> CDI ist der offizielle DI-Standard in Jakarta EE, vergleichbar mit Spring Dependency Injection.

## ❓ Does Spring Support CDI?

Ja. Spring unterstützt zentrale CDI-Konzepte und akzeptiert viele CDI-Annotationen wie `@Inject` und
`@Named`, obwohl Spring sein eigenes DI-Modell verwendet.

### Kurzfazit

> Spring ist kompatibel mit CDI-Annotationen, nutzt intern aber sein eigenes DI-System.

## ❓ Would you recommend to use CDI or Spring Annotations?

In Spring-Anwendungen sollte man **Spring-Annotationen** verwenden.
Sie sind besser integriert, vollständig unterstützt, bieten mehr Funktionen und sind der Standard in
Spring Boot.

### Kurzfazit

> In Spring-Projekten immer Spring-Annotationen nutzen — CDI nur in reinen Jakarta-EE-Umgebungen.

## ❓ What are the major features in different versions of Spring?

### **Spring 3.x**

* Einführung von **Java-basierten Konfigurationen** (`@Configuration`, `@Bean`)
* Verbesserte REST-Unterstützung in Spring MVC

---

### **Spring 4.x**

* Unterstützung für **Java 8** (Lambdas, Streams, Optional)
* Verbesserte WebSocket- und Async-Unterstützung

---

### **Spring 5.x**

* Einführung von **Spring WebFlux** (reaktiv, non-blocking)
* Unterstützung für **Java 9+**, **Kotlin**, Functional Bean Registration
* Modernere Basistechnologien (Netty, Reactor)

---

### **Spring Boot 2.x / Spring Framework 5.x**

* Actuator stark erweitert
* AutoConfiguration verbessert
* WebFlux stabilisiert

---

### **Spring Framework 6 / Spring Boot 3 (neueste Generation)**

* **Java 17+ Pflicht**
* **Jakarta EE** statt javax
* Vollständige Unterstützung für **Native Images (GraalVM)**
* Verbesserte Observability (Micrometer, OpenTelemetry)
* Moderne AOT-Optimierungen

---

### Kurzfazit

> Jede Version modernisiert Kernmodule, hebt Java-Versionen an und erweitert Web-, DI- und
> Config-Funktionalität — Spring Boot 3/Spring 6 markiert den großen Sprung zu Java 17+, AOT und
> Jakarta EE.

## ❓ What are important Spring Modules?

Spring besteht aus mehreren Kernmodulen, die verschiedene Aufgaben abdecken:

### **1. Core Container**

* **spring-core, spring-beans, spring-context**
  → Basis für IoC, Dependency Injection, ApplicationContext

### **2. Spring AOP**

* Unterstützung für aspektorientierte Programmierung (z. B. Logging, Transactions)

### **3. Spring Data Access / Integration**

* **spring-jdbc**, **spring-orm**, **spring-tx**
  → Zugriff auf Datenbanken, Transaktionsmanagement

### **4. Spring MVC / Web**

* **spring-web**, **spring-webmvc**
  → REST-APIs, Web-Anwendungen, HTTP-Handling

### **5. Spring WebFlux**

* Reaktives Webframework (non-blocking)

### **6. Spring Security**

* Authentifizierung, Autorisierung

### **7. Spring Test**

* Unterstützung für Unit- und Integrationstests

---

### Kurzfazit

> Die wichtigsten Spring-Module sind Core, AOP, Data, MVC/Web, WebFlux, Security und Test — sie
> bilden das Fundament moderner Spring-Anwendungen.

## ❓ What are important Spring Projects?

Neben dem Spring Framework selbst gibt es mehrere eigenständige Spring-Projekte:

### **1. Spring Boot**

Erleichtert den Einstieg, Auto-Configuration, eingebettete Server, vereinfachtes Build.

### **2. Spring Data**

Vereinheitlichte Datenzugriffe (JPA, Mongo, Redis, Elasticsearch usw.).

### **3. Spring Security**

Authentifizierung, Autorisierung, OAuth2, JWT.

### **4. Spring Cloud**

Tools für verteilte Systeme: Config Server, Discovery, Circuit Breaker, Gateway.

### **5. Spring Batch**

Batch-Verarbeitung großer Datenmengen.

### **6. Spring Integration**

Enterprise-Integration-Patterns (Messaging, Channels, Adapter).

### **7. Spring WebFlux**

Reaktives, non-blocking Webframework.

### **8. Spring HATEOAS / Spring REST Docs**

Unterstützung für hypermedia und API-Dokumentation.

---

### Kurzfazit

> Wichtige Spring-Projekte: **Boot, Data, Security, Cloud, Batch, Integration, WebFlux** — sie
> erweitern das Spring-Framework je nach Anwendungsfall.

## ❓ What is the simplest way of ensuring that we are using a single version of all Spring-related dependencies?

In einem Maven-Projekt nutzt man einfach den **Spring Boot Starter Parent**.
Er definiert eine zentrale, konsistente Version aller Spring-Abhängigkeiten.

### Beispiel (pom.xml)

```xml

<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>3.2.0</version>
</parent>
```

Damit stellt Spring Boot sicher, dass **alle Spring-Module zueinander passen**, ohne dass man
Versionen einzeln festlegen muss.

### Kurzfazit

> Der einfachste Weg: **Spring Boot Starter Parent** verwenden — er verwaltet alle Spring-Versionen
> automatisch und konsistent.

## ❓ Name some of the design patterns used in Spring Framework

Spring verwendet mehrere bekannte Design Patterns, u. a.:

* **Singleton** – Beans im Default-Scope sind Singleton pro ApplicationContext
* **Factory Pattern** – BeanFactory und ApplicationContext erzeugen Objekte
* **Proxy Pattern** – für AOP, Transactions, Security
* **Template Method** – z. B. in `JdbcTemplate`, `RestTemplate`
* **Dependency Injection (DI)** – zentrale Grundlage des Frameworks
* **Model–View–Controller (MVC)** – in Spring Web MVC
* **Front Controller** – der `DispatcherServlet`

### Kurzfazit

> Spring baut auf klassischen Patterns wie Singleton, Factory, Proxy, DI, Template Method und MVC
> auf.

## ❓ What do you think about Spring Framework?

Spring Framework ist ein **mächtiges, ausgereiftes und flexibel einsetzbares** Java-Framework für
Unternehmensanwendungen.
Es bietet:

* ein starkes IoC/DI-Modell
* konsistente Programmiermodelle für Web, Datenzugriff und Integration
* klare Modularität
* eine große Community und ausgezeichnete Dokumentation

In Kombination mit Spring Boot wird Entwicklung deutlich **einfacher, schneller und produktiver**,
weil Konfiguration, Server-Setup und Dependency-Management weitgehend automatisiert sind.

### Kurzfazit

> Spring ist ein robustes, modernes Framework, das saubere Architektur, Testbarkeit und schnelle
> Entwicklung unterstützt.

## ❓ Why is Spring Popular?

Spring ist beliebt, weil es:

* **leichtgewichtige und flexible Architektur** bietet
* **starke Dependency Injection** ermöglicht
* **klar strukturierte Programmiermodelle** für Web, Datenzugriff, Security und Integration liefert
* mit **Spring Boot** Entwicklung massiv vereinfacht (Auto-Config, eingebettete Server, Starter)
* eine **riesige Community**, gute Dokumentation und viele Erweiterungsprojekte besitzt
* sich gut für **Microservices**, **Cloud-Anwendungen** und **moderne Architekturen** eignet

### Kurzfazit

> Spring ist populär, weil es flexibel, produktiv, modular und für eine Vielzahl moderner
> Anwendungstypen geeignet ist.

## ❓ Can you give a big picture of the Spring Framework?

Spring Framework ist ein **modulares Ökosystem** für die Entwicklung moderner Java-Anwendungen.
Es besteht im Kern aus:

### **1. Core & IoC**

* Herzstück von Spring
* Dependency Injection, Bean-Lifecycle, ApplicationContext

### **2. Web**

* Spring MVC für klassische Web-/REST-Anwendungen
* Spring WebFlux für reaktive, non-blocking Anwendungen

### **3. Data**

* Einheitlicher Datenzugriff mit Spring Data (JPA, Mongo, Redis usw.)
* Transaktionsmanagement

### **4. AOP**

* Querschnittsfunktionen wie Logging, Security, Transactions per Proxy

### **5. Security**

* Vollständiges Framework für Authentifizierung, Autorisierung, OAuth2

### **6. Integration & Messaging**

* Spring Integration, Spring Cloud, AMQP, Kafka, Eventing

### **7. Spring Boot**

* Stellt alles obige „out of the box“ bereit
* Auto-Configuration, Starter Dependencies, eingebettete Server, Actuator

### Kurzfazit

> Spring ist ein vollständiges, modulares Framework, das Web, Daten, Security, Integration und DI
> vereint — und Spring Boot macht es extrem einfach nutzbar.

## ❓ Can you show an example controller method in Spring MVC?

Ein Spring-MVC-Controller verarbeitet HTTP-Anfragen und gibt eine Antwort zurück.
In Spring Boot sieht eine typische Methode so aus:

### Beispielcontroller

```java

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  @GetMapping("/{id}")
  public String getOrder(@PathVariable Long id) {
    return "Order with id " + id;
  }
}
```

### Was passiert hier?

* `@RestController` → Klasse ist ein Web-Controller, Rückgabewerte werden direkt als HTTP-Response
  gesendet
* `@RequestMapping` → Basis-URL für alle Methoden
* `@GetMapping("/{id}")` → verarbeitet GET-Anfrage `/api/orders/123`
* `@PathVariable` → extrahiert `{id}` aus der URL

### Kurzfazit

> Ein Spring-MVC-Controller besteht aus einer Klasse mit `@RestController` und Methoden, die über
`@GetMapping`, `@PostMapping` usw. HTTP-Anfragen verarbeiten.

## ❓ Can you explain a simple flow in Spring MVC?

Ein typischer Request durchläuft in Spring MVC folgenden Ablauf:

1. **HTTP-Request trifft ein**
   Der Request geht an die Anwendung (z. B. `/api/orders/10`).

2. **DispatcherServlet empfängt den Request**
   Das zentrale Front Controller Servlet von Spring.

3. **Handler Mapping findet den passenden Controller**
   Spring sucht eine Methode mit z. B. `@GetMapping("/api/orders/{id}")`.

4. **Controller-Methode wird ausgeführt**
   Parameter wie `@PathVariable`, `@RequestBody`, etc. werden gebunden.

5. **Rückgabewert wird verarbeitet**

    * Bei `@RestController`: direkt in JSON/HTTP-Response
    * Bei klassischen MVC-Views: über ViewResolver umgesetzt

6. **DispatcherServlet sendet die HTTP-Response zurück**

### Kurzfazit

> Spring MVC nutzt das DispatcherServlet als Front Controller, das Requests entgegennimmt, passende
> Controller findet, deren Methoden ausführt und anschließend die Antwort zurückgibt.

## ❓ What is a ViewResolver?

Ein **ViewResolver** entscheidet, **welche View** (z. B. HTML-Template, JSP, Thymeleaf-Seite) für
den Rückgabewert eines Controllers verwendet wird.

Er wandelt einen **View-Namen** wie `"home"` in eine **konkrete Datei** wie z. B.
`/templates/home.html` um.

### Beispiel (klassisches MVC)

```java

@GetMapping("/home")
public String home() {
  return "home";  // ViewResolver findet passende View-Datei
}
```

In REST-Controllern (`@RestController`) wird **kein** ViewResolver verwendet, weil die Rückgabe
direkt als JSON erfolgt.

### Kurzfazit

> ViewResolver mappt einen logischen View-Namen auf eine konkrete View-Datei.

## ❓ What is Model?

In Spring MVC ist das **Model** ein Datencontainer, der Werte vom Controller an die View (z. B.
HTML-Template) übergibt.
Es enthält Attribute, die in der View dargestellt werden sollen.

### Beispiel

```java

@GetMapping("/hello")
public String hello(Model model) {
  model.addAttribute("name", "Jens");
  return "hello"; // View bekommt das Model
}
```

In der View kannst du dann auf `name` zugreifen.

### Kurzfazit

> Das Model trägt Daten vom Controller zur View und macht sie dort verfügbar.

## ❓ What is ModelAndView?

`ModelAndView` kombiniert **Model-Daten** und den **View-Namen** in einem einzigen Rückgabeobjekt.
Es wird in klassischem Spring MVC (nicht bei `@RestController`) verwendet, um sowohl die View als
auch die anzuzeigenden Daten zu definieren.

### Beispiel

```java

@GetMapping("/greet")
public ModelAndView greet() {
  ModelAndView mv = new ModelAndView("greeting"); // View-Name
  mv.addObject("message", "Hello World");         // Model-Daten
  return mv;
}
```

Die View `greeting` erhält das Model-Attribut `message`.

### Kurzfazit

> `ModelAndView` transportiert **View + Model** gemeinsam vom Controller zur View.

## ❓ What is a RequestMapping?

`@RequestMapping` legt fest, **welche URL** und **welche HTTP-Methode** eine Controller-Methode in
Spring MVC verarbeitet.
Es ist die Grundannotation für Routing in Spring.

Beispiele:

```java

@RequestMapping("/users")          // URL
public class UserController {

}

@RequestMapping(method = RequestMethod.GET, value = "/{id}")
public String getUser(@PathVariable Long id) { ...}
```

In modernen Spring-Boot-Anwendungen verwendet man meist die Kurzformen:

* `@GetMapping`
* `@PostMapping`
* `@PutMapping`
* `@DeleteMapping`

### Kurzfazit

> `@RequestMapping` verbindet URLs und HTTP-Methoden mit Controllermethoden und dient als Basis
> aller Mapping-Annotationen.

## ❓ What is DispatcherServlet?

Das **DispatcherServlet** ist der **Front Controller** in Spring MVC.
Es empfängt **alle HTTP-Requests**, entscheidet, welcher Controller sie verarbeiten soll, und
kümmert sich um die gesamte Request-/Response-Verarbeitung.

### Aufgaben:

* Request entgegennehmen
* passenden Controller und Handler-Methode finden
* Parameter binden (`@PathVariable`, `@RequestBody`, …)
* Controller-Methode ausführen
* View auswählen oder JSON erzeugen
* HTTP-Response zurücksenden

### Kurzfazit

> Das DispatcherServlet ist das zentrale Servlet von Spring MVC, das jeden Request verarbeitet und
> den passenden Controller aufruft.

## ❓ How do you set up Dispatcher Servlet?

In **Spring Boot** musst du das DispatcherServlet **nicht selbst einrichten**.
Es wird **automatisch** über `@SpringBootApplication` konfiguriert.

Spring Boot:

* registriert das `DispatcherServlet`
* mapped es standardmäßig auf `/`
* aktiviert automatische Handler-Mappings
* startet sofort Spring MVC

### Beispiel (alles automatisch):

```java

@SpringBootApplication
public class App {

}
```

Sobald du einen Controller definierst, arbeitet das DispatcherServlet:

```java

@RestController
public class HelloController {

  @GetMapping("/hello")
  public String hello() {
    return "Hi";
  }
}
```

### Kurzfazit

> In Spring Boot wird das DispatcherServlet **automatisch eingerichtet** – keine manuelle
> Konfiguration nötig.

## ❓ What is a form backing object?

Ein **form backing object** ist ein ganz normales Java-Objekt (DTO), das in Spring MVC verwendet
wird, um **Formulardaten aus einer HTML-Form** zu binden.

Es repräsentiert also die **Daten, die ein Benutzer im Formular eingibt**, und wird vom Controller
als Parameter entgegengenommen.

### Beispiel

```java
public class LoginForm {

  private String username;
  private String password;
  // Getter/Setter
}
```

```java

@PostMapping("/login")
public String submit(@ModelAttribute LoginForm form) {
  // form enthält die Formulardaten
  return "success";
}
```

### Kurzfazit

> Ein form backing object ist ein Java-Objekt, das die Daten einer HTML-Form repräsentiert und
> automatisch von Spring MVC befüllt wird.

## ❓ How is validation done using Spring MVC?

Spring MVC führt Validierung durch, indem man:

1. **Bean Validation Annotationen** (JSR-380) am DTO nutzt
2. im Controller `@Valid` (oder `@Validated`) anwendet
3. optional `BindingResult` auswertet, um Fehler abzufangen

### Beispiel

```java
public class UserForm {

  @NotBlank
  private String name;

  @Min(18)
  private int age;
  // Getter/Setter
}
```

```java

@PostMapping("/users")
public String createUser(@Valid @ModelAttribute UserForm form,
    BindingResult result) {
  if (result.hasErrors()) {
    return "form"; // Fehler erneut anzeigen
  }
  return "success";
}
```

Spring prüft automatisch alle JSR-380-Annotationen und trägt Fehler ins `BindingResult` ein.

### Kurzfazit

> Validierung erfolgt über Bean Validation (z. B. `@NotBlank`, `@Min`) und wird durch `@Valid` im
> Controller ausgelöst; Fehler landen im `BindingResult`.

## ❓ What is BindingResult?

`BindingResult` ist ein Objekt, das **Validierungs- und Bindungsfehler** enthält, die beim Binden
von Formulardaten an ein Objekt auftreten können.

Es steht **direkt nach** dem zu validierenden Objekt im Controller und ermöglicht dir zu prüfen, ob
Fehler vorliegen.

### Beispiel

```java

@PostMapping("/register")
public String register(@Valid UserForm form, BindingResult result) {
  if (result.hasErrors()) {
    return "form"; // Validierungsfehler erneut anzeigen
  }
  return "success";
}
```

### Kurzfazit

> `BindingResult` enthält alle Fehler, die bei der Datenbindung oder Validierung auftreten, und
> ermöglicht deren Auswertung im Controller.

## ❓ How do you map validation results to your view?

In Spring MVC werden Validierungsfehler automatisch in das **Model** übernommen, wenn du:

1. das Formular-Objekt mit `@Valid` annotierst
2. ein **BindingResult** direkt dahinter platzierst
3. bei Fehlern die gleiche View zurückgibst

Die View (z. B. Thymeleaf) kann dann über die Fehler im Model zugreifen.

### Beispiel

```java

@PostMapping("/register")
public String register(@Valid UserForm form, BindingResult result) {
  if (result.hasErrors()) {
    return "form"; // Fehler liegen jetzt im Model
  }
  return "success";
}
```

### In der View (Thymeleaf Beispiel)

```html
<span th:if="${#fields.hasErrors('name')}"
      th:errors="*{name}"></span>
```

Spring füllt automatisch:

* Fehler für jedes Feld
* globale Fehler
* gebundene Formulardaten

### Kurzfazit

> Validierungsfehler landen automatisch im Model, wenn man `@Valid` + `BindingResult` nutzt. Die
> View kann sie direkt anzeigen (z. B. mit Thymeleaf `th:errors`).

## ❓ What are Spring Form Tags?

Spring Form Tags sind spezielle JSP-Tags (z. B. `<form:form>`, `<form:input>`, `<form:errors>`), die
das **Binden von HTML-Formularen an Java-Objekte** erleichtern.
Sie wurden hauptsächlich in klassischen Spring-MVC-Anwendungen mit JSP genutzt.

Sie unterstützen:

* automatische Bindung von Formulardaten an ein Model-Objekt
* Anzeige von Validierungsfehlern
* einfache Erstellung von Formularfeldern

### Beispiel (klassisches JSP)

```jsp
<form:form modelAttribute="user">
    <form:input path="name"/>
    <form:errors path="name"/>
</form:form>
```

### Kurzfazit

> Spring Form Tags sind JSP-Hilfstags für Formularbindung und Validierungsanzeige — heute weniger
> verbreitet, da moderne Projekte meist Thymeleaf oder REST verwenden.

## ❓ What is a Path Variable?

Ein **Path Variable** ist ein Platzhalter in der URL, dessen Wert aus dem Pfad extrahiert und als
Methodenparameter in den Controller übergeben wird.

### Beispiel

```java

@GetMapping("/users/{id}")
public String getUser(@PathVariable Long id) {
  return "User: " + id;
}
```

`{id}` wird aus der URL (`/users/5`) gelesen und Spring setzt automatisch `id = 5`.

### Kurzfazit

> Ein Path Variable ist ein Wert in der URL, der direkt an die Controller-Methode gebunden wird.

## ❓ What is a Model Attribute?

Ein **Model Attribute** ist ein Objekt, das Spring MVC dem Model hinzufügt, damit es in der View
verfügbar ist.
Mit `@ModelAttribute` kannst du:

1. **Form-Objekte binden**
2. **Model-Daten vor jedem Request bereitstellen**
3. **Objekte an Views übergeben**

### Beispiel 1: Form-Daten binden

```java

@PostMapping("/save")
public String save(@ModelAttribute UserForm form) {
  // form wurde automatisch aus Request-Daten befüllt
  return "success";
}
```

### Beispiel 2: Daten vorab ins Model legen

```java

@ModelAttribute("roles")
public List<String> roles() {
  return List.of("ADMIN", "USER");
}
```

→ `roles` steht jeder View zur Verfügung.

### Kurzfazit

> `@ModelAttribute` bindet Request-Daten an Objekte oder stellt Model-Daten für Views bereit.

## ❓ What is a Session Attribute?

Ein **Session Attribute** ist ein Model-Attribut, das Spring MVC **in der HTTP-Session speichert**,
sodass es **über mehrere Requests hinweg** verfügbar bleibt.

Dazu nutzt man `@SessionAttributes` auf Controller-Ebene.

### Beispiel

```java

@Controller
@SessionAttributes("cart")
public class CartController {

  @ModelAttribute("cart")
  public Cart initCart() {
    return new Cart();
  }
}
```

→ Das Attribut **cart** wird in der Session gehalten und bleibt zwischen mehreren Requests bestehen.

### Kurzfazit

> Session Attributes sind Model-Daten, die Spring automatisch in der HTTP-Session speichert, damit
> sie mehrere Requests überdauern.

## ❓ What is an init binder?

Ein **Init Binder** ist eine Methode im Controller, die mit `@InitBinder` annotiert ist und Spring
MVC erlaubt, **die Bindung von Request-Daten an Objekte anzupassen**.
Damit kann man z. B. eigene Formatter, Validatoren oder Datumsformate registrieren.

### Beispiel

```java

@InitBinder
public void initBinder(WebDataBinder binder) {
  binder.registerCustomEditor(LocalDate.class, new LocalDateEditor());
}
```

→ Wird vor jeder Datenbindung aufgerufen.

### Kurzfazit

> `@InitBinder` ermöglicht die Anpassung der Datenbindung (z. B. Formate, Validatoren) für
> Controller-Methoden.

## ❓ How do you set default date format with Spring?

Du kannst das Standard-Datumsformat einstellen, indem du in einem Controller oder globalen
Config-Klasse einen **Init Binder** definierst, der einen passenden Formatter/Editor registriert.

### Beispiel (Controller-spezifisch)

```java

@InitBinder
public void initBinder(WebDataBinder binder) {
  binder.registerCustomEditor(LocalDate.class,
      new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false));
}
```

### Beispiel (global über Converter & @Configuration)

```java

@Configuration
public class DateConfig {

  @Bean
  public Converter<String, LocalDate> stringToLocalDate() {
    return LocalDate::parse; // erwartet ISO-Format yyyy-MM-dd
  }
}
```

### Kurzfazit

> Standard-Datumsformate setzt man über `@InitBinder` oder globale Converter/Formatter.

## ❓ How do you implement common logic for controllers in Spring MVC?

Gemeinsame Controller-Logik wird in Spring MVC typischerweise über **@ControllerAdvice** oder *
*@ModelAttribute** realisiert.

### **1. @ControllerAdvice**

Für globale Logik wie:

* Exception Handling
* gemeinsame Model-Attribute
* globale Binder/Formatter

```java

@ControllerAdvice
public class GlobalControllerAdvice {

  @ModelAttribute("appName")
  public String appName() {
    return "MyApp";
  }
}
```

→ Alle Controller erhalten `appName` automatisch.

---

### **2. Gemeinsame Basisklasse (selten nötig)**

Controller können von einer gemeinsamen Klasse erben, die Hilfsmethoden bereitstellt.

---

### Kurzfazit

> Gemeinsame Controller-Logik implementiert man am besten mit **@ControllerAdvice** für globale
> Logik oder **@ModelAttribute** für gemeinsame Daten.

## ❓ What is a Controller Advice?

`@ControllerAdvice` ist ein Mechanismus, um **globale Logik für mehrere Controller** zentral
bereitzustellen.
Es wird verwendet, um Controller-übergreifend Funktionen bereitzustellen wie:

* **globale Exception-Handler**
* **globale Model-Attribute**
* **globale InitBinder-Konfigurationen**

### Beispiel

```java

@ControllerAdvice
public class GlobalAdvice {

  @ExceptionHandler(Exception.class)
  public String handle(Exception ex) {
    return "error"; // globale Fehlerseite
  }
}
```

### Kurzfazit

> `@ControllerAdvice` erlaubt es, gemeinsame Controller-Logik wie Exception Handling oder
> Model-Attribute zentral an einer Stelle zu definieren.

## ❓ What is @ExceptionHandler?

`@ExceptionHandler` markiert eine Methode, die **eine bestimmte Exception abfängt**, die in einem
Controller auftritt.
Spring leitet die Exception an diese Methode weiter, statt sie unkontrolliert propagieren zu lassen.

### Beispiel

```java

@ExceptionHandler(IllegalArgumentException.class)
public String handleIllegalArgument(IllegalArgumentException ex) {
  return "error"; // View oder Response
}
```

Kann in einem einzelnen Controller oder global in `@ControllerAdvice` verwendet werden.

### Kurzfazit

> `@ExceptionHandler` definiert Methoden, die bestimmte Exceptions abfangen und kontrolliert
> verarbeiten.

## ❓ Why is Spring MVC so popular?

Spring MVC ist beliebt, weil es:

* ein **klares und bewährtes MVC-Programmierungsmodell** bietet
* **flexibel** ist und viele View-Technologien (Thymeleaf, JSON, XML, JSP usw.) unterstützt
* stark in **Spring Boot integriert** ist, wodurch Konfiguration minimal wird
* **REST-APIs** sehr einfach implementierbar macht (`@RestController`, `@GetMapping`, …)
* umfangreiche **Validierungs-, Binding- und Exception-Handling**-Mechanismen bietet
* gute **Testbarkeit** besitzt
* eine riesige **Community** und ausgezeichnete Dokumentation hat

### Kurzfazit

> Spring MVC ist populär, weil es ein ausgereiftes, flexibles und leichtgewichtiges Webframework
> ist, das mit Spring Boot extrem einfach zu benutzen ist.

## ❓ What is Spring Boot?

Spring Boot ist ein Framework, das die Entwicklung von Spring-Anwendungen **stark vereinfacht**,
indem es:

* Konfiguration automatisiert (**Auto-Configuration**)
* eine Sammlung vorkonfigurierter Abhängigkeiten bereitstellt (**Starter Dependencies**)
* einen eingebetteten Webserver mitliefert (Tomcat, Jetty, Netty)
* Projekte mit sehr wenig Setup startklar macht

Es reduziert Boilerplate, verbessert die Produktivität und macht Spring-Anwendungen sofort
lauffähig.

### Kurzfazit

> Spring Boot ist ein Framework, das Spring-Anwendungen durch Auto-Configuration, Starter und
> eingebettete Server extrem schnell und einfach nutzbar macht.

## ❓ What are the important Goals of Spring Boot?

Spring Boot verfolgt vier zentrale Ziele:

### **1. Schnell und einfach starten**

Minimale Konfiguration, sofort lauffähige Projekte durch Auto-Configuration und Starter.

### **2. Produktionsreife out of the box**

Actuator, Health Checks, Metrics, Logging, Error Handling → ohne Zusatzaufwand.

### **3. Weniger Boilerplate, weniger Komplexität**

Keine XML-Konfiguration, klare Defaults, automatische Bean-Erkennung.

### **4. Einfache Bereitstellung**

Eingebettete Server → Anwendung läuft als eigenständiges Jar/Container.

### Kurzfazit

> Spring Boot will Entwicklung vereinfachen, Konfiguration minimieren und produktionsreife
> Anwendungen schnell bereitstellen.

## ❓ What are the important Features of Spring Boot?

Spring Boot bietet mehrere zentrale Features, die Entwicklung und Betrieb stark vereinfachen:

### **1. Auto-Configuration**

Spring konfiguriert Komponenten automatisch basierend auf den vorhandenen Abhängigkeiten.

### **2. Starter Dependencies**

Vorkonfigurierte Maven/Gradle-Dependencies für typische Anwendungsfälle
(z. B. `spring-boot-starter-web`, `spring-boot-starter-data-jpa`).

### **3. Embedded Servers**

Tomcat, Jetty oder Netty direkt eingebettet → keine externe Serverinstallation nötig.

### **4. Spring Boot Actuator**

Bereitstellung von Health-Checks, Metrics, Info-Endpoints und Monitoring-Funktionen out of the box.

### **5. Production-ready Defaults**

Logging, Fehlerseiten, Security-Integrationen und Application Properties mit sinnvollen
Standardwerten.

### **6. Externalized Configuration**

Konfiguration über `application.properties`, `application.yml`, Environment-Variablen, Profiles.

### **7. Easy Packaging and Deployment**

Lauffähige JARs/Container mit allen Abhängigkeiten – ideal für Docker und Cloud.

### **Kurzfazit**

> Spring Boot bietet Auto-Configuration, Starter, eingebaute Server, Actuator und flexible
> Konfiguration — alles, um moderne Anwendungen schnell und produktiv zu entwickeln.

## ❓ Compare Spring Boot vs Spring

### **Spring (Spring Framework)**

* Bietet die Kernfunktionen: IoC, DI, AOP, MVC, Data, Security
* Erfordert **manuelle Konfiguration** (z. B. Webserver, DataSource, MVC-Setup)
* Flexibler, aber mehr Boilerplate
* Für komplexe, fein kontrollierte Architekturen geeignet

---

### **Spring Boot**

* Baut auf Spring Framework auf, **vereinfacht aber alles**
* Auto-Configuration → kaum Konfiguration notwendig
* Starter Dependencies → einfache Abhängigkeitsverwaltung
* Eingebettete Server → Anwendung sofort lauffähig
* Produktionsfeatures wie Actuator integriert
* Perfekt für Microservices und moderne Cloud-Apps

---

### **Kurzfazit**

> **Spring** ist das Fundament (Framework).
> **Spring Boot** ist die komfortable und produktive Art, Spring-Anwendungen zu bauen.

## ❓ Compare Spring Boot vs Spring MVC

### **Spring MVC**

* Web-Framework innerhalb des Spring-Ökosystems
* Bietet das **MVC-Modell**, Routing (`@GetMapping` usw.), Controller, Views
* Erfordert **Konfiguration** (z. B. DispatcherServlet, ViewResolver) – außer wenn Boot benutzt wird
* Fokus: **HTTP-/Webschicht**

---

### **Spring Boot**

* Baut auf Spring MVC (oder WebFlux) auf
* Liefert **Auto-Configuration**, **Starter**, **eingebettete Server**, **Actuator**
* Startet eine Spring-MVC-Anwendung ohne manuelle Konfiguration
* Fokus: **Vollständige App-Initialisierung und Produktivbetrieb**

---

### **Kurzfazit**

> **Spring MVC** ist das Webframework.
> **Spring Boot** ist der Rahmen um die ganze Anwendung herum – und macht Spring MVC sofort nutzbar,
> vorkonfiguriert und produktionsbereit.

## ❓ What is the importance of @SpringBootApplication?

`@SpringBootApplication` ist die zentrale Annotation in Spring Boot und fasst drei wichtige
Annotationen zusammen:

* `@Configuration` – markiert die Klasse als Java-Konfigurationsklasse
* `@EnableAutoConfiguration` – aktiviert Spring Boot **Auto-Configuration**
* `@ComponentScan` – scannt das Paket der Klasse nach Beans

Damit sorgt sie dafür, dass:

* Spring Boot die Anwendung automatisch konfiguriert
* alle Komponenten im Projekt gefunden werden
* die Anwendung sofort lauffähig ist, ohne manuelle Konfiguration

### Beispiel

```java

@SpringBootApplication
public class App {

}
```

### Kurzfazit

> `@SpringBootApplication` aktiviert Component Scan, Auto-Configuration und Java-Konfiguration – die
> Basis, warum Spring Boot so einfach funktioniert.

## ❓ What is Auto Configuration?

**Auto-Configuration** ist ein Spring-Boot-Mechanismus, der automatisch sinnvolle
Standardkonfigurationen bereitstellt — abhängig von den vorhandenen Dependencies und Beans im
Projekt.

Spring Boot erkennt also, **was du in deinem Projekt nutzt**, und konfiguriert die passenden
Komponenten automatisch (z. B. Web MVC, DataSource, JPA, Jackson).

### Beispiel

Wenn `spring-boot-starter-web` auf dem Klassenpfad liegt:

* eingebetteter Tomcat wird konfiguriert
* DispatcherServlet wird automatisch eingerichtet
* JSON-Konfiguration wird automatisch bereitgestellt

Alles ohne eigene Konfigurationsklassen.

### Kurzfazit

> Auto-Configuration konfiguriert Spring-Komponenten automatisch anhand der vorhandenen
> Abhängigkeiten — dadurch entfällt fast alle manuelle Konfiguration.

Hier die **kurze, präzise Antwort** für deine README:

---

## ❓ How can we find more information about Auto Configuration?

Spring Boot bietet mehrere Möglichkeiten, um Auto-Configuration im Detail zu verstehen und zu
prüfen:

### **1. Aktiviere den Auto-Configuration Report (`--debug`)**

Beim Start der Anwendung:

```
--debug
```

Oder in `application.properties`:

```properties
debug=true
```

Dann zeigt Spring Boot im Log:

* **welche Auto-Configurations aktiviert wurden**
* **welche deaktiviert wurden und warum**

---

### **2. Spring Boot Actuator – /actuator/conditions**

Wenn Actuator aktiviert ist:

```properties
management.endpoints.web.exposure.include=conditions
```

Aufruf:

```
/actuator/conditions
```

→ Zeigt detailliert, welche AutoConfig geladen wurde und welche nicht.

---

### **3. Spring Boot Docs**

Alle Auto-Configs sind in den offiziellen Docs beschrieben:

* Listen der AutoConfiguration-Klassen
* Bedingungen (`@Conditional*`)
* welche Beans erstellt werden

---

### Kurzfazit

> Mehr Infos über Auto-Configuration erhält man über den Debug-Report (`--debug`), den
> Actuator-Endpoint `/actuator/conditions` und die Spring Boot Dokumentation.

## ❓ How can we find more information about Auto Configuration?

Spring Boot bietet mehrere Möglichkeiten, um Auto-Configuration im Detail zu verstehen und zu
prüfen:

### **1. Aktiviere den Auto-Configuration Report (`--debug`)**

Beim Start der Anwendung:

```
--debug
```

Oder in `application.properties`:

```properties
debug=true
```

Dann zeigt Spring Boot im Log:

* **welche Auto-Configurations aktiviert wurden**
* **welche deaktiviert wurden und warum**

---

### **2. Spring Boot Actuator – /actuator/conditions**

Wenn Actuator aktiviert ist:

```properties
management.endpoints.web.exposure.include=conditions
```

Aufruf:

```
/actuator/conditions
```

→ Zeigt detailliert, welche AutoConfig geladen wurde und welche nicht.

---

### **3. Spring Boot Docs**

Alle Auto-Configs sind in den offiziellen Docs beschrieben:

* Listen der AutoConfiguration-Klassen
* Bedingungen (`@Conditional*`)
* welche Beans erstellt werden

---

### Kurzfazit

> Mehr Infos über Auto-Configuration erhält man über den Debug-Report (`--debug`), den
> Actuator-Endpoint `/actuator/conditions` und die Spring Boot Dokumentation.

## ❓ What is an embedded server? Why is it important?

### **Was ist ein embedded server?**

Ein **embedded server** ist ein Webserver (z. B. Tomcat, Jetty, Netty), der **direkt in der
Anwendung mitläuft**, statt separat installiert zu werden.
Spring Boot startet diesen Server automatisch beim Aufruf der Anwendung.

### Beispiel

Ein Spring-Boot-JAR enthält bereits Tomcat.
Starten reicht:

```
java -jar app.jar
```

---

### **Warum ist das wichtig?**

* **Keine Installation** eines externen Servers
* **Einfaches Deployment** (ein einziges Jar/Docker-Image)
* **DevOps-freundlich** – perfekt für Docker, Kubernetes und Cloud
* **Einheitliche Umgebung**: Server-Version ist immer korrekt eingebettet
* **Microservices-ready**: jedes Service bringt seinen eigenen Webserver mit

---

### Kurzfazit

> Ein embedded server ist ein in die Anwendung integrierter Webserver.
> Er macht Deployment einfacher, reproduzierbar und ideal für Microservices.

## ❓ What is the default embedded server with Spring Boot?

Der **Standard-Webserver** in Spring Boot ist **Apache Tomcat**, der im Starter
`spring-boot-starter-web` enthalten ist.

### Kurzfazit

> Spring Boot nutzt standardmäßig **Tomcat** als embedded server.

## ❓ What are the other embedded servers supported by Spring Boot?

Neben dem Standardserver **Tomcat** unterstützt Spring Boot auch:

* **Jetty**
* **Undertow**
* **Netty** (für WebFlux)

### Kurzfazit

> Spring Boot kann Tomcat, Jetty, Undertow und Netty als embedded servers verwenden.

## ❓ What are Starter Projects?

**Starter Projects** sind vordefinierte, gebündelte Maven/Gradle-Abhängigkeiten, die typische
Anwendungsfälle in Spring Boot abdecken.
Sie enthalten alle benötigten Libraries mit passenden Versionen — ohne dass du sie einzeln
heraussuchen musst.

### Beispiele

* `spring-boot-starter-web` → Web + Tomcat + JSON
* `spring-boot-starter-data-jpa` → JPA + Hibernate + Transactions
* `spring-boot-starter-security` → Security-Konfiguration
* `spring-boot-starter-test` → Testing-Frameworks

### Kurzfazit

> Starter Projects sind Paketbündel, die Spring Boot sofort einsatzbereit für typische Aufgaben
> machen — ohne komplexe Dependency-Konfiguration.

## ❓ What is Starter Parent?

Der **Spring Boot Starter Parent** ist ein Maven-Parent-POM, das:

* **zentrale Versionsverwaltung** für alle Spring-Abhängigkeiten bereitstellt
* sinnvolle **Default-Konfigurationen** für Maven enthält
* Build-Plugins und Einstellungen vorkonfiguriert (z. B. Compiler-Setup, Encoding, Tests)

Damit musst du **keine Versionen** für typische Spring-Abhängigkeiten selbst angeben.

### Beispiel (pom.xml)

```xml

<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>3.2.0</version>
</parent>
```

### Kurzfazit

> Der Starter Parent liefert zentrale, konsistente Versionen und Build-Defaults — und vereinfacht
> das Maven-Setup erheblich.

## ❓ What are the different things that are defined in Starter Parent?

Der **Spring Boot Starter Parent** bringt eine Reihe vorkonfigurierter Einstellungen mit, die ein
Maven-Projekt sofort stabil und konsistent machen.

### Was definiert der Starter Parent?

* **Dependency Management**
  Versionen aller Spring-Bibliotheken und vieler Drittanbieter werden zentral verwaltet.

* **Plugin-Konfigurationen**
  Vorkonfigurierte Plugins wie

    * `maven-compiler-plugin`
    * `spring-boot-maven-plugin`
    * `maven-surefire-plugin`

* **Java-Version & Encoding Defaults**
  Standard: UTF-8, passende Compiler-Optionen.

* **Build Defaults**
  Z. B. Ressourcenfilterung, Testkonfigurationen, Reproducible Builds.

* **Default Properties**
  z. B. sinnvolle Einstellungen für Logging, Exceptions und Packaging.

### Kurzfazit

> Der Starter Parent definiert zentrale Versionsverwaltung, Plugins, Compiler- und Build-Defaults —
> damit Spring-Boot-Projekte ohne zusätzliche Konfiguration stabil laufen.

## ❓ How does Spring Boot enforce common dependency management for all its Starter projects?

Spring Boot erzwingt ein **einheitliches Dependency-Management** durch:

### **1. Das Spring Boot Parent POM**

Alle Starter-Projekte beziehen ihre Versionsinformationen aus dem zentralen Parent:

```xml

<dependencyManagement>
  <dependencies>
    <!-- hier stehen alle Versionen -->
  </dependencies>
</dependencyManagement>
```

→ Dadurch nutzen **alle Starter** dieselben Versionen von Spring, Jackson, Tomcat, Hibernate usw.

---

### **2. Das Spring Boot BOM (Bill of Materials)**

Selbst ohne Parent kannst du das BOM importieren:

```xml

<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-dependencies</artifactId>
      <version>3.2.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

→ Alle Starter nutzen automatisch die im BOM festgelegten Versionen.

---

### Kurzfazit

> Spring Boot stellt ein einheitliches Dependency-Management sicher, indem alle Starter auf das *
*Spring Boot Parent POM / BOM** verweisen, das sämtliche Versionsnummern zentral definiert.

## ❓ What is Spring Initializr?

**Spring Initializr** ist ein Online-Tool ([https://start.spring.io](https://start.spring.io)), das
automatisch ein **startfertiges Spring-Boot-Projekt** generiert.
Du wählst:

* Maven/Gradle
* Java-Version
* Dependencies (z. B. Web, JPA, Security)
* Projektname & Package

Initializr erstellt daraus ein vollständiges Projektgerüst, das direkt gebaut und gestartet werden
kann.

### Kurzfazit

> Spring Initializr ist ein Generator für Spring-Boot-Projekte, der dir das Setup und die
> Konfiguration abnimmt — ideal für einen schnellen Start.

## ❓ What is `application.properties`?

`application.properties` ist die zentrale **Konfigurationsdatei** in Spring Boot.
Darin kannst du Einstellungen für:

* Server (Port, Error Pages)
* Datenbanken
* Logging
* Spring MVC
* Security
* Eigene Custom Properties

definieren — ohne den Code zu ändern.

### Beispiel

```properties
server.port=8081
spring.datasource.url=jdbc:postgresql://localhost/db
logging.level.org.springframework=INFO
```

Spring Boot liest diese Datei beim Start automatisch ein.

### Kurzfazit

> `application.properties` enthält externe Konfiguration für die Anwendung — sauber getrennt vom
> Code und leicht für verschiedene Umgebungen anpassbar.

## ❓ What are some of the important things that can be customized in `application.properties`?

In Spring Boot kannst du viele zentrale Einstellungen über `application.properties` anpassen, z. B.:

### **1. Server-Konfiguration**

```properties
server.port=8081
server.error.include-message=always
```

### **2. Datenbank & JPA**

```properties
spring.datasource.url=jdbc:postgresql://localhost/db
spring.jpa.hibernate.ddl-auto=update
```

### **3. Logging**

```properties
logging.level.root=INFO
logging.level.org.springframework.web=DEBUG
```

### **4. Spring MVC / Web**

```properties
spring.mvc.view.prefix=/templates/
spring.mvc.format.date=yyyy-MM-dd
```

### **5. Actuator**

```properties
management.endpoints.web.exposure.include=health,info
```

### **6. Eigene Konfiguration (Custom Properties)**

```properties
app.title=My Application
```

### Kurzfazit

> Über `application.properties` kannst du Server, Datenbank, Logging, Webverhalten, Actuator und
> eigene Einstellungen konfigurieren — ohne den Code anzupassen.

## ❓ How do you externalize configuration using Spring Boot?

Spring Boot erlaubt es, **Konfiguration vom Code zu trennen**, damit Einstellungen je nach
Umgebung (dev, test, prod) geändert werden können — ohne neu zu bauen.

Wichtige Wege der Externalisierung:

### **1. application.properties / application.yml**

Standard-Konfiguration im Projekt.

---

### **2. Environment-spezifische Dateien**

Spring lädt sie automatisch je nach aktivem Profile:

```
application-dev.properties
application-prod.properties
```

Aktivieren:

```properties
spring.profiles.active=dev
```

---

### **3. Environment Variables (Umgebungsvariablen)**

Ideal für Docker, Cloud, CI/CD:

```bash
export SERVER_PORT=9090
```

---

### **4. Command-Line Arguments**

```bash
java -jar app.jar --server.port=9090
```

---

### **5. External Config Files**

Spring lädt automatisch Dateien außerhalb des JARs:

```
/config/application.properties
application.properties im selben Ordner wie das JAR
```

---

### **6. @ConfigurationProperties für strukturierte Konfiguration**

```java

@ConfigurationProperties(prefix = "app")
public class AppConfig {

  private String name;
}
```

---

### Kurzfazit

> Konfiguration kann über Properties/YAML, Profile, Umgebungsvariablen, externe Dateien,
> CLI-Parameter oder `@ConfigurationProperties` externalisiert werden — sauber getrennt vom Code.

## ❓ How can you add custom application properties using Spring Boot?

Du kannst eigene Konfigurationseigenschaften definieren, indem du:

### **1. Eigene Properties in `application.properties` anlegst**

```properties
app.title=Interview Guide
app.max-users=20
```

---

### **2. Eine Klasse mit `@ConfigurationProperties` erstellst**

```java

@ConfigurationProperties(prefix = "app")
@Component
public class AppProperties {

  private String title;
  private int maxUsers;

  // Getter/Setter
}
```

---

### **3. Properties im Code verwenden**

```java

@Service
public class DemoService {

  private final AppProperties props;

  public DemoService(AppProperties props) {
    this.props = props;
  }

  public void print() {
    System.out.println(props.getTitle());
  }
}
```

---

### Kurzfazit

> Eigene Properties werden in `application.properties` definiert und über `@ConfigurationProperties`
> sauber typisiert in den Code übernommen.

## ❓ What is @ConfigurationProperties?

`@ConfigurationProperties` bindet **externe Konfiguration** (z. B. aus `application.properties` oder
Umgebungsvariablen) **typsicher an eine Java-Klasse**.
Damit können komplexe Konfigurationen strukturiert und sauber im Code genutzt werden.

### Beispiel

```properties
app.title=My App
app.max-users=10
```

```java

@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {

  private String title;
  private int maxUsers;
  // Getter/Setter
}
```

Spring füllt die Felder automatisch mit den Properties.

### Vorteile

* typsichere Konfiguration
* Autocomplete im IDE
* Gruppen von Einstellungen übersichtlich gebündelt
* besser testbar

### Kurzfazit

> `@ConfigurationProperties` mappt externe Properties auf eine Java-Klasse — typsicher, strukturiert
> und ideal für Konfiguration.

## ❓ What is a profile?

Ein **Spring Profile** ist ein Mechanismus, um **unterschiedliche Konfigurationen für verschiedene
Umgebungen** bereitzustellen — z. B. *dev*, *test*, *prod*.

Du kannst damit bestimmte Beans oder Properties **nur für ein bestimmtes Profil aktivieren**.

### Beispiel

```java

@Profile("dev")
@Component
public class DevDatabaseConfig {

}
```

Aktivieren eines Profils:

```properties
spring.profiles.active=dev
```

### Kurzfazit

> Ein Profil ermöglicht es, abhängig von der Umgebung unterschiedliche Beans, Konfigurationen oder
> Properties zu verwenden.

## ❓ How do you define beans for a specific profile?

Du kannst Beans an bestimmte Profile binden, indem du sie mit `@Profile` annotierst.
Die Bean wird **nur geladen**, wenn das entsprechende Profil aktiv ist.

### Beispiel

```java

@Configuration
public class AppConfig {

  @Bean
  @Profile("dev")
  public DataSource devDataSource() {
    return new HikariDataSource(); // Beispiel
  }

  @Bean
  @Profile("prod")
  public DataSource prodDataSource() {
    return new HikariDataSource();
  }
}
```

Aktives Profil setzen:

```properties
spring.profiles.active=dev
```

### Kurzfazit

> Beans werden profilabhängig definiert, indem man sie mit `@Profile("<name>")` annotiert — Spring
> lädt sie nur, wenn dieses Profil aktiv ist.

## ❓ How do you create application configuration for a specific profile?

Spring Boot erlaubt es, **profile-spezifische Properties-Dateien** anzulegen.
Diese enthalten Konfigurationen, die nur gelten, wenn ein bestimmtes Profil aktiv ist.

### **1. Profile-spezifische Properties-Dateien anlegen**

Beispiele:

```
application-dev.properties
application-test.properties
application-prod.properties
```

Spring lädt automatisch die Datei, die dem aktiven Profil entspricht.

---

### **2. Profil aktivieren**

In `application.properties`:

```properties
spring.profiles.active=dev
```

Oder via CLI:

```bash
java -jar app.jar --spring.profiles.active=prod
```

Oder per Umgebungsvariable:

```bash
export SPRING_PROFILES_ACTIVE=test
```

---

### **3. Beispiel: application-dev.properties**

```properties
server.port=8081
spring.datasource.url=jdbc:h2:mem:testdb
```

**application-prod.properties**

```properties
server.port=8080
spring.datasource.url=jdbc:postgresql://prod/db
```

---

### Kurzfazit

> Lege `application-<profile>.properties` Dateien an und aktiviere ein Profil über
`spring.profiles.active`. Spring lädt automatisch die passende Konfiguration.

## ❓ How do you have different configuration for different environments?

Spring Boot unterstützt verschiedene Umgebungen durch:

### **1. Profile-spezifische Properties**

```
application-dev.properties
application-prod.properties
```

Aktivieren:

```properties
spring.profiles.active=dev
```

---

### **2. Environment Variables**

Ideal für Docker/Cloud:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://prod/db
```

---

### **3. Command-line arguments**

```bash
java -jar app.jar --server.port=9000
```

---

### **4. Profile-spezifische Beans**

```java

@Profile("prod")
@Bean
public DataSource dataSource() { ...}
```

### Kurzfazit

> Unterschiedliche Konfigurationen werden über **Profile**, **Environment Variables**, *
*CLI-Parameter** und **profilabhängige Beans** realisiert.

## ❓ What is Spring Boot Actuator?

Spring Boot Actuator ist ein Modul, das **produktiosnreife Monitoring- und Management-Endpunkte**
bereitstellt.
Es liefert Informationen über:

* Health Checks
* Metriken
* Konfiguration
* Logs
* Beans
* Thread Dumps
* Environment Properties

Aktivierung erfolgt über die Dependency:

```xml

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### Kurzfazit

> Actuator stellt Monitoring-, Diagnose- und Verwaltungsendpunkte bereit — ideal für Betrieb,
> Debugging und Observability.

## ❓ How do you monitor web services using Spring Boot Actuator?

Mit Spring Boot Actuator kannst du Webservices über **vorkonfigurierte Monitoring-Endpunkte**
überwachen.
Dazu gehören u. a.:

* **/actuator/health** – zeigt den Gesundheitszustand
* **/actuator/metrics** – liefert Metriken (CPU, Speicher, HTTP-Requests)
* **/actuator/httptrace** – letzte HTTP-Requests (optional)
* **/actuator/loggers** – Logging-Level anzeigen/ändern
* **/actuator/beans** – Übersicht aller Beans

### Schritte

### **1. Actuator aktivieren**

```xml

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### **2. Endpunkte freigeben**

```properties
management.endpoints.web.exposure.include=health,metrics,info
```

### **3. Monitoring-Tools nutzen**

* Via Browser/HTTP: `/actuator/metrics/http.server.requests`
* Integration mit Monitoring-Systemen: Prometheus, Grafana, New Relic, etc.

### Kurzfazit

> Webservices werden über Actuator-Endpunkte wie *health* und *metrics* überwacht; sie liefern
> Status, Metriken und Diagnoseinformationen.

## ❓ How do you find more information about your application environment using Spring Boot?

Spring Boot stellt über **Actuator** mehrere Endpunkte bereit, die detaillierte Informationen über
die Laufzeitumgebung liefern:

### **1. /actuator/env**

Zeigt alle Environment-Properties:

* `application.properties`
* System-Properties
* Environment-Variablen
* Konfigurationen von Spring

### **2. /actuator/configprops**

Listet alle **@ConfigurationProperties** inkl. ihrer aktuellen Werte auf.

### **3. /actuator/beans**

Zeigt alle geladenen Beans und ihre Abhängigkeiten.

### **4. /actuator/info**

Kann eigene Meta-Informationen anzeigen (z. B. Build- oder Versiondaten).

### **Aktivieren:**

```properties
management.endpoints.web.exposure.include=env,configprops,beans,info
```

---

### Kurzfazit

> Über Actuator-Endpunkte wie **/env**, **/configprops** und **/beans** erhältst du vollständige
> Informationen über Umgebung, Konfiguration und Bean-Setup deiner Anwendung.

## ❓ What is a CommandLineRunner?

`CommandLineRunner` ist ein Spring-Boot-Callback-Interface, dessen `run()`-Methode **nach dem Start
der Anwendung** ausgeführt wird.
Es eignet sich für Initialisierungscode wie:

* Daten laden
* Logs schreiben
* Testdaten erzeugen
* Startmeldungen ausgeben

### Beispiel

```java

@Component
public class StartupRunner implements CommandLineRunner {

  @Override
  public void run(String... args) {
    System.out.println("Application started!");
  }
}
```

### Kurzfazit

> `CommandLineRunner` ermöglicht es, Code direkt nach dem Starten der Spring-Boot-Anwendung
> auszuführen.

## ❓ What is Spring JDBC? How is it different from JDBC?

**Spring JDBC** ist ein Modul in Spring, das den Umgang mit JDBC **stark vereinfacht**, indem es:

* Boilerplate-Code entfernt
* Ressourcenmanagement übernimmt (Connection, Statement, ResultSet)
* Fehler in **DataAccessExceptions** übersetzt
* Hilfsklassen wie `JdbcTemplate` bereitstellt

### Unterschied zu „normalem“ JDBC

| JDBC (klassisch)                                                             | Spring JDBC                                  |
|------------------------------------------------------------------------------|----------------------------------------------|
| viel Boilerplate: Connection öffnen, Statement erzeugen, ResultSet schließen | **automatisches Ressourcen-Handling**        |
| Fehler als `SQLException`                                                    | Fehlerübersetzung in **DataAccessException** |
| viel wiederholter Code                                                       | **JdbcTemplate** übernimmt Standardlogik     |
| manuell Mappen von ResultSets                                                | **RowMapper** für sauberes Mapping           |

### Mini-Beispiel mit Spring JDBC

```java

@Autowired
JdbcTemplate jdbc;

public User findById(long id) {
  return jdbc.queryForObject(
      "SELECT id, name FROM users WHERE id = ?",
      (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("name")),
      id
  );
}
```

### Kurzfazit

> Spring JDBC reduziert den Aufwand der Arbeit mit JDBC, automatisiert Ressourcenmanagement und
> bietet komfortable Hilfsklassen wie `JdbcTemplate`.

## ❓ What is a JdbcTemplate?

`JdbcTemplate` ist eine zentrale Hilfsklasse in Spring JDBC, die **Datenbankzugriffe stark
vereinfacht**.
Sie übernimmt:

* Öffnen und Schließen von Connections
* Erstellen von Statements
* Ausführen von SQL
* Fehlerbehandlung (Übersetzung in `DataAccessException`)

Der Entwickler schreibt nur noch **SQL + Mapping**, nicht den Infrastrukturcode.

### Beispiel

```java

@Autowired
private JdbcTemplate jdbc;

public List<User> findAll() {
  return jdbc.query(
      "SELECT id, name FROM users",
      (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("name"))
  );
}
```

Spring kümmert sich automatisch um:

* Connection holen
* PreparedStatement erzeugen
* ResultSet schließen
* Exceptions übersetzen

### Kurzfazit

> `JdbcTemplate` reduziert JDBC auf das Wesentliche, indem es Ressourcenmanagement und
> Fehlerbehandlung automatisiert.

## ❓ What is a RowMapper?

Ein **RowMapper** ist ein Interface in Spring JDBC, das definiert, **wie eine Zeile eines ResultSets
in ein Java-Objekt umgewandelt** wird.
Es trennt das Ergebnis-Mapping sauber vom SQL.

### Beispiel

```java
public class UserMapper implements RowMapper<User> {

  @Override
  public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new User(rs.getLong("id"), rs.getString("name"));
  }
}
```

Verwendung mit `JdbcTemplate`:

```java
jdbc.query("SELECT * FROM users",new UserMapper());
```

### Kurzfazit

> Ein RowMapper konvertiert eine Datenbankzeile in ein Java-Objekt — sauber, wiederverwendbar und
> ohne Boilerplate.

## ❓ What is JPA?

**JPA (Java Persistence API)** ist eine **Spezifikation** für objektorientierte Datenbankzugriffe in
Java.
Sie definiert, *wie* Java-Objekte („Entities“) auf relationale Datenbanktabellen abgebildet werden —
ohne festzulegen, *wie* die Implementierung aussieht.

Bekannte Implementierungen sind z. B.:

* **Hibernate**
* EclipseLink
* OpenJPA

### Zweck von JPA

* Objekt–Relationale Abbildung (ORM)
* CRUD-Operationen
* Querying (JPQL, Criteria API)
* Transaktionsmanagement
* Caching

JPA selbst ist also **kein Framework**, sondern ein **Standard**, den Frameworks wie Hibernate
umsetzen.

### Kurzfazit

> JPA ist die Java-Standardspezifikation für ORM, die definiert, wie Java-Objekte in
> Datenbanktabellen gespeichert und geladen werden — umgesetzt z. B. durch Hibernate.

## ❓ What is Hibernate?

**Hibernate** ist die **beliebteste Implementierung von JPA** und ein vollständiges ORM-Framework
für Java.
Es setzt die JPA-Spezifikation um und erweitert sie zusätzlich mit eigenen Features.

### Was bietet Hibernate?

* ORM: Mapping von Java-Klassen auf Datenbanktabellen
* Automatische SQL-Generierung
* Caching (1st- und 2nd-Level Cache)
* Lazy Loading / Fetching-Strategien
* Transaktionsmanagement
* JPQL- und Criteria-API-Unterstützung
* Erweiterungen über den JPA-Standard hinaus

### Kurzfazit

> Hibernate ist eine mächtige ORM-Engine und die gängigste JPA-Implementierung, die Java-Objekte
> automatisch mit relationalen Datenbanken synchronisiert.

## ❓ How do you define an entity in JPA?

Eine JPA-Entity ist eine **Java-Klasse**, die mit der Annotation `@Entity` markiert ist und eine *
*Primärschlüsselspalte** besitzt (`@Id`).
Sie repräsentiert eine Tabelle in der Datenbank.

### Beispiel

```java

@Entity
public class User {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  // Getter/Setter
}
```

### Wichtig

* `@Entity` → Klasse wird dauerhaft gespeichert
* `@Id` → Primärschlüssel
* `@GeneratedValue` → automatische ID-Erzeugung
* Felder werden automatisch zu Spalten (konfigurierbar über `@Column`)

### Kurzfazit

> Eine Entity ist eine mit `@Entity` annotierte Klasse mit einem `@Id`-Feld, die eine Tabelle in der
> Datenbank abbildet.

## ❓ What is an Entity Manager?

Der **EntityManager** ist die zentrale JPA-Komponente, die für **CRUD-Operationen, Abfragen und das
Verwalten des Persistence Context** zuständig ist.
Er steuert also, wie Entities gespeichert, geladen, aktualisiert und gelöscht werden.

### Aufgaben des EntityManagers

* `persist()` → Objekt speichern
* `find()` → Entity laden
* `merge()` → Änderungen übernehmen
* `remove()` → Entity löschen
* Queries ausführen (JPQL, Criteria)
* Lifecycle von Entities verwalten (managed, detached, removed)

### Beispiel

```java

@Autowired
private EntityManager em;

public User load(Long id) {
  return em.find(User.class, id);
}
```

### Kurzfazit

> Der EntityManager ist das JPA-Interface für alle Datenbankoperationen und verwaltet den Zustand
> der Entities im Persistence Context.

## ❓ What is a Persistence Context?

Der **Persistence Context** ist der **erste-Level-Cache** in JPA, in dem der EntityManager alle *
*verwalteten Entities** hält.
Solange eine Entity dort liegt, ist sie **“managed”**, d. h.:

* Änderungen an der Entity werden automatisch verfolgt
* JPA synchronisiert diese Änderungen beim Flush/Persist mit der Datenbank
* In einem Persistence Context gibt es jede Entity **nur einmal** (Identity Guarantee)
* Wiederholte `find()`-Aufrufe liefern dasselbe Objekt aus dem Cache, nicht erneut aus der DB

### Beispiel

```java
User u1 = em.find(User.class, 1L);
User u2 = em.find(User.class, 1L);

assert u1 ==u2; // true – gleiche Instanz aus dem Persistence Context
```

### Kurzfazit

> Der Persistence Context ist der von JPA verwaltete Cache, in dem Entities während einer
> Transaktion leben. Änderungen an diesen Entities werden automatisch nachverfolgt und zur Datenbank
> synchronisiert.

## ❓ How do you map relationships in JPA?

Beziehungen zwischen Entities werden in JPA mit speziellen Annotationen modelliert.
Die wichtigsten Beziehungstypen sind:

### **1. @OneToOne**

Eine Entity gehört genau zu einer anderen.

```java

@OneToOne
private Address address;
```

---

### **2. @OneToMany**

Eine Entity hat mehrere zugeordnete Entities.

```java

@OneToMany(mappedBy = "order")
private List<Item> items;
```

---

### **3. @ManyToOne**

Mehrere Entities referenzieren dieselbe Ziel-Entity.

```java

@ManyToOne
private Customer customer;
```

---

### **4. @ManyToMany**

Beide Seiten haben mehrere Referenzen.

```java

@ManyToMany
private List<Role> roles;
```

---

Alle Beziehungen können zusätzlich konfiguriert werden über:

* `mappedBy`
* `cascade`
* `fetch`
* `joinColumn`

### Kurzfazit

> Beziehungen werden mit @OneToOne, @OneToMany, @ManyToOne und @ManyToMany abgebildet — je nach Art
> der Beziehung zwischen Entities.

## ❓ What are the different types of relationships in JPA?

JPA unterstützt vier grundlegende Beziehungstypen zwischen Entities:

### **1. @OneToOne**

Eine Entity ist mit genau einer anderen verknüpft.
Beispiel: User ↔ Address

---

### **2. @OneToMany**

Eine Entity besitzt viele abhängige Entities.
Beispiel: Order → List<OrderItem>

---

### **3. @ManyToOne**

Viele Entities gehören zu einer übergeordneten Entity.
Beispiel: Many Orders → One Customer

---

### **4. @ManyToMany**

Mehrere Entities sind mit mehreren anderen verknüpft (Join-Tabelle).
Beispiel: User ↔ Roles

---

### Kurzfazit

> JPA kennt vier Beziehungstypen: **OneToOne**, **OneToMany**, **ManyToOne**, **ManyToMany**.

## ❓ How do you define a datasource in a Spring Context?

In **Spring Boot** musst du keinen DataSource-Bean selbst definieren.
Spring Boot erstellt automatisch eine `DataSource`, sobald die notwendigen Properties gesetzt sind.

### Beispiel (application.properties)

```properties
spring.datasource.url=jdbc:postgresql://localhost/mydb
spring.datasource.username=user
spring.datasource.password=secret
spring.datasource.driver-class-name=org.postgresql.Driver
```

Spring Boot erkennt diese Einstellungen und erzeugt automatisch eine `HikariDataSource`.

### Manuelle Definition (nur falls nötig)

```java

@Bean
public DataSource dataSource() {
  return DataSourceBuilder.create()
      .url("jdbc:h2:mem:testdb")
      .username("sa")
      .build();
}
```

### Kurzfazit

> In Spring Boot definierst du eine DataSource meist über Properties; Spring erstellt die Bean
> automatisch. Nur in Spezialfällen definiert man sie manuell als @Bean.

## ❓ What is the use of `persistence.xml`?

`persistence.xml` ist eine **JPA-Konfigurationsdatei**, die in klassischen Java-EE- oder
non-Spring-Boot-Anwendungen verwendet wird.
Darin werden Dinge definiert wie:

* die Persistence Unit
* Datenbankverbindung
* JPA-Provider (z. B. Hibernate)
* Entity-Scanning
* JPA-Einstellungen (Dialect, Caching, usw.)

### Beispiel (klassisch)

```xml

<persistence-unit name="myPU">
  <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
  <class>com.example.User</class>
</persistence-unit>
```

---

### In Spring Boot 💡

**Spring Boot benötigt kein `persistence.xml`.**
Alles wird über:

* `application.properties`
* Auto-Configuration
* Annotationen wie `@Entity`

konfiguriert.

---

### Kurzfazit

> `persistence.xml` konfiguriert klassische JPA-Persistence-Units — in modernen
> Spring-Boot-Anwendungen wird es **nicht mehr verwendet**, weil Spring Boot die JPA-Konfiguration
> automatisch übernimmt.

## ❓ How do you configure Entity Manager Factory and Transaction Manager?

In **Spring Boot** musst du **EntityManagerFactory** und **TransactionManager nicht manuell
konfigurieren**.
Spring Boot erstellt beide automatisch, sobald:

* eine JPA-Abhängigkeit (`spring-boot-starter-data-jpa`) vorhanden ist
* eine DataSource konfiguriert wurde

---

## ✔️ Automatische Konfiguration (Spring Boot Standard)

Spring Boot erzeugt automatisch:

* eine **`LocalContainerEntityManagerFactoryBean`**
* einen **`JpaTransactionManager`**

basierend auf deinen `application.properties`.

### Beispiel (properties)

```properties
spring.datasource.url=jdbc:postgresql://localhost/db
spring.jpa.hibernate.ddl-auto=update
```

→ mehr ist nicht nötig.

---

## ✔️ Manuelle Konfiguration (nur Spezialfälle)

Wenn du z. B. mehrere Datenbanken verwendest, kannst du Factory und TransactionManager selbst
definieren:

```java

@Configuration
@EnableTransactionManagement
public class JpaConfig {

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      DataSource dataSource) {

    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setDataSource(dataSource);
    emf.setPackagesToScan("com.example.entities");
    emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

    return emf;
  }

  @Bean
  public PlatformTransactionManager transactionManager(
      EntityManagerFactory emf) {

    return new JpaTransactionManager(emf);
  }
}
```

---

## Kurzfazit

> In Spring Boot werden **EntityManagerFactory** und **TransactionManager automatisch konfiguriert
**.
> Manuelle Konfiguration ist nur nötig, wenn du mehrere Datenbanken oder komplexe Settings
> verwendest.

## ❓ How do you define transaction management for Spring – Hibernate integration?

In modernen Spring-Boot-Anwendungen wird das **Transaktionsmanagement für Hibernate/JPA** weitgehend
**automatisch** konfiguriert:

* `spring-boot-starter-data-jpa` auf dem Klassenpfad
* konfigurierte `DataSource`
* → Spring Boot erstellt automatisch einen `JpaTransactionManager`.

Du aktivierst Transaktionsmanagement über Annotationen:

### **1. @EnableTransactionManagement** (oft schon indirekt aktiv)

In einer Config-Klasse (falls nötig explizit):

```java

@Configuration
@EnableTransactionManagement
public class JpaConfig {

}
```

### **2. @Transactional an Service-Methoden**

```java

@Service
public class AccountService {

  @Transactional
  public void transferMoney(...) {
    // Hibernate/JPA-Operationen
  }
}
```

Spring:

* öffnet beim Aufruf eine Transaction
* führt Hibernate/JPA-Operationen innerhalb dieser Transaction aus
* committet oder rollt bei Exception zurück

---

### Kurzfazit

> Spring–Hibernate-Transaktionen werden in Spring Boot durch `JpaTransactionManager` +
`@Transactional` gesteuert. Meist reicht es, `@Transactional` an Service-Methoden zu verwenden –
> Boot übernimmt den Rest.

## ❓ What is Spring Data?

**Spring Data** ist ein Spring-Framework, das die Arbeit mit Datenquellen stark vereinfacht.
Es bietet **abstrakte Repository-Schnittstellen**, die automatisch Implementierungen für CRUD-,
Paging- und Query-Operationen erzeugen.

Es deckt viele Technologien ab:

* **Spring Data JPA** (Hibernate/JPA)
* Spring Data MongoDB
* Spring Data JDBC
* Spring Data Redis
* u. a.

### Vorteile

* kaum Boilerplate-Code
* automatische Repository-Generierung
* Query-Methoden aus Methodennamen (`findByEmail(...)`)
* einheitliches Programmiermodell für verschiedene Datenquellen

### Kurzfazit

> Spring Data bietet ein einheitliches, boilerplate-freies Repository-Modell für relationale und
> NoSQL-Datenbanken.

## ❓ What is the need for Spring Data?

Spring Data löst das Problem, dass **Datenzugriff in Java traditionell viel Boilerplate-Code**
benötigt (JPA, JDBC, Hibernate API).
Es reduziert Komplexität und wiederholte Aufgaben massiv.

### Warum braucht man Spring Data?

* **Weniger Boilerplate**
  Kein eigenes Schreiben von CRUD-Implementierungen.

* **Automatische Repository-Generierung**
  Interfaces reichen – Spring erstellt die Implementierung.

* **Einheitliches API**
  Egal ob JPA, MongoDB, Redis, Elasticsearch – das Programmiermodell bleibt gleich.

* **Einfache Query-Erstellung**
  Methoden wie `findByEmail()` funktionieren ohne SQL.

* **Saubere Trennung**
  Domänenlogik bleibt schlank; Datenzugriff ist gekapselt.

### Kurzfazit

> Spring Data wird gebraucht, um Datenzugriff einfacher, konsistenter und boilerplate-frei zu
> machen — unabhängig von der verwendeten Datenbank.

## ❓ What is Spring Data JPA?

**Spring Data JPA** ist ein Modul von Spring Data, das die Arbeit mit **JPA/Hibernate** stark
vereinfacht.
Es baut auf JPA auf und generiert automatisch Repository-Implementierungen, inklusive CRUD-, Paging-
und Query-Methoden.

### Was bietet Spring Data JPA?

* automatische Implementierung von Repositories
* CRUD-Methoden ohne Code
* Query-Methoden durch Methodennamen (`findByEmail`)
* Unterstützung für JPQL, Native Queries und Criteria
* Paging & Sorting out-of-the-box
* nahtlose Integration mit Spring Boot

### Beispiel

```java
public interface UserRepository extends JpaRepository<User, Long> {

  List<User> findByEmail(String email);
}
```

Spring erstellt die Implementierung vollständig automatisch.

### Kurzfazit

> Spring Data JPA ist eine Abstraktionsschicht über JPA, die Repository-Implementierungen
> automatisiert und Datenzugriff extrem vereinfacht.

## ❓ What is a CrudRepository?

`CrudRepository` ist ein Spring-Data-Interface, das **Basis-CRUD-Operationen** für eine Entity
bereitstellt — ohne dass du selbst Implementierungen schreiben musst.

### Wichtige Methoden

* `save(entity)`
* `findById(id)`
* `findAll()`
* `deleteById(id)`
* `count()`

### Beispiel

```java
public interface UserRepository extends CrudRepository<User, Long> {

}
```

Spring generiert die Implementierung automatisch.

### Kurzfazit

> `CrudRepository` stellt grundlegende CRUD-Operationen bereit und erspart dir die manuelle
> Implementierung — ideal für einfache Datenzugriffe.

## ❓ What is a PagingAndSortingRepository?

`PagingAndSortingRepository` erweitert `CrudRepository` um **Paging- und Sortierfunktionen**.
Damit kannst du Datensätze seitenweise laden und sortieren — ohne eigene SQL-Logik.

### Zusätzliche Methoden

* `findAll(Pageable pageable)`
* `findAll(Sort sort)`

### Beispiel

```java
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

}
```

Verwendung:

```java
Page<User> page = repo.findAll(PageRequest.of(0, 20, Sort.by("name")));
```

### Kurzfazit

> `PagingAndSortingRepository` ergänzt CRUD um Paging und Sortierung und eignet sich für große
> Datenmengen, die seitenweise geladen werden sollen.
