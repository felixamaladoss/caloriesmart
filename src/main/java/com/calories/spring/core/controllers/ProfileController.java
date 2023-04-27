package com.calories.spring.core.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Producer;
import org.springframework.web.bind.annotation.RequestBody;

import com.calories.spring.core.models.EGender;
import com.calories.spring.core.models.Profile;
import com.calories.spring.core.models.User;
import com.calories.spring.core.payload.request.ProfileRequest;
import com.calories.spring.core.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

  @Autowired
  UserRepository userRepository;

  @GetMapping("/my")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<ProfileRequest>  getProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();
    
    Optional<User> useropt = userRepository.findByUsername(currentPrincipalName);
    ProfileRequest profilereq = new ProfileRequest();
    if(useropt.isPresent()){
      User user = useropt.get();
      Profile prof = user.getProfile();
      profilereq.setName(user.getName());
      profilereq.setAge(prof.getAge());
      profilereq.setGender(prof.getGender().name());
      profilereq.setHeight(prof.getHeight());
      profilereq.setWeight(prof.getWeight());
      profilereq.setActivity(prof.getActivity());
      profilereq.setBmi(prof.getBmi());
      profilereq.setUsername(user.getUsername());
      profilereq.setEmail(user.getEmail());
    }
    return ResponseEntity.ok().body(profilereq);

  }

  @PostMapping("/update")
  public ResponseEntity<?> registerUser(@Valid @RequestBody  ProfileRequest profReq) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();
    Optional<User> useropt = userRepository.findByUsername(currentPrincipalName);

    if(useropt.isPresent()){
      User user = useropt.get();
      user.setName(profReq.getName());
      user.setEmail(profReq.getEmail());
      Profile profile = user.getProfile();
      profile.setAge(profReq.getAge());
      switch(profReq.getGender().toLowerCase()){
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
      profile.setHeight(profReq.getHeight());
      profile.setWeight(profReq.getWeight());

      String activity;

    switch(profReq.getActivity().toLowerCase()){
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
    double bmi = profReq.getHeight()/Math.sqrt(profReq.getWeight()) ;
    profile.setBmi(bmi);
    profile.setActivity(activity);
    user.setProfile(profile);
    userRepository.save(user);
    profReq.setBmi(bmi);
    profReq.setActivity(activity);
    return ResponseEntity.accepted().body(profReq);
    }
    else return ResponseEntity.badRequest().body(new ProfileRequest());
  
  }
}
