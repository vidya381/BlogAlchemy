package com.example.blogalchemy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.blogalchemy.model.Post;
import com.example.blogalchemy.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByFeaturedTrue();

    List<Post> findByAuthor(User author);

    @Query("SELECT p FROM Post p WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Post> searchPosts(@Param("keyword") String keyword);

    List<Post> findByTitleContainingOrContentContaining(String title, String content);
}