package com.tttiger;

import com.tttiger.excel.annotation.ExcelField;

/**
 * @author 秦浩桐
 */
public class GoodsType {

    @ExcelField("类型id")
    private String typeId;

    @ExcelField("类型名称")
    private String typeName;


    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
