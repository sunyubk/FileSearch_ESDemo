package com.sy.filesearch_esdemo.entity.VO;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName EsFile
 * @Description
 * @Author sunyu
 * @Date 2023/10/12 10:02
 * @Version 1.0
 **/
@Data
public class EsFileVO implements Serializable {

    // private String id;

    private String fileCode;

    private String fileName;

    private String realFileName;

    private String filePath;

    private Integer fileVersion;

    private Date createTime;

    private Integer keywordCount;
}
