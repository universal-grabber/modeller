package net.tislib.ugm.api.controller;

import lombok.RequiredArgsConstructor;
import net.tislib.ugm.api.service.ModelHtmlProcessorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/model/frame")
public class HtmlProcessorController {

    private final ModelHtmlProcessorService modelHtmlProcessorService;

    @GetMapping(produces = "text/html")
    public String processedFrame(@RequestParam String modelName, @RequestParam Integer exampleId) {
        return modelHtmlProcessorService.processedFrame(modelName, exampleId);
    }

}
