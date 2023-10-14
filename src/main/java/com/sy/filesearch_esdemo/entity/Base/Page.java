package com.sy.filesearch_esdemo.entity.Base;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName Page
 * @Description
 * @Author sunyu
 * @Date 2023/10/14 15:42
 * @Version 1.0
 **/
@Data
public class Page<T> {

    /**
     * 查询数据列表
     */
    protected List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    private long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    protected long size = 10;

    /**
     * 当前页
     */
    protected long current = 1;


    public Page(Integer current, Integer size, Integer total, List<T> records) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.records = records;
    }
}
