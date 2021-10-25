package com.daar.indexcv.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by Wenzhuo Zhao on 25/10/2021.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "cv")
public class CVShort {
    @Id
    String id;
    String username;
    Attachment attachment;

}
