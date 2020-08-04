package net.tislib.ugm.html.impl;

import lombok.RequiredArgsConstructor;
import net.tislib.ugm.html.lib.core.DomAccess;
import net.tislib.ugm.html.lib.core.Element;
import org.jsoup.nodes.Document;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JsoupDomAccess implements DomAccess {
    private final Document document;

    @Override
    public Element access() {
        return new JsoupElement(document);
    }

    @RequiredArgsConstructor
    private static class JsoupElement implements Element {
        private final org.jsoup.nodes.Element element;

        @Override
        public Element select(String cssSelector) {
            return new JsoupElement(element.select(cssSelector).first());
        }

        @Override
        public Iterable<Element> selectMulti(String cssSelector) {
            return element.select(cssSelector).stream()
                    .map(JsoupElement::new)
                    .collect(Collectors.toList());
        }

        @Override
        public String text() {
            return element.text();
        }

        @Override
        public String attr(String key) {
            return element.attr(key);
        }

        @Override
        public void attr(String key, String value) {
            element.attr(key, value);
        }

        @Override
        public Element parent() {
            return new JsoupElement(element.parent());
        }
    }
}
