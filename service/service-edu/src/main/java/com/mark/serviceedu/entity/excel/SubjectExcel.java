package com.mark.serviceedu.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/23 18:31
 */
@Data
public class SubjectExcel {

    @ExcelProperty(index = 0)
    private String parentTitle;

    @ExcelProperty(index = 1)
    private String childTitle;

}
