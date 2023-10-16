package com.sy.filesearch_esdemo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.sy.filesearch_esdemo.entity.Base.Page;
import com.sy.filesearch_esdemo.entity.EsFile;
import com.sy.filesearch_esdemo.entity.Param.EsFileUpdateParam;
import com.sy.filesearch_esdemo.entity.VO.EsFileVO;
import com.sy.filesearch_esdemo.service.FileService;
import com.sy.filesearch_esdemo.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @ClassName FileServiceImpl
 * @Description
 * @Author sunyu
 * @Date 2023/10/12 10:31
 * @Version 1.0
 **/
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void uploadFile() {
        String wordPath = "/Users/sunyu/Documents/工作/国电/周报/孙宇-周报20230928.doc";
        String wordContent = FileUtil.readWord(wordPath);
        EsFile esFile1 = new EsFile();
        esFile1.setId("1");
        esFile1.setFileCode("1");
        esFile1.setFileName("孙宇-周报0928.doc");
        esFile1.setRealFileName("孙宇-周报20230928.doc");
        esFile1.setContent(wordContent);
        esFile1.setFilePath("word测试文档/孙宇-周报20230928.doc");
        esFile1.setFileVersion(0);
        esFile1.setCreateTime(new Date());

        String excelPath = "/Users/sunyu/Documents/工作/国电/BOS接口文档/Bos3DEngine服务接口列表.xlsx";
        String excelContent = FileUtil.readExcel(excelPath);
        EsFile esFile2 = new EsFile();
        esFile2.setId("2");
        esFile2.setFileCode("2");
        esFile2.setFileName("服务接口列表.xlsx");
        esFile2.setRealFileName("Bos3DEngine服务接口列表.xlsx");
        esFile2.setContent(excelContent);
        esFile2.setFilePath("excel测试文档/Bos3DEngine服务接口列表.xlsx");
        esFile2.setFileVersion(0);
        esFile2.setCreateTime(new Date());


        String pdfPath = "/Users/sunyu/Documents/工作/国电/测试文件/孙宇-周报20230915.pdf";
        String pdfContent = FileUtil.readPDF(pdfPath);
        EsFile esFile3 = new EsFile();
        esFile3.setId("3asddf");
        esFile3.setFileCode("3");
        esFile3.setFileName("孙宇-周报0915.pdf");
        esFile3.setRealFileName("孙宇-周报20230915.pdf");
        esFile3.setContent(pdfContent);
        esFile3.setFilePath("word测试文档/Bos3DEngine服务接口列表.xlsx");
        esFile3.setFileVersion(0);
        esFile3.setCreateTime(new Date());

        String testwordPath = "/Users/sunyu/Documents/工作/国电/测试文件/word测试文件.docx";
        String testwordContent = FileUtil.readWord(testwordPath);
        EsFile testesFile1 = new EsFile();
        testesFile1.setId("4");
        testesFile1.setFileCode("4");
        testesFile1.setFileName("word测试文件.docx");
        testesFile1.setRealFileName("word测试文件.docx");
        testesFile1.setContent(testwordContent);
        testesFile1.setFilePath("word测试文档/word测试文件.docx");
        testesFile1.setFileVersion(0);
        testesFile1.setCreateTime(new Date());

        boolean exists = elasticsearchRestTemplate.indexOps(EsFile.class).exists();
        if (!exists){
            elasticsearchRestTemplate.indexOps(EsFile.class).create();
        }
        elasticsearchRestTemplate.save(esFile1);
        elasticsearchRestTemplate.save(esFile2);
        elasticsearchRestTemplate.save(esFile3);
        elasticsearchRestTemplate.save(testesFile1);
        logger.info("doc文件以添加到ES中");
    }

    @Override
    public Page search(String keyword, Integer pageNumber, Integer pageSize) {
        long l = System.currentTimeMillis();
        // 设置分页查询的参数，这里只是简单的配置了条数，还可以配置排序等
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        // 构建查询条件对象
        NativeSearchQuery query;

        // 如果要搜索的关键词不为空，则按照关键词检索，否则检索出所有文档
        if (keyword != null && !keyword.equals("")) {
            // 构建根据完整的关键词查询，不对关键词做拆分，查询指定的字段
            List<MatchPhraseQueryBuilder> matches = new ArrayList<>();
            matches.add(QueryBuilders.matchPhraseQuery("fileName", keyword));
            matches.add(QueryBuilders.matchPhraseQuery("realFileName", keyword));
            matches.add(QueryBuilders.matchPhraseQuery("content", keyword));
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.should().addAll(matches);
            query = new NativeSearchQueryBuilder()
                    .withQuery(boolQueryBuilder)
                    .withPageable(pageRequest)
                    // .withHighlightFields(new HighlightBuilder.Field("content"))
                    .build();
        } else {
            query = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.matchAllQuery())
                    .withPageable(pageRequest)
                    // .withHighlightFields(new HighlightBuilder.Field("content"))
                    .build();
        }

        // 根据条件进行查询
        SearchHits<EsFile> search = elasticsearchRestTemplate.search(query, EsFile.class);


        // 对查询出来的结果进行处理
        List<EsFileVO> results = new ArrayList<>();
        // 统计关键词在对应文档内容出现的次数，并不是正规的词频统计，因为这里只是对es中存储的文件的内容中的关键词出现次数做统计，并不包含别的字段。
        for (SearchHit<EsFile> hit : search) {
            EsFile esFile = hit.getContent();
            if (esFile != null && esFile.getContent() != null) {

                EsFileVO esFileVO = new EsFileVO();
                BeanUtils.copyProperties(esFile, esFileVO);
                if (keyword != null && !keyword.equals("")) {
                    // 获取关键词，并赋值给返回给页面的VO对象
                    int keywordCount = esFile.getContent().split(keyword).length - 1;
                    esFileVO.setKeywordCount(keywordCount);
                }
                results.add(esFileVO);
            }
        }

        Page page = new Page(pageNumber, pageSize, search.getSearchHits().size(), results);
        logger.info("执行时间：{}",System.currentTimeMillis() - l);
        return page;
    }

    @Override
    public Object update(String docId, EsFileUpdateParam param) {
        // QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("file_code", "2");
        UpdateQuery query = UpdateQuery.builder(docId)
                .withDocument(Document.from(BeanUtil.beanToMap(param)))
                .build();
        UpdateResponse files = elasticsearchRestTemplate.update(query, IndexCoordinates.of("files"));
        return files;
    }

    @Override
    public Object del(String docId) {
        String delete = elasticsearchRestTemplate.delete(docId, EsFile.class);
        return delete;
    }

}
