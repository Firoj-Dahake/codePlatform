package com.example.codePlatform.controller;

import com.example.codePlatform.JwtResponse;
import com.example.codePlatform.JwtTokenProvider;
import com.example.codePlatform.repo.UserService;
import com.example.codePlatform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Implement user registration logic, including password encryption
        // Return appropriate response
        userService.saveUser(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        // Implement user login logic
        // Return JWT token or session information
        // Authenticate the user
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            // Generate JWT token
            String jwt = jwtTokenProvider.generateToken((UserDetails) authentication.getPrincipal());

            // Return the token in the response
            return ResponseEntity.ok(new JwtResponse(jwt));
        }  catch (Exception e) { //Authentication
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }
}
