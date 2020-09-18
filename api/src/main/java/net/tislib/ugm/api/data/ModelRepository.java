package net.tislib.ugm.api.data;

import net.tislib.ugm.lib.markers.base.model.Model;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ModelRepository extends MongoRepository<Model, String> {

    Optional<Model> findByName(String name);

    @Query("db.model.updateMany({name: ?0}, {$set: $1})")
    void update(String name, Model model);
}
