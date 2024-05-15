package com.sztu.check.dto;

/**
 * 分页数据
 * 
 * @author lujiawei
 */
public class PageDTO
{
    /** 当前记录起始索引 */
    private Integer page = 1;

    /** 每页显示记录数 */
    private Integer pageSize = 10;

    public Integer getPageNum()
    {
        return page;
    }

    public void setPage(Integer pageNum)
    {
        this.page = pageNum;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

}
