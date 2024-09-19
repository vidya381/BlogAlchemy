package com.example.blogalchemy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.blogalchemy.model.Comment;
import com.example.blogalchemy.model.Post;
import com.example.blogalchemy.model.User;
import com.example.blogalchemy.service.CommentService;
import com.example.blogalchemy.service.PostService;
import com.example.blogalchemy.service.UserService;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, CommentService commentService, UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
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
    public String createPost(@ModelAttribute Post post, @AuthenticationPrincipal UserDetails userDetails) {
        User author = userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        post.setAuthor(author);
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
    public String addComment(@PathVariable Long postId, @ModelAttribute Comment comment, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.getPostById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        User author = userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        comment.setAuthor(author.getUsername());
        commentService.createComment(comment, post, null);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/comments/{parentId}/reply")
    public String addReply(@PathVariable Long postId, @PathVariable Long parentId, @ModelAttribute Comment reply, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.getPostById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        Comment parent = commentService.getCommentById(parentId).orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + parentId));
        User author = userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        reply.setAuthor(author.getUsername());
        commentService.createComment(reply, post, parent);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{id}/feature")
    public String featurePost(@PathVariable Long id) {
        Post post = postService.getPostById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        post.setFeatured(!post.isFeatured());
        postService.updatePost(post);
        return "redirect:/posts";
    }
}