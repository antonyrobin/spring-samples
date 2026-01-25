# Spring Boot Student CRUD Application

This project demonstrates a **simple Student Registration system** using **Spring Boot**, **Spring Data JPA**, **Thymeleaf**, and **H2/MySQL**. It covers **Create, Read, Update, Delete (CRUD)** operations with a basic UI.

---

## 📌 Features
- Create new student
- List all students
- Edit student details
- Delete student
- Thymeleaf-based UI
- JPA + Hibernate integration

---

## 🛠️ Prerequisites
Make sure you have the following installed:
- Java 17 or above
- Maven 3.x
- IDE (IntelliJ / Eclipse / VS Code)

---

## 🚀 Create Spring Boot Project

### Using Spring Initializr
1. Go to: https://start.spring.io
2. Project: **Maven**
3. Language: **Java**
4. Spring Boot: default (recommended)
5. Group: `com.example`
6. Artifact: `demo`
7. Packaging: **Jar**
8. Java: **17+**

### Add Dependencies
- Spring Web
- Spring Data JPA
- Thymeleaf
- H2 Database (or MySQL)

Click **Generate**, unzip, and open in IDE.

---

## 📦 Required Dependencies (pom.xml)

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>

    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

---

## 📂 Project Structure

```
src/main/java/com/example/demo
 ├── DemoApplication.java
 ├── controller
 │    └── StudentController.java
 ├── model
 │    └── Student.java
 └── repository
      └── StudentRepository.java

src/main/resources
 ├── templates
 │    ├── students.html
 │    └── student-form.html
 ├── static
 │    └── css
 │         └── style.css
 └── application.properties
```

---

## 🧱 Entity: Student

```java
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String course;
}
```

---

## 🗄️ Repository

```java
public interface StudentRepository extends JpaRepository<Student, Long> {
}
```

---

## 🎮 Controller

```java
@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository repo;

    public StudentController(StudentRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", repo.findAll());
        return "students";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("student", new Student());
        return "student-form";
    }

    @PostMapping
    public String saveStudent(@ModelAttribute Student student) {
        repo.save(student);
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model) {
        model.addAttribute("student", repo.findById(id).orElseThrow());
        return "student-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/students";
    }
}
```

---

## 🖼️ Thymeleaf Templates

### students.html
```html
<a href="/students/new">Add Student</a>
<table>
  <tr th:each="student : ${students}">
    <td th:text="${student.name}"></td>
    <td>
      <a th:href="@{/students/edit/{id}(id=${student.id})}">Edit</a>
      <a th:href="@{/students/delete/{id}(id=${student.id})}">Delete</a>
    </td>
  </tr>
</table>
```

### student-form.html
```html
<form th:action="@{/students}" th:object="${student}" method="post">
    <input type="hidden" th:field="*{id}" />
    <input type="text" th:field="*{name}" />
    <input type="email" th:field="*{email}" />
    <input type="text" th:field="*{course}" />
    <button type="submit">Save</button>
</form>
```

---

## 🎨 Add CSS from Static Folder

### Location
```
src/main/resources/static/css/style.css
```

### Usage
```html
<link rel="stylesheet" th:href="@{/css/style.css}">
```

---

## ⚙️ application.properties

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.h2.console.enabled=true
```

---

## ▶️ Run the Application

### Using IDE
- Right-click `DemoApplication`
- Run as Spring Boot App

### Using Maven
```bash
mvn spring-boot:run
```

Open browser:
```
http://localhost:8080/students
```

---

## 🔁 Run App on Different Port

### Option 1: application.properties
```properties
server.port=9090
```

### Option 2: Command line
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

---

## 🧪 Common Issues

| Problem | Cause | Fix |
|------|------|------|
| 500 error on form | Missing model attribute | Add `model.addAttribute("student", new Student())` |
| CSS not loading | Wrong path | Use `/css/style.css` |
| JPA classes not found | Missing dependency | Add `spring-boot-starter-data-jpa` |

---

## ✅ Next Enhancements
- Add validation (@NotBlank, @Email)
- Add Service layer
- Use MySQL instead of H2
- Convert to REST API
- Add Bootstrap UI

---

Happy coding! 🚀

