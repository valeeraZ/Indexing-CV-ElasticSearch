package com.daar.indexcv.repository;


import com.daar.indexcv.entity.CV;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by Wenzhuo Zhao on 20/10/2021.
 */
@Component
public interface CVRepository extends ElasticsearchRepository<CV,String> {
    boolean existsById(String id);
}
