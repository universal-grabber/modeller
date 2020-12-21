package net.tislib.ugm.data.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import net.tislib.ugm.data.Example;
import net.tislib.ugm.data.SchemaProperty;
import net.tislib.ugm.data.features.index.IndexFeature;
import net.tislib.ugm.data.features.index.IndexType;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class StringProperty implements SchemaProperty, IndexFeature {
    private String pattern;
    private IndexType indexType = IndexType.MATCH;
    private Set<String> tags;

    private String description;

    private boolean allowQuery = true;

    private Example example;
}
