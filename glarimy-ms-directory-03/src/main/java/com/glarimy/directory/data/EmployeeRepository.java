package com.glarimy.directory.data;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.glarimy.directory.domain.Employee;

import reactor.core.publisher.Flux;

@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String>{
	Flux<Employee> findByNameIgnoreCaseContaining(String token);
}
