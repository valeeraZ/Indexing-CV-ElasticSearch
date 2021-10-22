package com.daar.indexcv.service;

import com.daar.indexcv.entity.CV;
import com.daar.indexcv.exceptions.EmptyFileException;
import com.daar.indexcv.repository.CVRepository;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
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
    private final RestHighLevelClient highLevelClient;

    public void save(MultipartFile file) throws IOException {
        String name;
        if (!file.isEmpty()) {
            name = file.getOriginalFilename();
            Tika tika = new Tika();
            String detectedType = tika.detect(file.getBytes());
            if (!(detectedType.equals("application/pdf") || detectedType.equals("application/x-tika-ooxml"))) {
                throw new EmptyFileException(ImmutableMap.of("filename", name, "extension", detectedType));
            }
        } else {
            throw new EmptyFileException(ImmutableMap.of("filename", "empty"));
        }

        String contents = DatatypeConverter.printBase64Binary(file.getBytes());
        IndexRequest request = new IndexRequest("cv")
                .source("data", contents)
                .setPipeline("attachment");

        IndexResponse response = highLevelClient.index(request, RequestOptions.DEFAULT);
        String index = response.getIndex();
        String id = response.getId();
        log.info("Save document " + name + " in " + index + " with id " + id);
    }

    public List<CV> query(){
        Iterator<CV> ite = cvRepository.findAll().iterator();
        List<CV> res = new ArrayList<>();
        while (ite.hasNext()){
            res.add(ite.next());
        }
        return res;
    }

    public List<CV> queryIncontent(){
    }
}
