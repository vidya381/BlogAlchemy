package com.example.blogalchemy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blogalchemy.model.Post;
import com.example.blogalchemy.model.User;
import com.example.blogalchemy.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public List<Post> getFeaturedPosts() {
        return postRepository.findByFeaturedTrue();
    }

    public void toggleFeaturedStatus(Long postId) {
        Post post = getPostById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        post.setFeatured(!post.isFeatured());
        postRepository.save(post);
    }

    public List<Post> getPostsByAuthor(User author) {
        return postRepository.findByAuthor(author);
    }

    public List<Post> searchPosts(String keyword) {
        return postRepository.searchPosts(keyword);
    }
}