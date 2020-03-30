package com.tttiger.sql;

import java.util.UUID;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/12 22:58
 */
public class UuidGenerator implements IdGenerator<String> {
    @Override
    public String getId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
