package com.tttiger;

import com.tttiger.sql.mapper.BaseMapper;

import java.util.List;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/10 15:51
 */
public class TestMapper extends BaseMapper<Goods> {
    public static void main(String[] args) {
        Goods goods = new Goods();
        goods.setGoodsId("12312");
        goods.setGoodsName("商品名称W");
        goods.setStatus(1);
        goods.setPrice(1.8);
        GoodsType type = new GoodsType();
        type.setTypeId("999");
        type.setTypeName(null);
        goods.setGoodsType(type);
        goods.setIsExist(1);
        TestMapper mapper = new TestMapper();
//        mapper.insert(goods);
//        mapper.selectById("1");
//        mapper.deleteById("1");
        List<Goods> select = mapper.select(null);
        System.out.println(select.size());
//        String str = " a a ";
//        System.out.println(str.trim());
//        mapper.insert(goods);
//       mapper.updateById(goods);
//        QueryWrapper wrapper = new QueryWrapper();
//        wrapper.gt("goods_id", 2);
//        mapper.delete(wrapper);
//        System.out.println(select);
//        QueryWrapper wrapper2 = new QueryWrapper();
//        QueryWrapper wrapper = new QueryWrapper();
//        wrapper.eq("goods_name","a").or().eq("goods_name","b");
//        wrapper.or().groupCondition(wrapper2.eq("goods_type","1").or().eq("goods_name","2"));
//        mapper.select(wrapper);
    }

}
