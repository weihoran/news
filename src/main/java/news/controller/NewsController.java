package news.controller;

import news.client.NewsClient;
import news.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

import java.util.Map;

@RestController
public class NewsController {


    @Autowired
    NewsClient newsclient;


    @RequestMapping(method = RequestMethod.GET,produces={"application/json"})
    public Map<String, News[]> getLicenses(@RequestParam("city") String city,
                                           @RequestParam("country") String country) throws IOException {

            return newsclient.getAllNews(country);
    }
}
