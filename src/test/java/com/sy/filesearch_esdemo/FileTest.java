package com.sy.filesearch_esdemo;

import com.sy.filesearch_esdemo.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @ClassName fileTest
 * @Description
 * @Author sunyu
 * @Date 2023/10/12 14:14
 * @Version 1.0
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void readWord() {
        String path = "/Users/sunyu/Documents/工作/国电/周报/孙宇-周报20230928.doc";
        String content = FileUtil.readWord(path);
        if (content != null) {
            // 将内容写入文件
            FileUtil.writeContentToFile(content, "outputWordContent.txt");
        }
    }
    @Test
    public void readExcel() {
        String path = "/Users/sunyu/Documents/工作/国电/BOS接口文档/Bos3DEngine服务接口列表.xlsx";
        String content = FileUtil.readExcel(path);
        if (content != null) {
            // 将内容写入文件
            FileUtil.writeContentToFile(content, "outputExcelContent.txt");
        }
    }
}
