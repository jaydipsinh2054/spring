package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Contact;
import com.example.repository.ContactRepository;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

	@Autowired
	private ContactRepository contactRepository;

	@PostMapping
	public Contact createContact(@RequestBody Contact contact) {
		return contactRepository.save(contact);
	}

	@GetMapping
	public List<Contact> getAllContacts() {
		return contactRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
		Contact contact = contactRepository.findById(id).orElse(null);
		if (contact == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(contact);
	}

	// Update a contact
	@PutMapping("/{id}")
	public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
		Contact contact = contactRepository.findById(id).orElse(null);
		if (contact == null) {
			return ResponseEntity.notFound().build();
		}

		contact.setName(updatedContact.getName());
		contact.setEmail(updatedContact.getEmail());
		contact.setMessage(updatedContact.getMessage());

		Contact savedContact = contactRepository.save(contact);
		return ResponseEntity.ok(savedContact);
	}

	// Delete a contact
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
		if (!contactRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		contactRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
