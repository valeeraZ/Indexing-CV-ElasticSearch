package com.daar.indexcv.service;

import com.daar.indexcv.entity.CV;
import com.daar.indexcv.entity.CVShort;
import com.daar.indexcv.exceptions.BadFormatException;
import com.daar.indexcv.exceptions.EmptyFileException;
import com.daar.indexcv.exceptions.EmptyKeywordException;
import com.daar.indexcv.exceptions.IdNotFoundException;
import com.daar.indexcv.repository.CVRepository;
import com.daar.indexcv.repository.CVShortRepository;
import com.daar.indexcv.representation.CVShortRepresentation;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSON;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.*;

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
    public List<CVShort> query() {
        Iterator<CVShort> ite = cvShortRepository.findAll().iterator();
        List<CVShort> res = new ArrayList<>();
        while (ite.hasNext()) {
            res.add(ite.next());
        }
        return res;
    }

    //Zhaojie LU
    public List<CVShort> queryInContent(String keyword) {
        List<CVShort> cvs = new ArrayList<>();
        if (!keyword.isEmpty()) {
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("attachment.content", keyword)).build();
            SearchHits<CVShort> search = elasticsearchRestTemplate.search(searchQuery, CVShort.class);
            for (SearchHit<CVShort> searchHit : search) {
                cvs.add(searchHit.getContent());
            }
        } else {
            throw new EmptyKeywordException(ImmutableMap.of("keyword", "empty"));
        }
        return cvs;
    }

    //HOU Zhen
    // Wenzhuo ZHAO: a simple way: use CVRepository returning Optional<CV>
    // TODO: handle the empty id exception
    public CV queryGetById(String id) {
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.termQuery("_id", id));
            SearchRequest searchRequest = new SearchRequest("cv");
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            if (RestStatus.OK.equals(searchResponse.status())) {
                org.elasticsearch.search.SearchHits hits = searchResponse.getHits();
                for (org.elasticsearch.search.SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    //CV res = hit.getSourceAsString()
                    CV res = JSON.parseObject(hit.getSourceAsString(), CV.class);
                    res.setId(id);
                    //log.info("get Cv by id: " + res.toString());
                    //System.out.println("cv: "+res);
                    return res;
                }

            }
        } catch (IOException e) {
            log.error("", e);
        }

        return null;
    }

    //HOU Zhen
    public String updateCV(String id, MultipartFile file, String username) throws IOException {
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
                //.setPipeline("attachment")
                ;
        UpdateRequest updateRequest = new UpdateRequest("cv", id).doc(request);
        //updateRequest.;
        //Map<String, Object> jsonMap = new HashMap<>();
        //jsonMap.put("data", contents);

        UpdateResponse response = highLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        String index = response.getIndex();
        //String id = response.getId();
        log.info("Save document " + name + " of user " + username + " in " + index + " with id " + id);
        return id;
    }

    public void deleteCV(String id){
        if(!cvRepository.existsById(id))
            throw new IdNotFoundException(ImmutableMap.of("id", id));
        cvRepository.deleteById(id);
        log.info("Delete document with id " + id);
    }

    public String deleteAll(){
        return "Dangerous Method not supported";
    }
}
