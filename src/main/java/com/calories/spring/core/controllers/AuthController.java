package com.calories.spring.core.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calories.spring.core.models.EGender;
import com.calories.spring.core.models.ERole;
import com.calories.spring.core.models.Profile;
import com.calories.spring.core.models.Role;
import com.calories.spring.core.models.User;
import com.calories.spring.core.payload.request.LoginRequest;
import com.calories.spring.core.payload.request.SignupRequest;
import com.calories.spring.core.payload.response.MessageResponse;
import com.calories.spring.core.payload.response.UserInfoResponse;
import com.calories.spring.core.repository.RoleRepository;
import com.calories.spring.core.repository.UserRepository;
import com.calories.spring.core.security.jwt.JwtUtils;
import com.calories.spring.core.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(new UserInfoResponse(userDetails.getId(),
                                   userDetails.getUsername(),
                                   userDetails.getEmail(), jwtUtils.getJwt_token(),
                                   roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                         signUpRequest.getEmail(),
                         encoder.encode(signUpRequest.getPassword()));

    Profile profile = new Profile();
    switch(signUpRequest.getGender().toLowerCase()){
      case "male" :
        profile.setGender(EGender.MALE);
        break;
      case "female" :
        profile.setGender(EGender.FEMALE);
        break;
      default:
        profile.setGender(EGender.NOT_SPECIFIED);
        break;
    }
    
    String activity;

    switch(signUpRequest.getActivity().toLowerCase()){
      case "base" :
        activity = "Basic metabolic rate";
        break;
      case "little" :
        activity = "Little or no excercise";
        break;
      case "moderate" :
        activity = "Moderate 2 - 3 times a week";
        break;
      case "active" :
        activity = "Active, daily exercise 4 - 5 times a week";
        break;
      case "very" :
        activity = "Very Active,  intense exercise 6 - 7 times a week";
        break;
      case "extra" :
        activity = "Physical job and extremely active";
        break;
      default:
        activity = "Basic metabolic rate";;
        break;
    }


    double bmi = signUpRequest.getHeight()/Math.sqrt(signUpRequest.getWeight()) ;
    profile.setAge(signUpRequest.getAge());
    profile.setHeight(signUpRequest.getHeight());
    profile.setWeight(signUpRequest.getWeight());
    profile.setBmi(bmi);
    profile.setActivity(activity);

    Set<Role> roles = new HashSet<>();
    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));  
    roles.add(userRole);
   

    user.setRoles(roles);
    user.setProfile(profile);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }
}
