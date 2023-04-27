package com.calories.spring.core.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "profile")
public class Profile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Integer age;

  private Integer height;

  private Double weight;

  private Double bmi;

  @Column(length = 100)
  private String activity;
  
  @Enumerated(EnumType.STRING)
  @Column(length = 10)
  private EGender gender;

  @OneToOne(mappedBy = "profile")
  private User user;


  public Profile() {

  }

  public Integer getId() {
    return id;
  }

  public Integer getAge() {
    return age;
  }

  public Integer getHeight() {
    return height;
  }

  public Double getWeight() {
    return weight;
  }

  public Double getBmi() {
    return bmi;
  }

  public String getActivity() {
    return activity;
  }

  public EGender getGender() {
    return gender;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public void setBmi(Double bmi) {
    this.bmi = bmi;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  public void setGender(EGender gender) {
    this.gender = gender;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  

}