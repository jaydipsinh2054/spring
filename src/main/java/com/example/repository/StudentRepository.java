package com.example.repository;

import com.example.entity.Student;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
	List<Student> findByusername(String username);

	Optional<Student> findByEmail(String email);
}
