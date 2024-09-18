package com.example.chatapp.service;

import com.example.chatapp.exception.ChatException;
import com.example.chatapp.exception.MessageException;
import com.example.chatapp.exception.UserException;
import com.example.chatapp.model.Message;
import com.example.chatapp.model.User;
import com.example.chatapp.request.SendMessageReq;

import java.util.List;

public interface MessageService {
    Message sendMessage(SendMessageReq req) throws UserException, ChatException;
    List<Message> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException;
    Message findMessageById(Integer messageId) throws MessageException;
    void deleteMessage(Integer messageId,  User reqUser) throws MessageException, UserException;

}
