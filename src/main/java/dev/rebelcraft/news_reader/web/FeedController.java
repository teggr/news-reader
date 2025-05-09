package dev.rebelcraft.news_reader.web;

import dev.rebelcraft.news_reader.feeds.Feeds;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/feeds")
public class FeedController {

    private final Feeds feeds;

    public FeedController(Feeds feeds) {
        this.feeds = feeds;
    }

    @GetMapping
    public String home(Model model) {
        model.addAttribute("feeds", feeds.getAllFeedDetails());
        return "feedView";
    }

}
