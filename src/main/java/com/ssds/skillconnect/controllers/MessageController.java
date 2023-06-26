package com.ssds.skillconnect.controllers;

import com.ssds.skillconnect.dao.Message;
import com.ssds.skillconnect.model.MessageModel;
import com.ssds.skillconnect.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("api/v1/message")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{postId}")
    public List<Message> getMessageByPostId(@PathVariable Integer postId) {
        return messageService.getMessageByPostId(postId);
    }

    @PostMapping("/{postId}")
    public Message postMessageByPostId(
            @PathVariable Integer postId,
            @RequestBody MessageModel messageModel,
            @RequestHeader(value="Authorization") String authorizationHeader
    ) {
        return messageService.postMessageByPostId(postId, messageModel, authorizationHeader);
    }


}
