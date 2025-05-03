package dev.rebelcraft.news_reader.feeds;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndLink;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class FeedFileSystemRepository {

    private final File localStore;

    public FeedFileSystemRepository() {

        File workingDirectory = new File("").getAbsoluteFile();

        localStore = new File(workingDirectory, ".feeds");

        localStore.mkdirs();

    }

    public String save(Feed feed) {

        try {

            // TODO: save the feed to the file system

            // lookup the key
            String feedKey = FeedFileSystemKey.fromFeedId(feed.getId());

            System.out.println("Save: " + feed.getId() + " using key: " + feedKey);

            File feedDirectory = new File(localStore, feedKey);

            if (!feedDirectory.exists()) {
                feedDirectory.mkdirs();
            }

            File feedProperties = new File(feedDirectory,  "feed.properties");
            if(!feedProperties.exists()) {
                feedProperties.createNewFile();
                Properties props = new Properties();
                props.setProperty("feedId", feed.getId().id());
                props.setProperty("feedUrl", feed.getUrl());
                try(FileWriter fw = new FileWriter(feedProperties)) {
                    props.store(fw, "Feed Properties for " + feed.getUrl());
                }
            }

            File cachedFeed = new File(feedDirectory, "cached-feed.xml");
            if (!cachedFeed.exists()) {

                // Use try-with-resources to ensure the writer is closed.
                try (Writer writer = new FileWriter(cachedFeed)) {
                    SyndFeedOutput output = new SyndFeedOutput();

                    output.output(feed.getSyndFeed(), writer);

                    System.out.println("RSS feed successfully written to: " + cachedFeed.getAbsolutePath());

                } catch (IOException e) {
                    throw new IOException("Error writing RSS feed to file: " + cachedFeed.getAbsolutePath(), e); //Wrap for more context
                } catch (FeedException e) {
                    throw new FeedException("Error serializing RSS feed: " + e.getMessage(), e);
                } finally {
                    //  No need to close the writer here; try-with-resources handles it.
                    // System.clearProperty("com.rometools.rome.xml.prettyPrint"); //cleanup
                }

            }

            return feedKey;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<FeedDetails> findAll() {

        File[] rssDirectories = localStore.listFiles();

        return Stream.of(rssDirectories)
                .map( FeedFileSystemRepository::readFeed)
                .filter(Objects::nonNull)
                .map( FeedFileSystemRepository::mapFeedDetails)
                .sorted(Comparator.comparing(FeedDetails::getPublishedDate).reversed())
                .collect(Collectors.toList());

    }

    private static FeedDetails mapFeedDetails(Feed feed) {
        return new FeedDetails() {

            @Override
            public String getId() {
                return feed.getId().id();
            }

            @Override
            public String getTitle() {
                return feed.getSyndFeed().getTitle();
            }

            @Override
            public Date getPublishedDate() {
                return feed.getSyndFeed().getPublishedDate();
            }

            @Override
            public FeedDetailsLinks getLinks() {
                Optional<SyndLink> feedLink = feed.getSyndFeed().getLinks().stream().filter(l -> l.getRel().equals("self")).findFirst();
                Optional<SyndLink> siteLink = feed.getSyndFeed().getLinks().stream().filter(l -> l.getRel().equals("alternative")).findFirst();
                return new FeedDetailsLinks() {

                    @Override
                    public String getSiteHref() {
                        return siteLink.map(SyndLink::getHref).orElse(feed.getSyndFeed().getLink());
                    }

                    @Override
                    public String getFeedHref() {
                        return feedLink.map(SyndLink::getHref).orElse(feed.getUrl());
                    }
                };
            }

        };
    }

    private static Feed readFeed(File rssDirectory) {

        try {

            File cachedFile = new File(rssDirectory, "cached-feed.xml");

            if (!cachedFile.exists()) {
                return null;
            }

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(cachedFile);

            File propertiesFile = new File(rssDirectory, "feed.properties");

            Properties properties = new Properties();
            try(FileReader fr = new FileReader(propertiesFile)) {
            properties.load(fr);
            }

            FeedId feedId = new FeedId( properties.getProperty("feedId") );
            String feedUrl = properties.getProperty("feedUrl");

            return new Feed( feedId, feedUrl, feed );

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FeedException e) {
            throw new RuntimeException(e);
        }

    }

}
