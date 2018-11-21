package com.longhoang.controllers;

import com.longhoang.models.Post;
import com.longhoang.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/posts/")
    public ResponseEntity<List<Post>> listPost() {
        List<Post> posts = postService.findAll();
        if (posts.isEmpty()) {
            return new ResponseEntity<List<Post>>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/posts/", method = RequestMethod.POST)
    public ResponseEntity<Void> createCustomer(@RequestBody Post post, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating post " + post.getTitle());
        postService.save(post);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/customers/{id}").buildAndExpand(post.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Post> getPost(@PathVariable("id") Long id){
        System.out.println("Fetching post with id " + id);
        Post post = postService.findById(id);
        if (post == null) {
            System.out.println("Not found post with id " + id);
            return new ResponseEntity<Post>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Post>(post, HttpStatus.OK);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") Long id, @RequestBody Post post) {
        System.out.println("Updating post " + id);

        Post currentPost = postService.findById(id);

        if (currentPost == null) {
            System.out.println("Not found post " + id);
            return new ResponseEntity<Post>(HttpStatus.NOT_FOUND);
        }

        currentPost.setTitle(post.getTitle());
        currentPost.setDescription(post.getDescription());
        currentPost.setId(post.getId());

        postService.save(post);
        return new ResponseEntity<Post>(post, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable("id") Long id) {
        System.out.println("Fetching and deleting post " + id);
        Post post = postService.findById(id);
        if (post == null) {
            System.out.println("Not found post " + id);
            return new ResponseEntity<Post>(HttpStatus.NOT_FOUND);
        }
        postService.remove(id);
        return new ResponseEntity<Post>(HttpStatus.NO_CONTENT);
    }

}
