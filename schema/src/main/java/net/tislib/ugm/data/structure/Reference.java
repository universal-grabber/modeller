package net.tislib.ugm.data.structure;

import lombok.Data;

import java.io.Serializable;

@Data
public class Reference implements Serializable {
    String ref;
    String source;
    String sourceUrl;
    String objectType;
    String name;
}
