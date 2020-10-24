package net.tislib.ugm.data.property;

import lombok.Data;
import net.tislib.ugm.data.SchemaProperty;
import net.tislib.ugm.data.features.index.IndexFeature;
import net.tislib.ugm.data.features.index.IndexType;

@Data
public class NumberProperty implements SchemaProperty, IndexFeature {
    private IndexType indexType;
}
