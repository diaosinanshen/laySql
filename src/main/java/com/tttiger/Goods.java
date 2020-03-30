package com.tttiger;

import com.tttiger.excel.annotation.ExcelAssociate;
import com.tttiger.excel.annotation.ExcelField;
import com.tttiger.excel.annotation.Status;
import com.tttiger.sql.annotation.Table;
import com.tttiger.sql.annotation.TableField;
import com.tttiger.sql.annotation.TableId;
import com.tttiger.sql.annotation.TableLogicalField;

import java.io.IOException;

/**
 * @author 秦浩桐
 */
@Table("goods")
public class Goods {

    public static void main(String[] args) throws IOException, IllegalAccessException {
       Goods goods = new Goods();
        goods.setGoodsId("12312");
        goods.setGoodsName("商品名称");
        goods.setStatus(1);
        goods.setPrice(1.8);
        GoodsType type = new GoodsType();
        type.setTypeId("999");
        type.setTypeName(null);
        goods.setGoodsType(type);
/*
        Goods goods2 = new Goods();
        goods2.setGoodsId("12312");
        goods2.setGoodsName("商品名称");
        goods2.setStatus(1);
        goods2.setPrice(1.8);
        GoodsType type2 = new GoodsType();
        type2.setTypeId("999");
        type2.setTypeName("食品");
        goods2.setGoodsType(type2);

        ExcelUtil<Goods> util = new ExcelUtil<>();
        List<Goods> list = new ArrayList<>();
        list.add(goods);
        list.add(goods2);
        OutputStream out = new FileOutputStream("E:/text.xls");
        util.exportExcel("test", list, out);*/
    }


    @TableId
    private String goodsId;
    @ExcelField(value = "商品名称", sort = -2)
    private String goodsName;

    @Status(value = 1, name = "删除")
    @Status(value = 2, name = "上架")
    @ExcelField(value = "状态", status = true)
    private Integer status;

    @ExcelAssociate(value = {"typeId", "typeName"}, sort = -1)
    @TableField(exist = false)
    private GoodsType goodsType;

    @ExcelField(value = "价格",collect = true)
    private Double price;

    @TableLogicalField
    private Integer isExist;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public GoodsType getGoodsType() {
        return goodsType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getIsExist() {
        return isExist;
    }

    public void setIsExist(Integer isExist) {
        this.isExist = isExist;
    }

    public void setGoodsType(GoodsType goodsType) {
        this.goodsType = goodsType;
    }
}
