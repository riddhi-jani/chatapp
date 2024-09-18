package com.example.chatapp.service;

import com.example.chatapp.exception.ChatException;
import com.example.chatapp.exception.MessageException;
import com.example.chatapp.exception.UserException;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.Message;
import com.example.chatapp.model.User;
import com.example.chatapp.repository.MessageRepository;
import com.example.chatapp.request.SendMessageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserService userService;
    @Autowired
    ChatService chatService;

    @Override
    public Message sendMessage(SendMessageReq req) throws UserException, ChatException {

        User user = userService.findByUserId(req.getUserId());
        Chat chat = chatService.findChatById(req.getChatId());
        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(req.getContent());
        message.setTimeStamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    @Override
    public List<Message> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException {
        Chat chat = chatService.findChatById(chatId);
        if (!chat.getUsers().contains(reqUser)) {
            throw new UserException("You have not access of this chat " + chat.getId());
        }
        return messageRepository.findByChatId(chat.getId());
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Optional<Message> optional = messageRepository.findById(messageId);
         if (optional.isPresent()){
             return optional.get();
         }
        throw new MessageException("message not found with id "+messageId);
    }

    @Override
    public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException {
        Message message = findMessageById(messageId);
        if (message.getUser().getId().equals(reqUser.getId())){
            messageRepository.deleteById(messageId);
        }
        throw new UserException("You can't delete another user's message "+reqUser.getFull_name());
    }
}
