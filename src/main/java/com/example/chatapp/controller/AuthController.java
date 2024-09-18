package com.example.chatapp.controller;

import com.example.chatapp.config.TokenProvider;
import com.example.chatapp.exception.UserException;
import com.example.chatapp.model.User;
import com.example.chatapp.repository.UserRepository;
import com.example.chatapp.request.LoginRequest;
import com.example.chatapp.response.AuthResponse;
import com.example.chatapp.service.CustomUserService;
import com.example.chatapp.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    PasswordEncoder passwordEncoder;
    TokenProvider tokenProvider;
    @Autowired
    CustomUserService customUserService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String full_name = user.getFull_name();
        String password = user.getPassword();

        User isUser  = userRepository.findByEmail(email);
        if (isUser!=null){
            throw new UserException("Email is used with another account "+email);
        }
        User createdUser = new User();
        BeanUtils.copyProperties(user, createdUser);
        createdUser.setPassword(passwordEncoder.encode(password));

        userRepository.save(createdUser);

        Authentication authentication= new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse(jwt, true);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest request){
        String email = request.getEmail();
        String password = request.getPassword();
        Authentication authentication = authentication(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse(jwt, true);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    public Authentication authentication(String username, String password){
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        if (userDetails==null){
            throw  new BadCredentialsException("invalid username");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("invalid password or username");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
