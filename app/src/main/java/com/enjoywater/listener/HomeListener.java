package com.enjoywater.listener;

import com.enjoywater.model.News;
import com.enjoywater.model.Notify;

public interface HomeListener {
    void goHomeNewsDetail(News news);
    void goSaleNewsDetail(News news);
}
