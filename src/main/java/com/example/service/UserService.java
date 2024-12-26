package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public Optional<User> findByGoogleId(String googleId) {
		return userRepository.findByGoogleId(googleId);
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
