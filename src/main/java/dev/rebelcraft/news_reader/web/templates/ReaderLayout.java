package dev.rebelcraft.news_reader.web.templates;

import j2html.TagCreator;
import j2html.rendering.IndentedHtml;
import j2html.tags.DomContent;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ReaderLayout {

    private DomContent content = TagCreator.text("Add content here");

    public void write(HttpServletResponse response) throws IOException {
        content.render( IndentedHtml.into( response.getWriter() ) );
    }

    public void addContent(DomContent content) {
        this.content = content;
    }

}
