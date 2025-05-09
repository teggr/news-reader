package dev.rebelcraft.news_reader.web.templates;

import dev.rebelcraft.news_reader.feeds.EntryDetails;
import dev.rebelcraft.news_reader.feeds.FeedDetails;
import j2html.TagCreator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.each;

@Component
public class HomeView implements View {

    @Override
    public String getContentType() {
        return MediaType.TEXT_HTML_VALUE;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // get the model
        List<EntryDetails> entryDetails = (List<EntryDetails>) model.get("entries");

        // build the view

        ReaderLayout readerLayout = new ReaderLayout();

        readerLayout.addContent( each(
                entryDetails,
                entry -> each(
                        TagCreator.text(entry.title()),
                        TagCreator.text(entry.getPublishedDate().toInstant().atOffset(ZoneOffset.UTC).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                )
        ) );

        readerLayout.write(response);

    }
}
