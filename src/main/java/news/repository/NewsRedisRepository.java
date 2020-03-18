package news.repository;

import news.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class NewsRedisRepository implements  INewsRedisRepository{
    //private static final String HASH_NAME ="news";

    private RedisTemplate<String, News[]> redisTemplate;
    private HashOperations hashOperations;

    public NewsRedisRepository(){super();}

    @Autowired
    private NewsRedisRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }


    @Override
    public void updateNews(String country, String type, News[] newslist) {
        hashOperations.put(country, type, newslist);
    }

    @Override
    public void deleteNews(String country, String type) {
        hashOperations.delete(country, type);
    }

    @Override
    public News[] findNews(String country, String type) {
        return (News[]) hashOperations.get(country, type);
    }
}
