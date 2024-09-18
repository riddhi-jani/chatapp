package com.example.chatapp.service;

import com.example.chatapp.exception.ChatException;
import com.example.chatapp.exception.UserException;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.User;
import com.example.chatapp.request.GroupChatReq;

import java.util.List;

public interface ChatService {
    Chat createChat(User reqUser, Integer useId2) throws UserException;
    Chat findChatById(Integer chatId) throws ChatException;
    List<Chat> findAllChatByUserId(Integer userId) throws UserException;
    Chat createGroup(GroupChatReq req, User reqUser) throws UserException;
    Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws ChatException, UserException;
    Chat renameGroup(Integer chatId, String groupName,  User reqUser) throws ChatException, UserException;
    Chat removeFromGroup(Integer chatId, Integer userId,  User reqUser) throws UserException, ChatException;
    void deleteChat(Integer chatId, Integer userId) throws ChatException;
}
