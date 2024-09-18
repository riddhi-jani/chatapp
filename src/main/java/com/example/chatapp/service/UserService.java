package com.example.chatapp.service;

import com.example.chatapp.exception.UserException;
import com.example.chatapp.model.User;
import com.example.chatapp.request.UpdateUserReq;

import java.util.List;

public interface UserService {
    User findByUserId(Integer id) throws UserException;
    User updateUser(Integer userId, UpdateUserReq req) throws UserException;
    List<User> searchUser(String query);
    User findUserProfile(String jwt) throws UserException;
}
