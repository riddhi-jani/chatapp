package com.example.chatapp.controller;

import com.example.chatapp.exception.ChatException;
import com.example.chatapp.exception.UserException;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.User;
import com.example.chatapp.request.GroupChatReq;
import com.example.chatapp.request.SingleChatReq;
import com.example.chatapp.response.ApiResponse;
import com.example.chatapp.service.ChatService;
import com.example.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    ChatService chatService;
    @Autowired
    UserService userService;

    @PostMapping("/single")
    public ResponseEntity<Chat> createChatHandler(@RequestBody SingleChatReq singleChatReq, @RequestHeader("Authorization") String token) throws UserException {
        User reqUser = userService.findUserProfile(token);
        Chat chat = chatService.createChat(reqUser, singleChatReq.getUserId());
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupHandler(@RequestBody GroupChatReq groupChatReq, @RequestHeader("Authorization") String token) throws UserException {
        User reqUser = userService.findUserProfile(token);
        Chat chat = chatService.createGroup(groupChatReq, reqUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> getChatByIdHandler(@PathVariable("chatId") Integer chatId, @RequestHeader("Authorization") String token) throws UserException, ChatException {
        Chat chat = chatService.findChatById(chatId);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Chat>> getAllChatHandler(@RequestHeader("Authorization") String token) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(token);
        List<Chat> chats = chatService.findAllChatByUserId(reqUser.getId());
        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToGroupHandler(@PathVariable("chatId")Integer chatId, @PathVariable("userId")Integer userId, @RequestHeader("Authorization") String token) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(token);
        Chat chat = chatService.addUserToGroup(chatId, userId, reqUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/rename/{groupName}")
    public ResponseEntity<Chat> renameGroupHandler(@PathVariable("chatId")Integer chatId, @PathVariable("groupName")String groupName, @RequestHeader("Authorization") String token) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(token);
        Chat chat = chatService.renameGroup(chatId, groupName, reqUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeUserFromGroupHandler(@PathVariable("chatId")Integer chatId, @PathVariable("userId")Integer userId, @RequestHeader("Authorization") String token) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(token);
        Chat chat = chatService.removeFromGroup(chatId, userId, reqUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deleteChatHandler(@PathVariable("chatId")Integer chatId, @RequestHeader("Authorization") String token) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(token);
        chatService.deleteChat(chatId, reqUser.getId());
        ApiResponse apiResponse = new ApiResponse("chat is deleted successfully ",true);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
