package com.example.chatapp.service;

import com.example.chatapp.exception.ChatException;
import com.example.chatapp.exception.UserException;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.User;
import com.example.chatapp.repository.ChatRepository;
import com.example.chatapp.request.GroupChatReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UserService userService;

    @Override
    public Chat createChat(User reqUser, Integer useId2) throws UserException {
        User user = userService.findByUserId(useId2);
        Chat chat = chatRepository.findSingleChatByUserIds(user, reqUser);
        if (chat != null) {
            return chat;
        }
        chat = new Chat();
        chat.setCreatedBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
        chat.setGroup(false);
        return chat;
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isPresent()) {
            return chat.get();
        }
        throw new ChatException("chat not found with id  " + chatId);
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException {
        User user = userService.findByUserId(userId);
        return chatRepository.findChatByUserId(user.getId());
    }

    @Override
    public Chat createGroup(GroupChatReq req, User reqUser) throws UserException {
        Chat group = new Chat();
        group.setGroup(true);
        group.setChat_image(req.getChat_image());
        group.setChat_name(req.getChat_name());
        group.setCreatedBy(reqUser);
        group.getAdmins().add(reqUser);
        for (Integer userId : req.getUserIds()) {
            User user = userService.findByUserId(userId);
            group.getUsers().add(user);
        }

        return group;
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws ChatException, UserException {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        User user = userService.findByUserId(userId);
        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            if (chat.getAdmins().contains(reqUser)) {
                chat.getUsers().add(user);
                return chatRepository.save(chat);
            } else {
                throw new UserException("Only admin can  add user");
            }
        }
        throw new ChatException("chat not found with id  " + chatId);
    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            if (chat.getUsers().contains(reqUser)) {
                chat.setChat_name(groupName);
                return chatRepository.save(chat);
            }
            throw new UserException("You are nto member of this group ");
        }
        throw new ChatException("chat not found with id  " + chatId);
    }

    @Override
    public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        User user = userService.findByUserId(userId);
        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            if (chat.getAdmins().contains(reqUser)) {
                chat.getUsers().remove(user);
                return chatRepository.save(chat);
            } else if (chat.getUsers().contains(reqUser)) {
                if (user.getId().equals(reqUser.getId())) {
                    chat.getUsers().remove(user);
                    return chatRepository.save(chat);
                }
                throw new UserException("Only admin can  remove another user");
            }
        }
        throw new ChatException("chat not found with id  " + chatId);
    }

    @Override
    public void deleteChat(Integer chatId, Integer userId) throws ChatException {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            chatRepository.deleteById(chat.getId());
        }
        throw new ChatException("chat not found with id  " + chatId);
    }
}
