package com.example.blogalchemy.controller;

import com.example.blogalchemy.model.Comment;
import com.example.blogalchemy.model.Post;
import com.example.blogalchemy.service.CommentService;
import com.example.blogalchemy.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @Autowired
    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    public String getAllPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        model.addAttribute("post", post);
        model.addAttribute("newComment", new Comment());
        return "posts/view";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "posts/create";
    }

    @PostMapping
    public String createPost(@ModelAttribute Post post) {
        postService.createPost(post);
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        model.addAttribute("post", post);
        return "posts/edit";
    }

    @PostMapping("/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post) {
        post.setId(id);
        postService.updatePost(post);
        return "redirect:/posts";
    }

    @GetMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    @PostMapping("/{postId}/comments")
    public String addComment(@PathVariable Long postId, @ModelAttribute Comment comment) {
        Post post = postService.getPostById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        commentService.createComment(comment, post, null);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/comments/{parentId}/reply")
    public String addReply(@PathVariable Long postId, @PathVariable Long parentId, @ModelAttribute Comment reply) {
        Post post = postService.getPostById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        Comment parent = commentService.getCommentById(parentId).orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + parentId));
        commentService.createComment(reply, post, parent);
        return "redirect:/posts/" + postId;
    }
}