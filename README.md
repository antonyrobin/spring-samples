# sprint-boot-demo

Here's a brief guide on how to set up Spring Boot in Windows, and how to create and run a simple application.

## Install Java Development Kit (JDK)
Spring Boot requires JDK (Java Development Kit). Download and install JDK (preferably version 17 or later):

Download the JDK from the Oracle website or use OpenJDK.

After installation, configure the JAVA_HOME environment variable:

Right-click This PC > Properties > Advanced System Settings.
Go to the Environment Variables > New (under system variables).

Set the variable name as JAVA_HOME and the value to the JDK installation path, e.g., C:\Program Files\Java\jdk-17.

Add JAVA_HOME\bin to your Path environment variable.

Verify the installation by running the following command in the command prompt:
    
    java -version
It should display the JDK version.

## Install Maven
Maven is a build tool commonly used with Spring Boot.

Download Maven from Maven's website.

Extract the files and set up the M2_HOME environment variable similar to JAVA_HOME:

Set M2_HOME to the Maven installation directory.

Add M2_HOME\bin to the Path environment variable.
Verify the installation by running:

    mvn -version

## Install an IDE (Optional)
While not mandatory, using an Integrated Development Environment (IDE) like IntelliJ IDEA or Eclipse is recommended for developing Spring Boot applications.

Download and install IntelliJ IDEA or Eclipse.

Ensure that you install the Spring Boot plugin for the respective IDE.

## Create a Spring Boot Project

### Using Spring Initializr (Website)

Go to the Spring Initializr.

Fill in the project details:

Project: Maven Project.

Language: Java.

Spring Boot Version: Latest stable version (e.g., 3.0.x).

Project Metadata: Group (e.g., com.example), Artifact (e.g., demo), Name (e.g., Demo).

Dependencies: Add dependencies like Spring Web for a basic web application.

Click on Generate to download the project as a ZIP file.

Extract the ZIP file and open the project in your IDE (or command line).

### Using IntelliJ IDEA (Directly)

Open IntelliJ IDEA.

Click New Project > Spring Initializr.

Set the project details as above (Group, Artifact, Name) and add dependencies (like Spring Web).

Click Next and Finish.

## Writing Your First Spring Boot Application
Once the project is set up, navigate to the src/main/java/com/example/demo folder.

Open the DemoApplication.java file and write a basic REST controller:

    package com.example.demo;

    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RestController;

    @SpringBootApplication
    public class DemoApplication {
        public static void main(String[] args) {
            SpringApplication.run(DemoApplication.class, args);
        }
    }

    @RestController
    class HelloController {
        @GetMapping("/hello")
        public String hello() {
            return "Hello, Spring Boot!";
        }
    }

## Running the Application
### In an IDE

Right-click the DemoApplication class and select Run 'DemoApplication'.

Once the application starts, you'll see logs indicating that Spring Boot is up and running on http://localhost:8080.
### From the Command Line
Open a terminal in the project folder and use Maven to build and run the application:

    mvn spring-boot:run
After the build completes, the application will run on http://localhost:8080.

## Accessing the Application
Open a web browser and navigate to http://localhost:8080/hello. You should see the message "Hello, Spring Boot!".

## Packaging the Application (Optional)
To create a standalone JAR file:

Run the following command to package your application:

    mvn clean package

The JAR file will be created in the target/ directory. Run the JAR with:

    java -jar target/demo-0.0.1-SNAPSHOT.jar
This will start the application just like before, accessible on http://localhost:8080.

## Conclusion
You have now successfully set up Spring Boot on Windows and created a simple web application. You can explore more by adding additional Spring Boot dependencies for databases, security, etc., depending on your application's requirements.