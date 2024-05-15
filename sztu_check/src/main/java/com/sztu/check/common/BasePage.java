package com.sztu.check.common;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Page
 * @author elysiaEgo
 */
@Data
@AllArgsConstructor
public class BasePage<T> {
    private Long total;
    private Long pageSize;
    private Long pageNum;
    private boolean hasNext;
    private boolean hasPrevious;
    private List<T> list;

    public BasePage(Page<T> queryPage) {
        this.total = queryPage.getTotal();
        this.pageSize = queryPage.getSize();
        this.pageNum = queryPage.getCurrent();
        this.hasNext = queryPage.hasNext();
        this.hasPrevious = queryPage.hasPrevious();
        this.list = queryPage.getRecords();
    }
}
