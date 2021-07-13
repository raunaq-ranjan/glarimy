# Spring Boot Microservices #

1. Generate, download and extract `maven` project

```
https://start.spring.io/
```

2. Import the project into IDE and add `finalName` to the `pom.xml`

```
<build>
	<finalName>glarimy-directory</finalName>
	...
</build>
```

3. Edit the DirectoryApplication.java

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

4. Build and run the project

```
mvn clean package
java -jar target/glarimy-directory.jar
```

5. Verify the API


```
curl http://localhost:8080/hello
```

```
curl http://localhost:8080/hello?name=Krishna
```