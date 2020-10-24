package net.tislib.ugm.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Locale;

@ShellComponent
@RequiredArgsConstructor
public class SchemaApplyCommands {

//    private final SchemaService service;

    @ShellMethod("Translate text from one language to another.")
    public String translate(
            @ShellOption() String text,
            @ShellOption(defaultValue = "en_US") Locale from,
            @ShellOption() Locale to
    ) {
        // invoke service
//        return service.get("asd").getName();

        return text;
    }
}
