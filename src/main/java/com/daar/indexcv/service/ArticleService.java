package com.daar.indexcv.service;


import com.daar.indexcv.DTO.ArticleSaveDTO;
import com.daar.indexcv.entity.Article;
import com.daar.indexcv.repository.ArticleRepository;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Wenzhuo Zhao on 11/10/2021.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleService {
    private final ArticleRepository articleRepository;

    public void save(ArticleSaveDTO articleSaveDTO){
        articleRepository.save(articleSaveDTO.toArticle());
    }

    public List<Article> getAllArticles(){
        return Lists.newArrayList(articleRepository.findAll());
    }
}
