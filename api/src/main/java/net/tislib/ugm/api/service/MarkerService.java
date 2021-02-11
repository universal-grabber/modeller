package net.tislib.ugm.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.tislib.ugm.api.data.marker.Marker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.Unsafe;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkerService {

    @Value("${service.modeller-processor}")
    private String modelProcessorService;

    private final ObjectMapper mapper;

    @SneakyThrows
    public List<Marker> getAll() {
        String jsonData = Unirest.get(modelProcessorService + "/api/1.0/markers")
                .asString()
                .getBody();

        return mapper.readValue(jsonData, new TypeReference<List<Marker>>() {});
    }

    public Marker get(String name) {
        return getAll().stream()
                .filter(item -> item.getName().equals(name)).findAny()
                .orElseThrow(RuntimeException::new);
    }
}
