package news.repository;

import news.model.News;

public interface INewsRedisRepository {
    void updateNews(String country, String type, News[] newslist);
    void deleteNews(String country, String type);
    News[] findNews(String country, String type);

}
