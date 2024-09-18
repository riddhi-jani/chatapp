package com.example.chatapp.controller;

import com.example.chatapp.exception.ChatException;
import com.example.chatapp.exception.MessageException;
import com.example.chatapp.exception.UserException;
import com.example.chatapp.model.Message;
import com.example.chatapp.model.User;
import com.example.chatapp.request.SendMessageReq;
import com.example.chatapp.response.ApiResponse;
import com.example.chatapp.service.MessageService;
import com.example.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageReq req,  @RequestHeader("Authorization") String token) throws ChatException, UserException {
        User user = userService.findUserProfile(token);
        req.setUserId(user.getId());
        Message message = messageService.sendMessage(req);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getChatsMessagesHandler(@PathVariable("chatId") Integer chatId, @RequestHeader("Authorization") String token) throws UserException, ChatException {
        User user = userService.findUserProfile(token);
        List<Message> messages = messageService.getChatsMessages(chatId, user);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
    @GetMapping("/{messageId}")
    public ResponseEntity<Message> getMessageByIdHandler(@PathVariable("messageId") Integer messageId) throws MessageException {
        Message message = messageService.findMessageById(messageId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessage(@PathVariable("messageId") Integer messageId,  @RequestHeader("Authorization") String token) throws UserException, MessageException {
        User user = userService.findUserProfile(token);
        messageService.deleteMessage(messageId, user);
        ApiResponse apiResponse = new ApiResponse("message is deleted successfully ",true);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
