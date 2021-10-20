package com.daar.indexcv.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by Wenzhuo Zhao on 20/10/2021.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "cv")
public class CV {
    @Id
    String id;
    String content;
    Attachment attachment;

    public CV(String content){
        this.content = content;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Attachment{
    public String content_type;
    public String language;
    public String content;
    public int content_length;

}