package br.com.renan.mongodb;

import br.com.renan.mongodb.controller.PostController;
import br.com.renan.mongodb.model.Post;
import br.com.renan.mongodb.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class MongodbApplicationTests {

    @Autowired
    PostRepository postRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PostController postController;

    MockMvc mvc;
    Post post;

    @BeforeEach
    void initConfig() {
        mvc = MockMvcBuilders
                .standaloneSetup(postController)
                .build();
        initPostObj();
    }

    @Test
    void savePost() throws Exception {
        this.mvc.perform(post("/posts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(post))).andExpect(status().isOk());
    }

    @Test
    void updatePost() throws Exception {
        String newContent = "New Content";
        post.setContent(newContent);
        this.mvc.perform(put("/posts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(post))).andExpect(status().isOk());
        this.mvc.perform(get("/posts/{id}", post.getId())).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(post.getId().intValue()))
                .andExpect(jsonPath("$.content").value(newContent))
                .andExpect(jsonPath("$.date").value(this.formatDate(post.getDate())));
    }

    @Test
    void getPostById() throws Exception {
        this.postRepository.save(post);
        this.mvc.perform(get("/posts/{id}", post.getId())).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(post.getId().intValue()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andExpect(jsonPath("$.date").value(this.formatDate(post.getDate())));
    }

    @Test
    void getAllPost() throws Exception {
        this.postRepository.save(post);
        this.mvc.perform(get("/posts", post.getId())).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(post.getContent())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(this.formatDate(post.getDate()))));
    }

    @Test
    void deletePost() throws Exception {
        this.postRepository.save(post);
        this.mvc.perform(delete("/posts/{id}", post.getId())).andExpect(status().isOk());
    }

    private void initPostObj() {
        LocalDate localDate = LocalDate.of(2020, 10, 9);
        post = new Post();
        post.setId(1l);
        post.setContent("Post 1");
        post.setDate(localDate);
    }

    private String formatDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDate.format(formatter);
    }

}
