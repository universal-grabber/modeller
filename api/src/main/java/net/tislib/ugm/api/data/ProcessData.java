package net.tislib.ugm.api.data;

import lombok.Data;
import net.tislib.ugm.api.data.model.Model;
import net.tislib.ugm.data.Schema;

import java.util.Set;

@Data
public class ProcessData {
    private Model model;
    private Set<Model> additionalModels;
    private Schema schema;

    private String html;
    private String url;
}
