package com.daar.indexcv.service;

import com.daar.indexcv.entity.CV;
import com.daar.indexcv.entity.CVShort;
import com.daar.indexcv.exceptions.BadFormatException;
import com.daar.indexcv.exceptions.EmptyFileException;
import com.daar.indexcv.exceptions.EmptyKeywordException;
import com.daar.indexcv.repository.CVRepository;
import com.daar.indexcv.repository.CVShortRepository;
import com.daar.indexcv.representation.CVShortRepresentation;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Wenzhuo Zhao on 20/10/2021.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CVService {
    private final CVRepository cvRepository;
    private final CVShortRepository cvShortRepository;
    private final RestHighLevelClient highLevelClient;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public String save(String username, MultipartFile file) throws IOException {
        String name;
        if (!file.isEmpty()) {
            name = file.getOriginalFilename();
            Tika tika = new Tika();
            String detectedType = tika.detect(file.getBytes());
            if (!(detectedType.equals("application/pdf") || detectedType.equals("application/x-tika-ooxml"))) {
                throw new BadFormatException(ImmutableMap.of("filename", name, "extension", detectedType));
            }
        } else {
            throw new EmptyFileException(ImmutableMap.of("filename", "empty"));
        }

        String contents = DatatypeConverter.printBase64Binary(file.getBytes());
        IndexRequest request = new IndexRequest("cv")
                .source("data", contents,
                        "username", username)
                .setPipeline("attachment");

        IndexResponse response = highLevelClient.index(request, RequestOptions.DEFAULT);
        String index = response.getIndex();
        String id = response.getId();
        log.info("Save document " + name + " of user " + username + " in " + index + " with id " + id);
        return id;
    }

    //Zhaojie LU
    public List<CVShort> query(){
        Iterator<CVShort> ite = cvShortRepository.findAll().iterator();
        List<CVShort> res = new ArrayList<>();
        while (ite.hasNext()){
            res.add(ite.next());
        }
        return res;
    }

    //Zhaojie LU
    public List<CVShort> queryInContent(String keyword){
        List<CVShort> cvs = new ArrayList<>();
        if(!keyword.isEmpty()) {
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("attachment.content", keyword)).build();
            SearchHits<CVShort> search = elasticsearchRestTemplate.search(searchQuery, CVShort.class);
            for (SearchHit<CVShort> searchHit : search) {
                cvs.add(searchHit.getContent());
            }
        }else{
            throw new EmptyKeywordException(ImmutableMap.of("keyword", "empty"));
        }
        return cvs;
    }
}
