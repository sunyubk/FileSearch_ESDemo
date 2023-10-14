package com.sy.filesearch_esdemo.controller;

import com.alibaba.fastjson2.JSONObject;
import com.sy.filesearch_esdemo.entity.Base.Page;
import com.sy.filesearch_esdemo.entity.EsFile;
import com.sy.filesearch_esdemo.entity.VO.EsFileVO;
import com.sy.filesearch_esdemo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 文件接口
 * @ClassName FileController
 * @Description
 * @Author sunyu
 * @Date 2023/10/12 10:32
 * @Version 1.0
 **/
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService service;

    /**
     * 模拟上传文件的接口，实际上是读取本地文件
     * @return
     */
    @GetMapping("/uploadFile")
    public String uploadFile() {
        service.uploadFile();
        return "ok";

    }


    /**
     * 查询ES中的数据
     * @return
     */
    @GetMapping("/search")
    public JSONObject search(@RequestParam("keyWord") String keyword,
                             @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Page page = service.search(keyword, pageNumber, pageSize);
        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", page);
        return result;

    }

}
