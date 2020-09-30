package net.tislib.ugm.api.data.repository;

import net.tislib.ugm.api.data.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);
}
