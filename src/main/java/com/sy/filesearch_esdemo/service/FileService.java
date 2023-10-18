package com.sy.filesearch_esdemo.service;

import com.sy.filesearch_esdemo.entity.Base.Page;
import com.sy.filesearch_esdemo.entity.Param.EsFileUpdateParam;
import com.sy.filesearch_esdemo.entity.VO.EsFileVO;
import org.springframework.web.multipart.MultipartFile;

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

    void uploadFile(MultipartFile file);

    Page search(String keyword, Integer pageNumber, Integer pageSize);

    Object update(String docId, EsFileUpdateParam param);

    Object del(String docId);
}
