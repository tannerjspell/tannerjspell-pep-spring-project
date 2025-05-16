package com.example.service;
import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountService accountService;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountService accountService) {
        this.messageRepository = messageRepository;
        this.accountService = accountService;
    }

    public Message createMessage(Message message) {
        if (!isValidMessage(message)) {
            //checks if message is valid
            return null;
        }
        
        if (accountService.findById(message.getPostedBy()) == null) {
            //checks if user exists
            return null;
        }
        
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    public List<Message> getMessagesByUser(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

    public Integer deleteMessage(Integer messageId) {
        if (!messageRepository.existsById(messageId)) {
            return null;
        }
        messageRepository.deleteById(messageId);
        return 1;
    }

    public Integer updateMessage(Integer messageId, Message message) {
        // Validates the new message text checks if its a valid update not null blank or too long
        if (message.getMessageText() == null || 
            message.getMessageText().isBlank() || 
            message.getMessageText().length() > 255) {
            return null;
        }
        
        // checks if the message does exist
        Message existingMessage = messageRepository.findById(messageId).orElse(null);
        if (existingMessage == null) {
            return null;
        }
        
        existingMessage.setMessageText(message.getMessageText());
        messageRepository.save(existingMessage);
        return 1;
    }

    private boolean isValidMessage(Message message) {
        return message.getMessageText() != null &&
               !message.getMessageText().isBlank() &&
               message.getMessageText().length() <= 255 &&
               message.getPostedBy() != null;
    }
}