package com.tttiger.sql.wrapper;

import com.tttiger.sql.SqlUtil;
import com.tttiger.sql.SqlWord;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/09 22:29
 */
public class QueryWrapper implements Wrapper {

    private StringBuilder condition = new StringBuilder();

    private int orCount = 0;

    public QueryWrapper eq(String field, Object value) {
        condition.append(createCondition(field, "=", value));
        return this;
    }

    public QueryWrapper ne(String field, Object value) {
        condition.append(createCondition(field, "!=", value));
        return this;
    }

    public QueryWrapper gt(String field, Object value) {
        condition.append(createCondition(field, ">", value));
        return this;
    }

    public QueryWrapper ge(String field, Object value) {
        condition.append(createCondition(field, ">=", value));
        return this;
    }

    public QueryWrapper lt(String field, Object value) {
        condition.append(createCondition(field, "<", value));
        return this;
    }

    public QueryWrapper le(String field, Object value) {
        condition.append(createCondition(field, "<=", value));
        return this;
    }

    public QueryWrapper like(String field, Object value) {
        condition.append(createCondition(field, "like", "%" + value + "%"));
        return this;
    }

    public QueryWrapper or() {
        orCount++;
        return this;
    }

    public QueryWrapper groupCondition(Wrapper wrapper) {
        String linkSymbol = SqlWord.AND;
        if (orCount > 0) {
            linkSymbol = SqlWord.OR;
            orCount--;
        }
        condition.append(linkSymbol).append("(").append(wrapper.getConditionSql()).append(")");
        return this;
    }

    @Override
    public String getConditionSql() {
        condition.delete(0, 4);
        return condition.toString();
    }

    protected String createCondition(String field, String symbol, Object value) {
        String linkSymbol = SqlWord.AND;
        if (orCount > 0) {
            linkSymbol = SqlWord.OR;
            orCount--;
        }
        return linkSymbol + field + " " + symbol + " " + SqlUtil.convertToDBValue(value) + " ";
    }
}
