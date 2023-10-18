package com.sy.filesearch_esdemo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.TermVector;

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
@Document(indexName = "files", shards = 1, replicas = 0)
public class EsFile implements Serializable {


    @Id
    private String id;

    @Field(type = FieldType.Text, index = false)
    private String fileCode;

    /**
     * ik_max_word 细粒度拆分
     * ik_smart 粗粒度
     * 系统中列表所展示文件名
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String fileName;

    /**
     * ik_max_word 细粒度拆分
     * ik_smart 粗粒度
     * 物理文件名
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String realFileName;

    /**
     * 文件内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", fielddata = true)
    private String content;

    /**
     * 文件路径，不是指文件物理路径，指的是文件在系统中的逻辑路径
     */
    @Field(type = FieldType.Text, index = false)
    private String filePath;

    /**
     * 文件版本
     */
    @Field(type = FieldType.Text, index = false)
    private Integer fileVersion;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, index = false)
    private Date createTime;

}
