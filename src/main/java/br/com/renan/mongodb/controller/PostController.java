package br.com.renan.mongodb.controller;

import br.com.renan.mongodb.model.Post;
import br.com.renan.mongodb.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/posts")
@RestController
public class PostController {

    @Autowired
    private PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @PostMapping
    public void savePost(@RequestBody Post post) {
        this.postRepository.save(post);
    }

    @PutMapping
    public void updatePost(@RequestBody Post post) {
        this.postRepository.save(post);
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        Post post = this.postRepository.findById(id).get();
        return post;
    }

    @GetMapping()
    public List<Post> getPosts() {
        List<Post> posts = postRepository.findAll();
        return posts;
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        this.postRepository.deleteById(id);
    }

}
