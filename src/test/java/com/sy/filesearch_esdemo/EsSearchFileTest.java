package com.sy.filesearch_esdemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName EsSearchFileTest
 * @Description
 * @Author sunyu
 * @Date 2023/10/12 15:45
 * @Version 1.0
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class EsSearchFileTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void addWordToEs() {
        String path = "/Users/sunyu/Documents/工作/国电/周报/孙宇-周报20230928.doc";
    }

    @Test
    public void addExcelToEx() {
        String path = "/Users/sunyu/Documents/工作/国电/BOS接口文档/Bos3DEngine服务接口列表.xlsx";

    }



}
