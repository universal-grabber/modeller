package net.tislib.ugm.lib.markers.base;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.nio.charset.Charset;

public class DocumentOptimizer extends Document {
    private final Document delegate;
    
    public DocumentOptimizer(Document delegate) {
        super(delegate.baseUri());
        this.delegate = delegate;
    }

    @Override
    public String location() {
        return delegate.location();
    }

    @Override
    public DocumentType documentType() {
        return delegate.documentType();
    }

    @Override
    public Element head() {
        return delegate.head();
    }

    @Override
    public Element body() {
        return delegate.body();
    }

    @Override
    public String title() {
        return delegate.title();
    }

    @Override
    public void title(String title) {
        delegate.title(title);
    }

    @Override
    public Element createElement(String tagName) {
        return delegate.createElement(tagName);
    }

    @Override
    public Document normalise() {
        return delegate.normalise();
    }

    @Override
    public String outerHtml() {
        return delegate.outerHtml();
    }

    @Override
    public Element text(String text) {
        return delegate.text(text);
    }

    @Override
    public String nodeName() {
        return delegate.nodeName();
    }

    @Override
    public void charset(Charset charset) {
        delegate.charset(charset);
    }

    @Override
    public Charset charset() {
        return delegate.charset();
    }

    @Override
    public void updateMetaCharsetElement(boolean update) {
        delegate.updateMetaCharsetElement(update);
    }

    @Override
    public boolean updateMetaCharsetElement() {
        return delegate.updateMetaCharsetElement();
    }

    @Override
    public Document clone() {
        return delegate.clone();
    }

    @Override
    public OutputSettings outputSettings() {
        return delegate.outputSettings();
    }

    @Override
    public Document outputSettings(OutputSettings outputSettings) {
        return delegate.outputSettings(outputSettings);
    }

    @Override
    public QuirksMode quirksMode() {
        return delegate.quirksMode();
    }

    @Override
    public Document quirksMode(QuirksMode quirksMode) {
        return delegate.quirksMode(quirksMode);
    }

    @Override
    public Parser parser() {
        return delegate.parser();
    }

    @Override
    public Document parser(Parser parser) {
        return delegate.parser(parser);
    }
}
