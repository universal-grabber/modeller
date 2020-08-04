package net.tislib.ugm.html.lib.tools;

import net.tislib.ugm.html.impl.JsoupDomAccess;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class SelectionUtilTest {

    @Test
    public void test1() throws Exception {
        Document document = Jsoup.parse(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("html/page1.html"),
                "UTF-8",
                "page1");

        JsoupDomAccess jsoupDomAccess = new JsoupDomAccess(document);

        System.out.println(jsoupDomAccess.access().select("title").text());
    }

}
