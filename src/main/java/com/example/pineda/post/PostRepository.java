package com.example.pineda.post;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface PostRepository extends ListCrudRepository<Post, Integer> {

//    @Query("SELECT p FROM Post p WHERE p.title = :title")
//    Post findByTitle(String title);

    @Query("SELECT * FROM Post WHERE title = :title")
    Optional<Post> findByTitle(String title);
}
