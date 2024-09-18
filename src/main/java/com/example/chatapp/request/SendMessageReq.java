package com.example.chatapp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageReq {

    Integer chatId;
    Integer userId;
    String content;
}
