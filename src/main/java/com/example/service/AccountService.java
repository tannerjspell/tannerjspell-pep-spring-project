package com.example.service;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(Account account) {
        // Checks account registration if username is blank or if password is blank or too short
        if (!isValidAccount(account)) {
            return null;
        }
        
        // checks if username exists
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            return null;
        }
        
        return accountRepository.save(account);
    }

    public Account login(Account account) {
        //Checks if the username or password is null
        if (account.getUsername() == null || account.getPassword() == null) {
            return null;
        }
        
        // checks if the username and password match an existing account
        Account existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount == null || !existingAccount.getPassword().equals(account.getPassword())) {
            return null;
        }
        
        return existingAccount;
    }

    public Account findById(Integer accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    private boolean isValidAccount(Account account) {
        return account.getUsername() != null && 
               !account.getUsername().isBlank() && 
               account.getPassword() != null && 
               account.getPassword().length() >= 4;
    }
}