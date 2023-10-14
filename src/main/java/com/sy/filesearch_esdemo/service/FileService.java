package com.sy.filesearch_esdemo.service;

import com.sy.filesearch_esdemo.entity.Base.Page;
import com.sy.filesearch_esdemo.entity.VO.EsFileVO;

import java.util.List;

/**
 * @ClassName FileService
 * @Description
 * @Author sunyu
 * @Date 2023/10/12 10:31
 * @Version 1.0
 **/
public interface FileService {


    void uploadFile();

    Page search(String keyword, Integer pageNumber, Integer pageSize);
}
