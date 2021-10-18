package com.daar.indexcv.controller;


import com.daar.indexcv.DTO.ArticleSaveDTO;
import com.daar.indexcv.entity.Article;
import com.daar.indexcv.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Created by Wenzhuo Zhao on 11/10/2021.
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api")
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/articles")
    public ResponseEntity<Void> addArticle(@RequestBody @Valid ArticleSaveDTO articleSaveDTO){
        articleService.save(articleSaveDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/articles")
    public ResponseEntity<List<Article>> getArticles(){
        List<Article> articles = articleService.getAllArticles();
        return ResponseEntity.ok().body(articles);
    }
}
