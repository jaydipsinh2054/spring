package com.example.controller;

import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userRepository.findAll();
		return ResponseEntity.ok(users);
	}

	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {
		Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

		if (existingUser.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		User savedUser = userRepository.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}

	@GetMapping("/check")
	public ResponseEntity<Map<String, Boolean>> checkIfUserExists(@RequestParam String googleId) {
		boolean exists = userRepository.existsByGoogleId(googleId);
		Map<String, Boolean> response = new HashMap<>();
		response.put("exists", exists);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
		Optional<User> existingUser = userRepository.findById(id);
		if (existingUser.isPresent()) {
			user.setId(id);
			userRepository.save(user);
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		Optional<User> existingUser = userRepository.findById(id);
		if (existingUser.isPresent()) {
			userRepository.delete(existingUser.get());
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}
