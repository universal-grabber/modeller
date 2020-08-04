package net.tislib.ugm.html.jsimpl;

import net.tislib.ugm.html.lib.core.DomAccess;
import net.tislib.ugm.html.lib.core.Element;

public class JsDomAccess implements DomAccess {

    private final def.dom.Element element;

    public JsDomAccess(def.dom.Element element) {
        this.element = element;
    }

    @Override
    public Element access() {
        return new WindowElement(element);
    }

    private static class WindowElement implements Element {

        private final def.dom.Element element;

        private WindowElement(def.dom.Element element) {
            this.element = element;
        }

        @Override
        public Element select(String cssSelector) {
            return new WindowElement(element.querySelector(cssSelector));
        }

        @Override
        public Iterable<Element> selectMulti(String cssSelector) {
//            element.querySelectorAll(cssSelector);
            throw new RuntimeException();
        }

        @Override
        public String text() {
            return element.textContent;
        }

        @Override
        public String attr(String key) {
            return element.getAttribute(key);
        }

        @Override
        public void attr(String key, String value) {
            element.setAttribute(key, value);
        }

        @Override
        public Element parent() {
            return new WindowElement(element.parentElement);
        }

    }
}
