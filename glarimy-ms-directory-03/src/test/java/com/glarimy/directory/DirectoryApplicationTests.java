package com.glarimy.directory;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.glarimy.directory.domain.Employee;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DirectoryApplicationTests {
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	public void testAPI() throws JsonProcessingException, IOException {
		Employee e = new Employee();
		e.setName("Koyya");
		e.setPhone(123456);
		
		ResponseEntity<Employee> response = restTemplate.postForEntity("/employee", e, Employee.class);
		Employee entity = response.getBody();
		assertTrue(entity.getId().length() > 0);

		ResponseEntity<Employee> result = restTemplate.getForEntity("/employee/" + entity.getId(), Employee.class);
		assertTrue(result.getBody().getName().contains("Koyya"));
	}
}