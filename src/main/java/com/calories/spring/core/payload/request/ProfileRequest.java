package com.calories.spring.core.payload.request;

import javax.validation.constraints.*;
 
public class ProfileRequest {

    @NotBlank
    @Size(min = 3, max = 300)
    private String name;

    private Integer age;

    @NotBlank
    private String gender;

    private Integer height;

    private Double weight;

    private Double bmi;

    @NotBlank
    @Size(max = 100)
    private String activity;

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
 
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
      
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public Integer getHeight() {
        return height;
    }

    public Double getWeight() {
        return weight;
    }

    public String getActivity() {
        return activity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }
    
}
