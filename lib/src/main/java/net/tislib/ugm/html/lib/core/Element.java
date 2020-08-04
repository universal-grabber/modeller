package net.tislib.ugm.html.lib.core;

public interface Element {

    Element select(String cssSelector);

    Iterable<Element> selectMulti(String cssSelector);

    String text();

    String attr(String key);

    void attr(String key, String value);

    Element parent();

}
