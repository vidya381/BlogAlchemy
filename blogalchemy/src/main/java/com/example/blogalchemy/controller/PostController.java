package com.example.blogalchemy.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    public String getAllPosts(Model model, @RequestParam(required = false) String keyword) {
        List<Post> posts;
        if (keyword != null && !keyword.isEmpty()) {
            posts = postService.searchPosts(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            posts = postService.getAllPosts();
        }
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        Optional<Post> postOptional = postService.getPostById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            // Ensure images are loaded
            post.getImages().size(); // This triggers lazy loading
            model.addAttribute("post", post);
            model.addAttribute("newComment", new Comment());
            return "posts/view";
        } else {
            model.addAttribute("errorMessage", "Post not found");
            return "error/404";
        }
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "posts/create";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        model.addAttribute("post", post);
        return "posts/edit";
    }

    @GetMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    @PostMapping("/{postId}/comments")
    public String addComment(@PathVariable Long postId, @ModelAttribute Comment comment,
            @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        User author = userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        comment.setAuthor(author.getUsername());
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());

        commentService.createComment(comment);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/comments/{parentId}/reply")
    public String addReply(@PathVariable Long postId, @PathVariable Long parentId, @ModelAttribute Comment reply,
            @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        Comment parent = commentService.getCommentById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + parentId));
        User author = userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        reply.setAuthor(author.getUsername());
        reply.setCreatedAt(LocalDateTime.now());

        commentService.createReply(reply, post, parent);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{id}/toggle-featured")
    public String toggleFeaturedStatus(@PathVariable Long id) {
        Post post = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        post.setFeatured(!post.isFeatured());
        postService.updatePost(post);
        return "redirect:/posts";
    }

    @PostMapping
    public String createPost(@ModelAttribute Post post,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestParam(value = "publishOption", required = false) String publishOption,
            @RequestParam(value = "scheduleDateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduleDateTime)
            throws IOException {
        User author = userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        post.setAuthor(author);

        if ("schedule".equals(publishOption) && scheduleDateTime != null) {
            post.setScheduledPublishDate(scheduleDateTime);
            post.setPublished(false);
        } else {
            post.setPublished(true);
        }

        postService.createPost(post, imageFiles != null ? imageFiles : Collections.emptyList());
        return "redirect:/posts";
    }

    @PostMapping("/{id}")
    public String updatePost(@PathVariable Long id,
            @ModelAttribute Post post,
            @RequestParam("publishOption") String publishOption,
            @RequestParam(value = "scheduleDateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduleDateTime) {
        Post existingPost = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));

        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());

        if ("schedule".equals(publishOption) && scheduleDateTime != null) {
            existingPost.setScheduledPublishDate(scheduleDateTime);
            existingPost.setPublished(false);
        } else {
            existingPost.setScheduledPublishDate(null);
            existingPost.setPublished(true);
        }

        postService.updatePost(existingPost);
        return "redirect:/posts";
    }

    @PostMapping("/{id}/schedule")
    public String schedulePost(@PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduleDateTime) {
        Post post = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        post.setScheduledPublishDate(scheduleDateTime);
        post.setPublished(false);
        postService.updatePost(post);
        return "redirect:/posts";
    }

}