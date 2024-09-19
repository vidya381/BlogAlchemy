package com.example.blogalchemy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blogalchemy.model.Post;
import com.example.blogalchemy.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByFeaturedTrue();

    List<Post> findByAuthor(User author);
}