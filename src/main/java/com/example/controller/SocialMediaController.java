package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account registeredAccount = accountService.register(account);
        if (registeredAccount == null) {
            //checks if username is taken
            return ResponseEntity.status(409).build();
        }
        return ResponseEntity.ok(registeredAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account loggedInAccount = accountService.login(account);
        if (loggedInAccount == null) {
            //checks if account credentials are valid
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(loggedInAccount);
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage == null) {
            //checks if the message was created
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok(createdMessage);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
        Integer result = messageService.deleteMessage(messageId);
        if (result == null) {
            //checks if the message was deleted by the id if no message existed still returns ok
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Message message) {
        Integer result = messageService.updateMessage(messageId, message);
        if (result == null) {
            //checks if message exists for the update
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
        return ResponseEntity.ok(messageService.getMessagesByUser(accountId));
    }
}

