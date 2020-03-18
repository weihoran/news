package news.client;

import news.model.News;
import news.repository.NewsRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsCacheOperations {
    @Autowired
    NewsRedisRepository newsRedisRepo;

    public News[] checkRedisCache(String country, String type) {
        try {
            return newsRedisRepo.findNews(country, type);
        }
        catch (Exception ex){
            return null;
        }
    }

    public void updateRedisCache(String country, String type, News[] newslist) {
        try {
            newsRedisRepo.updateNews(country, type, newslist);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

}
