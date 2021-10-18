package com.daar.indexcv.repository;

import com.daar.indexcv.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Wenzhuo Zhao on 11/10/2021.
 */
@Component
public interface ArticleRepository extends ElasticsearchRepository<Article,String> {
    //List<Article> findAll();
}
