package net.tislib.ugm.html.lib.tools;

import net.tislib.ugm.html.lib.core.Element;

public class SelectionUtil {

    public String getUniqueSelector(Element element) {
        return element.attr("class");
    }

}
