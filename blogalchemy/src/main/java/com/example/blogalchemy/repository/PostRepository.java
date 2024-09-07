package com.example.blogalchemy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blogalchemy.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}