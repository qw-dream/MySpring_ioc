package com.qw.ioc;

import com.qw.ioc.framework.annotation.Component;

/**
 * 狗粮
 */
@Component
public class DogFood {

    private String type;

    public String getType() {
        return "骨头饼干";
    }

    public void setType(String type) {
        this.type = type;
    }
}