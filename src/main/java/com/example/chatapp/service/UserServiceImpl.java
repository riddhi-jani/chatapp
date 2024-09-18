package com.example.chatapp.service;

import com.example.chatapp.config.TokenProvider;
import com.example.chatapp.exception.UserException;
import com.example.chatapp.model.User;
import com.example.chatapp.repository.UserRepository;
import com.example.chatapp.request.UpdateUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenProvider tokenProvider;
    @Override
    public User findByUserId(Integer id) throws UserException {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        throw new UserException("User Not Found With id "+id);
    }

    @Override
    public User updateUser(Integer userId, UpdateUserReq req) throws UserException {
        User user = findByUserId(userId);
        if (req.getFull_name()!=null){
            user.setFull_name(req.getFull_name());
        }
        if (req.getProfile_picture()!=null){
            user.setProfile_picture(req.getProfile_picture());
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String query) {
        return userRepository.searchUser(query);
    }

    @Override
    public User findUserProfile(String jwt) throws UserException {
        String email = tokenProvider.getEmailFromToken(jwt);
        if(email!=null){
            throw new BadCredentialsException("received invalid token.....");
        }

        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UserException("user not found with email "+email);
        }

        return user;
    }
}
