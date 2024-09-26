package com.example.blogalchemy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blogalchemy.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPostId(Long postId);
}