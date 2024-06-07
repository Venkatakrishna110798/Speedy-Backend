package com.msmavas.speedy.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msmavas.speedy.models.User;
import com.msmavas.speedy.services.UserService;

@RestController
@RequestMapping("/User") 
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<String> registerCustomer(@RequestBody User user){ 
		return new ResponseEntity<>(userService.register(user), HttpStatus.OK);
	}


//	@PostMapping("/verifyCustomer")
//	public ResponseEntity<String> verifyUser(@RequestBody Map<String, String> requestData) {
//	    String email = requestData.get("email");
//	    String otp = requestData.get("otp");
//	    return new ResponseEntity<>(userService.verifyCustomer(email, otp), HttpStatus.OK);
//	}
	@PostMapping("/verifyCustomer")
    public ResponseEntity<String> verifyUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        try {
            userService.verifyUser(email, otp);
            return ResponseEntity.ok("User verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User customer) {
        User loggedInCustomer = userService.login(customer.getEmail(), customer.getPassword());
        if (loggedInCustomer != null) {
            return ResponseEntity.ok(loggedInCustomer); 
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
	

	 @GetMapping("/getCustomer/{id}")
	    public ResponseEntity<User> getCustomerById(@PathVariable int id) {
	        User customer = userService.getCustomerById(id);
	        if (customer != null) {
	            return ResponseEntity.status(HttpStatus.OK).body(customer);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }
	 }
	
	
	@PutMapping("/updateCustomer/{id}") 
	public ResponseEntity<User> updateCustomerById(@PathVariable int id, @RequestBody User newCustomer) { 
		User updatedCustomer = userService.updateCustomerById(id, newCustomer); 
		return ResponseEntity.ok(updatedCustomer);
	}
	
	@DeleteMapping("/deleteUser/{id}") 
	public ResponseEntity<Void> deleteCustomerById(@PathVariable int id) { 
		userService.deleteCustomerById(id);
		return ResponseEntity.noContent().build();
	}
	 @GetMapping("/getAllUser")
	    public ResponseEntity<List<User>> getAllCustomers() {
	        List<User> customers = userService.getAllCustomers();
	        return ResponseEntity.status(HttpStatus.OK).body(customers);
	    }
}
