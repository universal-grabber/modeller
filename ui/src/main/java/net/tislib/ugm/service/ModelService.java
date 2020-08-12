package net.tislib.ugm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.tislib.ugm.model.Model;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ModelService {

    String wdPath = "/home/taleh/Projects/UniersalDataPlatform/ugm-backend/wd";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public Model get(String name) {
        File file = new File(wdPath + "/" + name + ".json");

        if (!file.exists()) {
            throw new RuntimeException("model not found:" + name);
        }

        return objectMapper.readValue(file, Model.class);
    }

    @SneakyThrows
    public void saveModel(Model model) {
        File file = new File(wdPath + "/" + model.getId() + ".json");

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, model);
    }
}
