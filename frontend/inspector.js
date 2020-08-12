function Inspector(frameId) {
    this.elements = []
    console.log(frameId);
    this.frame = document.getElementById(frameId);

    var me = this;
    this.frame.onload = function () {
        me.frameDoc = me.frame.contentWindow.document;
        me.injectCss();
    }

    window.a = this;
    this.clickHandler = this.clickHandler.bind(me);
    this.hoverHandler = this.hoverHandler.bind(me);
}

Inspector.prototype.injectCss = function () {
    const styleElement = this.frameDoc.createElement('style');

    styleElement.innerHTML = `
        .ug-hover {
            border: 1px solid green;
        }
        .ug-click {
            border: 1px solid blue;
        }
    `;

    this.frameDoc.head.appendChild(styleElement);
}

Inspector.prototype.startInspection = function () {
    this.frameDoc.addEventListener('mouseover', this.hoverHandler);
}

Inspector.prototype.hoverHandler = function (event) {
    if (this.lastHoveredElem) {
        this.unAttach(this.lastHoveredElem);
    }

    this.attach(event.target);
    this.lastHoveredElem = event.target;
}

Inspector.prototype.attach = function (elem) {
    elem.classList.add('ug-hover');

    elem.addEventListener('click', this.clickHandler);
}

Inspector.prototype.unAttach = function (elem) {
    elem.classList.remove('ug-hover');

    elem.removeEventListener('click', this.clickHandler);
}

Inspector.prototype.clickHandler = function (event) {
    const isClicked = event.target.classList.contains('ug-click');

    if (isClicked) {
        event.target.classList.remove('ug-click');
        this.elements.remove(event.target);
    } else {
        event.target.classList.add('ug-click');
        this.elements.push(event.target);
    }

    event.preventDefault();
    event.stopPropagation();

    return false;
}

Inspector.prototype.stopInspection = function () {
    [].forEach.call(this.frameDoc.querySelectorAll('.ug-click'), function (el) {
        el.classList.remove("ug-click");
    });
    [].forEach.call(this.frameDoc.querySelectorAll('.ug-hover'), function (el) {
        el.classList.remove("ug-hover");
    });
    this.elements = [];

    this.frameDoc.removeEventListener('mouseover', this.hoverHandler);
}

Inspector.prototype.getElements = function () {
    var me = this;
    const selectors = this.elements.map(function (item) {
        return me.uniqueSelector(item);
    })
    console.log('getElements', this.elements, selectors);
    return selectors;
}

Inspector.prototype.clearElements = function () {
    console.log('clearElements', arguments);
    this.elements = [];
}

Inspector.prototype.uniqueSelector = function (elSrc) {
    const me = this;
    if (!(elSrc instanceof this.frame.contentWindow["Element"]))
        return;
    var sSel, aAttr = ['name', 'value', 'title', 'placeholder', 'data-*'], // Common attributes
        aSel = [],
        // Derive selector from element
        getSelector = function (el) {
            // 1. Check ID first
            // NOTE: ID must be unique amongst all IDs in an HTML5 document.
            // https://www.w3.org/TR/html5/dom.html#the-id-attribute
            if (el.id) {
                aSel.unshift('#' + el.id);
                return true;
            }
            aSel.unshift(sSel = el.nodeName.toLowerCase());
            // 2. Try to select by classes
            let className = el.className;
            className = className.replace(/pHovered/, '');
            className = className.replace(/pSelected/, '');
            className = className.replace(/pContainer/, '');
            className = className.replace(/pContainerIteration/, '');
            if (className) {
                aSel[0] = sSel += '.' + className.trim().replace(/ +/g, '.');
                if (uniqueQuery())
                    return true;
            }
            // 3. Try to select by classes + attributes
            for (var i = 0; i < aAttr.length; ++i) {
                if (aAttr[i] === 'data-*') {
                    // Build array of data attributes
                    var aDataAttr = [].filter.call(el.attributes, function (attr) {
                        return attr.name.indexOf('data-') === 0;
                    });
                    for (var j = 0; j < aDataAttr.length; ++j) {
                        aSel[0] = sSel += '[' + aDataAttr[j].name + '="' + aDataAttr[j].value + '"]';
                        if (uniqueQuery())
                            return true;
                    }
                } else if (el[aAttr[i]]) {
                    aSel[0] = sSel += '[' + aAttr[i] + '="' + el[aAttr[i]] + '"]';
                    if (uniqueQuery())
                        return true;
                }
            }
            // 4. Try to select by nth-of-type() as a fallback for generic elements
            var elChild = el, sChild, n = 1;
            while (elChild = elChild.previousElementSibling) {
                if (elChild.nodeName === el.nodeName)
                    ++n;
            }
            aSel[0] = sSel += ':nth-of-type(' + n + ')';
            if (uniqueQuery())
                return true;
            // 5. Try to select by nth-child() as a last resort
            elChild = el;
            n = 1;
            while (elChild = elChild.previousElementSibling)
                ++n;
            aSel[0] = sSel = sSel.replace(/:nth-of-type\(\d+\)/, n > 1 ? ':nth-child(' + n + ')' : ':first-child');
            if (uniqueQuery())
                return true;
            return false;
        },
        // Test query to see if it returns one element
        uniqueQuery = function () {
            return me.frame.contentWindow.document.querySelectorAll(aSel.join('>') || null).length === 1;
        };
    // Walk up the DOM tree to compile a unique selector
    while (elSrc.parentNode) {
        if (getSelector(elSrc)) {
            return aSel.join(' > ');
        }
        elSrc = elSrc.parentNode;
    }
    return aSel.join(' > ');
}


window.Inspector = Inspector;
