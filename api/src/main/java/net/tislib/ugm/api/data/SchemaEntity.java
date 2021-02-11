package net.tislib.ugm.api.data;

import lombok.Data;
import net.tislib.ugm.data.Schema;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("schema")
public class SchemaEntity extends Schema {
    private String id;
}
