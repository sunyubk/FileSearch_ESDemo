package com.sy.filesearch_esdemo.entity.Param;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName EsFileUpdateParam
 * @Description
 * @Author sunyu
 * @Date 2023/10/16 09:55
 * @Version 1.0
 **/
@Data
public class EsFileUpdateParam implements Serializable {

    private String fileName;

    private Integer fileVersion;

}
