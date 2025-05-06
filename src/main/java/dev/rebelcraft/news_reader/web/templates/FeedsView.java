package dev.rebelcraft.news_reader.web.templates;

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
public class FeedsView implements View {

    @Override
    public String getContentType() {
        return MediaType.TEXT_HTML_VALUE;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // get the model
        List<FeedDetails> feeds = (List<FeedDetails>) model.get("feeds");

        // build the view

        ReaderLayout readerLayout = new ReaderLayout();

        readerLayout.addContent( each(
                feeds,
                feed -> each(
                        TagCreator.text(feed.getTitle()),
                        TagCreator.text(feed.getPublishedDate().toInstant().atOffset(ZoneOffset.UTC).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE)),
                        TagCreator.text(feed.getLinks().getFeedHref()),
                        TagCreator.text(feed.getLinks().getSiteHref())
                )
        ) );

        readerLayout.write(response);

    }
}
