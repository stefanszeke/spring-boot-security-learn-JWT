package com.stevo.bankbackend2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stevo.bankbackend2.model.Accounts;
import com.stevo.bankbackend2.repository.AccountsRepository;

@RestController
public class AccountController {

  @Autowired AccountsRepository accountsRepository;

  @GetMapping("/myAccount")
  public Accounts getAccountDetails(@RequestParam int id) {
    Accounts accounts = accountsRepository.findByCustomerId(id);

    if(accounts == null) { return null; }

    return accounts;
  }

}
