# Spring Boot Microservice with JPA Data Repository #

1. Generate project with Spring Web, Spring Data JPA and Validation dependencies
```
https://start.spring.io/
```

2. Add `MySQL Driver` and `swagger` to the pom.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.4.8</version>
		<relativePath /> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.glarimy</groupId>
	<artifactId>directory</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>directory</name>
	<description>Directory Microservice</description>
	<properties>
		<java.version>1.8</java.version>
	</properties>
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
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>2.4.0</version>
		</dependency>
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>2.4.0</version>
		</dependency>
	</dependencies>

	<build>
		<finalName>glarimy-directory</finalName>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
```

3. Employee.java
```
package com.glarimy.directory.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
public class Employee {
	@Id
	@GeneratedValue
	private int id;

	@Pattern(regexp = "^[a-zA-Z]+(\\s[a-zA-Z]+)?$")
	@NotEmpty
	private String name;

	private long phone;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPhone() {
		return phone;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

}
```

4. EmployeeNotFoundException.java
```
package com.glarimy.directory.domain;

@SuppressWarnings("serial")
public class EmployeeNotFoundException extends RuntimeException {

	public EmployeeNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public EmployeeNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EmployeeNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public EmployeeNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public EmployeeNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
```

5. EmployeeRepository.java
```
package com.glarimy.directory.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glarimy.directory.domain.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
	List<Employee> findByNameIgnoreCaseContaining(String token);
}

```

6. Error.java
```
package com.glarimy.directory.rest;

import java.util.HashMap;
import java.util.Map;

public class Error {
	private int status;
	private String message;
	private Map<String, String> errors;

	public Error(int status, String message) {
		this.status = status;
		this.message = message;
		this.errors = new HashMap<String, String>();
	}

	public void addError(String field, String error) {
		this.errors.put(field, error);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

}
```

7. DirectoryController.java
```
package com.glarimy.directory.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glarimy.directory.data.EmployeeRepository;
import com.glarimy.directory.domain.Employee;
import com.glarimy.directory.domain.EmployeeNotFoundException;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@EnableSwagger2
@Validated
@CrossOrigin
public class EmployeeController {

	@Autowired
	private EmployeeRepository repo;

	@PostMapping("/employee")
	public ResponseEntity<Employee> create(@Valid @RequestBody Employee employee) {
		Employee entity = repo.save(employee);
		return new ResponseEntity<Employee>(entity, HttpStatus.CREATED);
	}

	@GetMapping("/employee/{id}")
	public ResponseEntity<Employee> find(@PathVariable("id") int id) {
		Employee employee = repo.findById(id).orElseThrow(() -> new EmployeeNotFoundException());
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}

	@GetMapping("/employee")
	public ResponseEntity<List<Employee>> search(@RequestParam(value = "name", defaultValue = "") String name) {
		List<Employee> employees;
		if (name == "")
			employees = repo.findAll();
		else
			employees = repo.findByNameIgnoreCaseContaining(name);
		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<Error> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> errors = result.getFieldErrors();
		Error error = new Error(400, "Invalid Employee");
		for (FieldError fe : errors) {
			error.addError(fe.getField(), fe.getDefaultMessage());
		}
		return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ RuntimeException.class })
	public ResponseEntity<String> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
		return new ResponseEntity<String>("Employee Not Found", HttpStatus.NOT_FOUND);
	}

}
```

8. DirectoryApplication.java
```
package com.glarimy.directory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DirectoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DirectoryApplication.class, args);
	}
	
	@GetMapping("/hello")
	public String hello(@RequestParam(value="name", defaultValue = "World") String name) {
		return String.format("Hello %s", name);
	}

}
```

9. main/resources/application.properties
```
server.port=8080
server.servlet.context-path=/directory/v1
spring.datasource.url=jdbc:mysql://localhost:3306/glarimy?useSSL=false
spring.datasource.username=root
spring.datasource.password=admin
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
spring.jpa.hibernate.ddl-auto=update
```

10. Dockerfile
```
FROM maven:3.5-jdk-8 AS build
COPY src /usr/glarimy/src
COPY pom.xml /usr/glarimy
RUN mvn -f /usr/glarimy/pom.xml clean package
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.datasource.url=jdbc:mysql://mysqldb:3306/glarimy?useSSL=false&allowPublicKeyRetrieval=true","-jar","/usr/glarimy/target/glarimy-directory.jar"]
```

11. Run the project
```
java -jar target/glarimy-directory.jar
```

12. Verify the API
```
http://localhost:8080/directory/v1/swagger-ui.html
```

13. Login to `https://labs.play-with-docker.com/` with your `docker-hub` id and start a Linux instance

14. Clone the code repository on to the instance

```
git clone https://glarimy@bitbucket.org/glarimy/glarimy-ms.git
```

15. Build the docker image
```
cd glarimy-ms/glarimy-ms-directory-04
```

```
docker build -t glarimy/glarimy-directory .
```
16. Create a network

```
docker network create glarimy
```

17. Run the `mysql` docker container

```
docker container run --name mysqldb --network glarimy -e MYSQL_ROOT_PASSWORD=admin -e MYSQL_DATABASE=glarimy -d mysql
```
```
docker container logs -f mysqldb
```

18. Run the `glarimy-directory` docker container
```
docker container run --network glarimy --name library -p 8080:8080  glarimy/library
```
```
docker container exec -it mysqldb bash
```

19. Find the exposed URL against the port 8080 and verify the `glarimy-directory` service

```
<docker-exposed-url>/directory/v1/swagger-ui.html
```

20. Some useful Docker commands
```
docker ps // lists the running docker containers
docker ps -a // lists all docker containers
docker container rm <name>
docker images // lists docker images
docker network ls // lists the networks
```