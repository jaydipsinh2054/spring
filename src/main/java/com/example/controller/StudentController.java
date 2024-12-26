package com.example.controller;

import com.example.entity.LoginRequest;
import com.example.entity.Student;
import com.example.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class StudentController {

	@Autowired
	private StudentRepository studentRepository;

	@GetMapping("/students")
	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	@PostMapping("/students")
	public ResponseEntity<Student> addStudent(@Valid @RequestBody Student student) {
		Student savedStudent = studentRepository.save(student);
		return ResponseEntity.status(201).body(savedStudent);
	}

	@PutMapping("/students/{id}")
	public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student student) {
		Optional<Student> existingStudent = studentRepository.findById(id);
		if (existingStudent.isPresent()) {
			try {
				Student updatedStudent = existingStudent.get();
				updatedStudent.setusername(student.getusername());
				updatedStudent.setEmail(student.getEmail());
				updatedStudent.setPassword(student.getPassword());
				studentRepository.save(updatedStudent);
				return ResponseEntity.ok(updatedStudent);
			} catch (Exception e) {
				return ResponseEntity.status(500).body("Error updating student: " + e.getMessage());
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/students/{id}")
	public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
		if (studentRepository.existsById(id)) {
			studentRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/students/login")
	public ResponseEntity<?> loginStudent(@RequestBody LoginRequest loginRequest) {
		List<Student> students = studentRepository.findByusername(loginRequest.getusername());

		if (students.isEmpty()) {
			return ResponseEntity.status(404).body("Student not found");
		} else {
			Student student = students.get(0);
			if (student.getPassword().equals(loginRequest.getPassword())) {
				return ResponseEntity.ok(student);
			} else {
				return ResponseEntity.status(401).body("Invalid password");
			}
		}

	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	 @PostMapping("/students/reset-password")
	    public ResponseEntity<String> resetPassword(@RequestBody Student resetRequest) {
	        String email = resetRequest.getEmail();
	        String newPassword = resetRequest.getPassword();

	        // Validate if email exists
	        Optional<Student> studentOptional = studentRepository.findByEmail(email);

	        if (studentOptional.isPresent()) {
	            Student student = studentOptional.get();
	            student.setPassword(newPassword);  // Set the new password

	            // Save the updated student in the database
	            studentRepository.save(student);

	            return ResponseEntity.ok("Password has been reset successfully.");
	        } else {
	            return ResponseEntity.badRequest().body("Email not found.");
	        }
	    }

}
