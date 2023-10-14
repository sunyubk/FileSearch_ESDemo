package com.sy.filesearch_esdemo.service.impl;

import com.sy.filesearch_esdemo.entity.Base.Page;
import com.sy.filesearch_esdemo.entity.EsFile;
import com.sy.filesearch_esdemo.entity.VO.EsFileVO;
import com.sy.filesearch_esdemo.service.FileService;
import com.sy.filesearch_esdemo.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
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
        esFile1.setId(1L);
        esFile1.setFileCode("1");
        esFile1.setFileName("孙宇-周报20230928.doc");
        esFile1.setContent(wordContent);
        esFile1.setFilePath("word测试文档/孙宇-周报20230928.doc");
        esFile1.setFileVersion(0);
        esFile1.setCreateTime(new Date());


        String excelPath = "/Users/sunyu/Documents/工作/国电/BOS接口文档/Bos3DEngine服务接口列表.xlsx";
        String excelContent = FileUtil.readExcel(excelPath);
        EsFile esFile2 = new EsFile();
        esFile2.setId(2L);
        esFile2.setFileCode("2");
        esFile2.setFileName("Bos3DEngine服务接口列表.xlsx");
        esFile2.setContent(excelContent);
        esFile2.setFilePath("excel测试文档/Bos3DEngine服务接口列表.xlsx");
        esFile2.setFileVersion(0);
        esFile2.setCreateTime(new Date());

        boolean exists = elasticsearchRestTemplate.indexOps(EsFile.class).exists();
        if (!exists){
            elasticsearchRestTemplate.indexOps(EsFile.class).create();
        }
        elasticsearchRestTemplate.save(esFile1);
        elasticsearchRestTemplate.save(esFile2);
        logger.info("doc文件以添加到ES中");
    }

    @Override
    public Page search(String keyword, Integer pageNumber, Integer pageSize) {
        // 设置分页查询的参数，这里只是简单的配置了条数，还可以配置排序等
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        // 构建根据完整的关键词查询，不对关键词做拆分，查询指定的字段
        List<MatchPhraseQueryBuilder> matches = new ArrayList<>();
        matches.add(QueryBuilders.matchPhraseQuery("fileName", keyword));
        matches.add(QueryBuilders.matchPhraseQuery("content", keyword));
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.should().addAll(matches);

        // 构建查询条件
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                // .withHighlightFields(new HighlightBuilder.Field("content"))
                .build();

        SearchHits<EsFile> search = elasticsearchRestTemplate.search(query, EsFile.class);

        List<EsFileVO> results = new ArrayList<>();
        // 统计关键词在对应文档内容出现的次数，并不是正规的词频统计，因为这里只是对es中存储的文件的内容中的关键词出现次数做统计，并不包含别的字段。
        for (SearchHit<EsFile> hit : search) {
            EsFile esFile = hit.getContent();
            if (esFile != null && esFile.getContent() != null) {

                EsFileVO esFileVO = new EsFileVO();
                BeanUtils.copyProperties(esFile, esFileVO);
                // 获取关键词，并赋值给返回给页面的VO对象
                int keywordCount = esFile.getContent().split(keyword).length - 1;
                esFileVO.setKeywordCount(keywordCount);
                results.add(esFileVO);
            }
        }

        Page page = new Page(pageNumber, pageSize, search.getSearchHits().size(), results);
        return page;
    }

}
