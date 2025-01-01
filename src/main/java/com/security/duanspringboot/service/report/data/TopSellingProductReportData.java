package com.security.duanspringboot.service.report.data;

import com.security.duanspringboot.constant.Constant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopSellingProductReportData {
    String productName = StringUtils.EMPTY;
    String productCategory = StringUtils.EMPTY;
    String sellingQuantity = StringUtils.EMPTY;
    String inStock = StringUtils.EMPTY;
    public String toRow() {
        return productName + Constant.COMMA +
                productCategory + Constant.COMMA +
                sellingQuantity + Constant.COMMA +
                inStock +
                Constant.NEW_LINE;
    }
}
