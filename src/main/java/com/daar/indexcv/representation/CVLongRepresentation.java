package com.daar.indexcv.representation;

import com.daar.indexcv.entity.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Wenzhuo Zhao on 25/10/2021.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CVLongRepresentation {
    String id;
    String username;
    String data;
    Attachment attachment;
}
