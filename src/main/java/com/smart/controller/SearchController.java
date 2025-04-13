package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@RestController   // ye koi page ya view return nahi karega , puri body return karega as a string
public class SearchController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	//search handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query ,Principal principal) { //Principal is used to get current user
															//query variable normal query aayga jo bheja ja raha hoga frontend se 
		System.out.println(query);
		
		User user	=	userRepository.getUserByUserName(principal.getName());
		
		List<Contact> contacts	=	contactRepository.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
		
	}		// mapping ke query ke query pass hogi pathvariable mein, 
			// fir pricipal ki help se current user ka naam nikaal kar, usko pass kara denge findByNameContainingAndUser mein,
			// contacts ke list me pass karane ke baad isi list ko return kara denge.
			// to isse data serialized form me store ho jayega,..... Serialized matlab data JSON me convert ho jayega
			//But isse User Entity bhi serialized ho jayega kyuki contact entity me user mapped hai, contacts ko he keval json banana hai
			// to isko avoid karne ke liye Contact.java me jakar User ko @JsonIgnore laga dena hai
	
			// yahan backend mein search code banane ke baad, frontend me use me laane ke liye javascript ka istemal kiya jayga
			// jo search bar me query likha jayega usko javascript ke madad se lana hoga iss backend ke controller ke paas
}
