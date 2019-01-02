package com.xhe.refreshrecycler;

import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;

/**
 * @author hexiang
 * @time 2019/1/2
 */

public class StyleConfig {
    private RefreshFooter refreshFooter;
    private RefreshHeader refreshHeader;

    public RefreshFooter getRefreshFooter() {
        return refreshFooter;
    }

    public void setRefreshFooter(RefreshFooter refreshFooter) {
        this.refreshFooter = refreshFooter;
    }

    public RefreshHeader getRefreshHeader() {
        return refreshHeader;
    }

    public void setRefreshHeader(RefreshHeader refreshHeader) {
        this.refreshHeader = refreshHeader;
    }
}
