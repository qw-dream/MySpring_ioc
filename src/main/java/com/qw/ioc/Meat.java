package com.qw.ioc;

import com.qw.ioc.framework.annotation.Component;

/**
 * 肉
 */
@Component
public class Meat {

    private String type;

    public String getType() {
        return "鸡肉";
    }

    public void setType(String type) {
        this.type = type;
    }

}