package net.tislib.ugm.data.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import net.tislib.ugm.data.SchemaProperty;
import net.tislib.ugm.data.features.index.IndexFeature;
import net.tislib.ugm.data.features.index.IndexType;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class NumberProperty implements SchemaProperty, IndexFeature {
    private IndexType indexType;

    private Set<String> tags;
}
