package net.tislib.ugm.data.structure;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Record extends Reference {

    private UUID id;

    private Set<String> tags;

    private String description;

    private Map<String, Object> data;

    private Map<String, String> meta;

    private String schema;
    
    private Long publishDate;

    private Long modifyDate;

}
