package com.example.blogalchemy.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.blogalchemy.model.Image;
import com.example.blogalchemy.model.Post;
import com.example.blogalchemy.model.User;
import com.example.blogalchemy.repository.ImageRepository;
import com.example.blogalchemy.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    // private final ImageRepository imageRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    @Autowired
    public PostService(PostRepository postRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id).map(post -> {
            Hibernate.initialize(post.getImages());
            return post;
        });
    }

    public Post createPost(Post post, List<MultipartFile> imageFiles) throws IOException {
        Post savedPost = postRepository.save(post);

        for (MultipartFile imageFile : imageFiles) {
            if (!imageFile.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                Image image = new Image();
                image.setFileName(fileName);
                image.setFilePath(filePath.toString());
                image.setPost(savedPost);
                imageRepository.save(image);
            }
        }

        return savedPost;
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

    public List<Post> getPostsByAuthor(User author) {
        return postRepository.findByAuthor(author);
    }

    // public List<Post> searchPosts(String keyword) {
    // return postRepository.searchPosts(keyword);
    // }

    public List<Post> searchPosts(String keyword) {
        return postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }
}