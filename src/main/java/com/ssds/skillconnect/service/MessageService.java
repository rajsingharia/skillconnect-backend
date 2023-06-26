package com.ssds.skillconnect.service;

import com.ssds.skillconnect.config.JwtService;
import com.ssds.skillconnect.dao.Message;
import com.ssds.skillconnect.dao.Post;
import com.ssds.skillconnect.dao.User;
import com.ssds.skillconnect.model.MessageModel;
import com.ssds.skillconnect.repository.MessageRepository;
import com.ssds.skillconnect.repository.PostRepository;
import com.ssds.skillconnect.repository.UserRepository;
import com.ssds.skillconnect.utils.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public List<Message> getMessageByPostId(Integer postId) {
        try {
            return messageRepository.findByPostIdSortedByCreatedOn(postId);
        } catch (Exception e) {
            throw new ApiRequestException("Error while fetching messages");
        }
    }


    public Message postMessageByPostId(Integer postId, MessageModel messageModel, String authorizationHeader) {
        try {
            Post post = postRepository.findById(messageModel.getPostId())
                    .orElseThrow(() -> new ApiRequestException("Post not found"));

            User userCreatedPost = userRepository.findById(post.getUser().getUserId())
                    .orElseThrow(() -> new ApiRequestException("User who created post not found"));

            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);

            User sender = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ApiRequestException("User not found"));

            Message newMessage = new Message();

            newMessage.setMessage(messageModel.getMessage());
            newMessage.setPostedOn(post);
            newMessage.setSender(sender);
            newMessage.setCreatedOn(messageModel.getCreatedOn());

            boolean isAuthor = false;
            if(userCreatedPost != null) {
                isAuthor = Objects.equals(userCreatedPost.getUserId(), sender.getUserId());
            }
            newMessage.setIsAuthor(isAuthor);

            return messageRepository.save(newMessage);

        } catch (Exception e) {
            throw new ApiRequestException("Error while posting message");
        }
    }
}
