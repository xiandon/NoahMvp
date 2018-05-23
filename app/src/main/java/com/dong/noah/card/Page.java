package com.dong.noah.card;

import java.io.Serializable;

public class Page implements Serializable {
    private static final long serialVersionUID = 4333019690903946141L;
    /**
     * 当前页。
     */
    private Integer currentPage;

    /**
     * 每页显示记录数。
     */
    private Integer pageSize;


    public Page(Integer currentPage, Integer pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
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
