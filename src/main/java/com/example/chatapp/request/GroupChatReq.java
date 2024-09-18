package com.example.chatapp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatReq {
    private List<Integer> userIds;
    private String chat_name;
    private String chat_image;

}
