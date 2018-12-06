package com.github.codegenerator.common.in.model;

import freemarker.cache.TemplateLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class StringTemplateLoader implements TemplateLoader{
    private String template;

    public StringTemplateLoader(String template){
        this.template = template;
    }

    @Override
    public Object findTemplateSource(String name) throws IOException {
        return new StringReader(template);
    }

    @Override
    public long getLastModified(Object templateSource) {
        return 0;
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        return (Reader)templateSource;
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {
        ((StringReader)templateSource).close();
    }
}
