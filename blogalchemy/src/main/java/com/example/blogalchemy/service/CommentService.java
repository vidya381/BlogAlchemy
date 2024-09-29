package com.example.blogalchemy.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blogalchemy.model.Comment;
import com.example.blogalchemy.model.Post;
import com.example.blogalchemy.repository.CommentRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

     @Transactional
    public Comment createReply(Comment reply, Post post, Comment parent) {
        reply.setPost(post);
        reply.setParent(parent);
        return commentRepository.save(reply);
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}