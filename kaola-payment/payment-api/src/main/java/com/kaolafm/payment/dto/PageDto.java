package com.kaolafm.payment.dto;

import java.util.List;
import java.util.Map;

/**
 * @author gongzf
 * @date 2016/3/30
 */
public class PageDto<T> extends BaseDto {
    private static final long serialVersionUID = 8556873842145921543L;
    private Integer page;// 当前页数
    private Integer pageSize;// 每页数量
    private Integer count;// 总条数
    private Integer totalPages;// 总页数
    private Integer haveNext;// 是否有下一页
    private Integer nextPage;// 下一页页数
    private Integer havePre;// 是否有上一页
    private Integer prePage;// 上一页页数
    private Integer startNum;// 开始条数
    private Integer endNum;// 结束条数
    private List<T> dataList;


    public  PageDto(){

    }

    public PageDto(Integer count, Integer page, Integer pageSize) {
        this.count = count;
        this.pageSize = pageSize;
        this.totalPages = (this.count % this.pageSize) == 0 ? (this.count / this.pageSize)
                : (this.count / this.pageSize + 1);
        this.page = (page > this.totalPages) ? this.totalPages : page;
        this.haveNext = (this.page < this.totalPages) ? 1 : 0;
        this.nextPage = (this.haveNext.intValue() == 1) ? this.page + 1
                : this.page;
        this.havePre = (this.page > 1) ? 1 : 0;
        this.prePage = (this.havePre.intValue() == 1) ? this.page - 1
                : this.page;
        this.startNum = (this.page >= 1 ? this.page - 1 : 0) * this.pageSize;
        this.endNum = (this.page.intValue() == totalPages.intValue()) ? this.count
                : (this.page * this.pageSize);
    }


    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getHaveNext() {
        return haveNext;
    }

    public void setHaveNext(Integer haveNext) {
        this.haveNext = haveNext;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getHavePre() {
        return havePre;
    }

    public void setHavePre(Integer havePre) {
        this.havePre = havePre;
    }

    public Integer getPrePage() {
        return prePage;
    }

    public void setPrePage(Integer prePage) {
        this.prePage = prePage;
    }

    public Integer getStartNum() {
        return startNum;
    }

    public void setStartNum(Integer startNum) {
        this.startNum = startNum;
    }

    public Integer getEndNum() {
        return endNum;
    }

    public void setEndNum(Integer endNum) {
        this.endNum = endNum;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
