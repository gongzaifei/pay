package com.kaolafm.payment.dto;

import java.util.ArrayList;
import java.util.List;

public class PageDto<T> extends BaseDto {
    
    private static final long serialVersionUID = 8849128739604682985L;
    private Integer haveNext = 0;// 是否有下一页
    private Integer nextPage = 0;// 下一页页数
    private Integer havePre = 0;// 是否有上一页
    private Integer prePage = 0;// 上一页页数
    private Integer currentPage;//当前页数
    private Integer count = 0; // 记录数
    private Integer sumPage = 0;// 总页数
	private Integer pageSize;//
    private List<T> dataList = new ArrayList<T>();// 数据
    
    public PageDto() {
        
    }
    
    /**
     * 是否设置默认值
     * 
     * @param isDefault
     */
    public PageDto(boolean isDefault) {
        this.haveNext = 0;
        this.nextPage = 0;
        this.haveNext = 0;
        this.havePre = 0;
        this.prePage = 0;
        this.count = 0;
        this.sumPage = 0;
    }
    
    public PageDto(Integer haveNext, Integer nextPage, Integer havePre, Integer prePage) {
        super();
        this.haveNext = haveNext;
        this.nextPage = nextPage;
        this.havePre = havePre;
        this.prePage = prePage;
    }
    
    public PageDto(Integer totalCounts,Integer currentPage,Integer pagesize){
		this.sumPage = (totalCounts%pagesize==0)?(totalCounts/pagesize):(totalCounts/pagesize)+1;
		this.haveNext = (currentPage<this.sumPage)?1:0;
		this.nextPage = (this.haveNext==1)?currentPage+1:currentPage;
		this.havePre = (currentPage>0 && currentPage != 1)?1:0;
		this.prePage = (this.havePre==1)?currentPage-1:currentPage;
		this.currentPage=currentPage;
		this.count = totalCounts;
		this.pageSize = pagesize;
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
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
    	this.count = count;
//		this.sumPage = (count%this.pageSize==0)?(count/this.pageSize):(count/this.pageSize)+1;
//		this.haveNext = (currentPage<this.sumPage)?1:0;
//		this.nextPage = (this.haveNext==1)?currentPage+1:currentPage;
//		this.havePre = (currentPage>0)?1:0;
//		this.prePage = (this.havePre==1)?currentPage-1:currentPage;
    }
    
    public Integer getPrePage() {
        return prePage;
    }
    
    public void setPrePage(Integer prePage) {
        this.prePage = prePage;
    }
    
    public List<T> getDataList() {
        return dataList;
    }
    
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
    
    public void setSumPage(Integer sumPage) {
        this.sumPage = sumPage;
    }
    
    public Integer getSumPage() {
        return sumPage;
    }

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
    
    
}
