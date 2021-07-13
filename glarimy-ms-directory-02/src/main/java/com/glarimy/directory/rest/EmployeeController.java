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
