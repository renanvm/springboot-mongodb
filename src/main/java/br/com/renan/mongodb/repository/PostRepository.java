package br.com.renan.mongodb.repository;

import br.com.renan.mongodb.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, Long> {

}
