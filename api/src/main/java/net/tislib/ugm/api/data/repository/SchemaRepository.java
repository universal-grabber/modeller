package net.tislib.ugm.api.data.repository;

import net.tislib.ugm.api.data.SchemaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SchemaRepository extends MongoRepository<SchemaEntity, String> {

    Optional<SchemaEntity> findBySchema_Name(String name);

}
