# Observing Spring Boot Metrics #

2. Generate Spring Boot Application from [https://start.spring.io/](https://start.spring.io/) with `web` and `wavefront` dependencies.

3. pom.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.4.8</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.glarimy</groupId>
	<artifactId>ms</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>glarimy-ms-wavefront</name>
	<description>Observability with Spring Boot</description>
	<properties>
		<java.version>1.8</java.version>
		<wavefront.version>2.1.1</wavefront.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>com.wavefront</groupId>
			<artifactId>wavefront-spring-boot-starter</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>com.wavefront</groupId>
				<artifactId>wavefront-spring-boot-bom</artifactId>
				<version>${wavefront.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
```

4. Microservice.java
```
package com.glarimy.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Microservice {

	public static void main(String[] args) {
		SpringApplication.run(Microservice.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return "Hello " + name;
	}

}
```

5. application.properties
```
wavefront.application.name=glarimy-ms-wafefront
wavefront.application.service=glarimy-service
management.endpoints.web.exposure.include=health,info,wavefront
```

6. Run the application and find the free API key generated and other info. Update the application.properties
```
wavefront.application.name=glarimy-ms-wafefront
wavefront.application.service=glarimy-service
management.metrics.export.wavefront.api-token=<generated-token>
management.metrics.export.wavefront.uri=https://wavefront.surf
management.endpoints.web.exposure.include=health,info,wavefront
```

7. Use the application
[http://localhost:8080/hello?name=Koyya](http://localhost:8080/hello?name=Koyya)

8. Visit the metrics in either of these ways

*[https://wavefront.surf/us/<key>](https://wavefront.surf/us/<key>)

*[http://localhost:8080/actuator/wavefront](http://localhost:8080/actuator/wavefront)