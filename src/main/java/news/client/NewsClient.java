package news.client;

import news.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Component
public class NewsClient {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    NewsCacheOperations newsCacheOperations;
    String key = "af11795034b94f25b05987aa8529e9b7";

    public Map<String, News[]> getAllNews(String country){
        //String code =  CountryCode.findByName(country).get(0).name();
        return new HashMap<String, News[]>() {{
            News[] sportsnews = getNewsByType(country,"sports");
            sportsnews = Arrays.copyOf(sportsnews, Math.min(2,sportsnews.length));

            News[] entertainmentsnews = getNewsByType(country,"entertainment");
            entertainmentsnews = Arrays.copyOf(entertainmentsnews, Math.min(2,entertainmentsnews.length));

            News[] healthsnews = getNewsByType(country,"health");
            healthsnews = Arrays.copyOf(healthsnews, Math.min(2,healthsnews.length));

            put("sports", sportsnews);
            put("entertainment", entertainmentsnews);
            put("health", healthsnews);
        }};
    }

    public News[] getNewsByType(String country, String type){

        News[] cachednewlist = newsCacheOperations.checkRedisCache(country,type);
        if(cachednewlist!=null) {
            System.out.println("available in cached");
            return cachednewlist;
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://newsapi.org/v2/top-headlines")
                .queryParam("country", country)
                .queryParam("category", type)
                .queryParam("apiKey", key);

        ResponseEntity<String> restExchange = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,null,String.class);
        String raw_content = restExchange.getBody();
        News[] newslist = processNews(raw_content,country,type);
        if(newslist != null) newsCacheOperations.updateRedisCache(country,type,newslist);
        System.out.println("not available cached data");
        return newslist;

    }

    public News[] processNews(String raw_content, String country, String type){
        ObjectMapper objectMapper = new ObjectMapper();
        //String content = new JSONObject(raw_content).getJSONObject("articles").toString();
        News[] newslist;
        try{
            String content = objectMapper.readTree(raw_content).get("articles").toString();
            newslist = objectMapper.readValue(content, News[].class);
            }
        catch(IOException e){
            return null;
        }

        for(News news : newslist) {
            news.setcountryCode(country);
            news.setType(type);
        }

        return newslist;
    }


}
