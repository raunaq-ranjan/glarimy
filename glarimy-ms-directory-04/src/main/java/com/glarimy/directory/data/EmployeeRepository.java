package com.glarimy.directory.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glarimy.directory.domain.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
	List<Employee> findByNameIgnoreCaseContaining(String token);
}
