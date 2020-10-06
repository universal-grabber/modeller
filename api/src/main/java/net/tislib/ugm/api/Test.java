package net.tislib.ugm.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;
import net.tislib.ugm.api.component.TimeCalc;
import net.tislib.ugm.lib.markers.base.ModelDataExtractor;
import net.tislib.ugm.lib.markers.base.model.Model;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    public static void main(String[] args) throws IOException {
        String pageUrl = "https://www.imdb.com/title/tt0133093";
        Path path = null;
        String html = FileUtils.readFileToString(new File("/root/universal-grabber-modeller/api/src/main/resources/html/the_matrix_main.html"));
        String modelUrl = "http://ugm.ug.tisserv.net/api/1.0/models?name=imdb/movie&token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0YWxlaCIsImV4cCI6MTkxNzEwOTU1NX0.VQUVPpHuUjQLYgMdit58oXSr1Wp1LeWDXUIpTrA0Ubzl1F3Rz2QHkZJUxVjkNQOlY8DI97wgxRN8nY_KZuaEAw";

        ObjectMapper objectMapper = new ObjectMapper();
        Model model = objectMapper.readValue(new URL(modelUrl), Model.class);

        TimeCalc timeCalc = new TimeCalc();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ModelDataExtractor modelDataExtractor = new ModelDataExtractor();

        for (int i = 0; i < 1000000; i++) {
            executorService.submit(() -> {
//                Jsoup.parse(html);
                Serializable res = modelDataExtractor.processDocument(model, pageUrl, html);
                timeCalc.printSpeedStep();
            });
        }

        System.out.println("Done");
    }

}
