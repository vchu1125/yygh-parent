package com.atguigu.yygh.cmn.excel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.io.File;

/**
 * @Author Weizhu
 * @Date 2023/8/3 15:58
 * @注释
 */
public class ExcelReadTest {

    /**
     * 最简单的读
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
     * <p>
     * 3. 直接读即可
     */
    @Test
    public void simpleRead() {

        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 写法3：
        String fileName = "d:/simpleWrite1691049749801.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet().doRead();


    }
}
