package com.yangyh.video.download.util.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: yangyh
 * @create: 2019-03-05 17:47
 **/
public class ExcelListener extends AnalysisEventListener {

    /**
     * 自定义用于暂时存储data。
     * 可以通过实例获取该值
     */
    private final List data = new ArrayList<>();

    @Override
    public void invoke(Object object, AnalysisContext context) {
        //数据存储
        data.add(object);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    public List getData() {
        return data;
    }
}
