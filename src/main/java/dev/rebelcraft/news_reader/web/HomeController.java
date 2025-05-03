package dev.rebelcraft.news_reader.web;

import dev.rebelcraft.news_reader.feeds.RssFeeds;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    private final RssFeeds rssFeeds;

    public HomeController(RssFeeds rssFeeds) {
        this.rssFeeds = rssFeeds;
    }

    @GetMapping
    public String home(Model model) {
        model.addAttribute("feeds", rssFeeds.getAllFeedDetails());
        return "homeView";
    }

}
