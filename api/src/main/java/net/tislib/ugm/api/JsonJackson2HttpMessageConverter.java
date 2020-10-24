package net.tislib.ugm.api;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

@Component
final class JsonJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {
    JsonJackson2HttpMessageConverter() {
        super(new JsonMapper(), MediaType.parseMediaType("application/json"));
    }
}
