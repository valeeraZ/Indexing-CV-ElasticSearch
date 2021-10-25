package com.daar.indexcv.entity;

import com.daar.indexcv.representation.CVLongRepresentation;
import com.daar.indexcv.representation.CVShortRepresentation;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    String username;
    String data;
    Attachment attachment;

    public CVShortRepresentation toCVShortRepresentation(){
        return CVShortRepresentation.builder()
                .id(id)
                .username(username)
                .attachment(attachment)
                .build();
    }

    public CVLongRepresentation toCVLongRepresentation(){
        return CVLongRepresentation.builder()
                .id(id)
                .username(username)
                .data(data)
                .attachment(attachment)
                .build();
    }

}