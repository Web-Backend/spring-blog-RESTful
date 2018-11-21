package com.longhoang.services;

import com.longhoang.models.Post;

import java.util.List;

public interface PostService {
    List<Post> findAll();

    Post findById(Long id);

    void save(Post post);

    void remove(Long id);
}
