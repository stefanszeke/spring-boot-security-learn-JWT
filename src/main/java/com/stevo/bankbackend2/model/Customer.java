package com.stevo.bankbackend2.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)

  @Column(name = "customer_id")
  private int id;

  private String name;

  private String email;

  @Column(name = "mobile_number")
  private String mobileNumber;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // This is to prevent the password from being returned in the JSON response, but still allow it to be set in the JSON request
  private String pwd;

  private String role;

  @Column(name = "create_dt")
  private String createDt;

  @JsonIgnore // This is to prevent the authorities from being returned in the JSON response
  @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
  private Set<Authority> authorities;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getCreateDt() {
    return createDt;
  }

  public void setCreateDt(String createDt) {
    this.createDt = createDt;
  }

  public Set<Authority> getAuthorities() {
    return this.authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
  }

}
