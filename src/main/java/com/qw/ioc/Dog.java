package com.qw.ioc;

import com.qw.ioc.framework.annotation.Autowired;
import com.qw.ioc.framework.annotation.Component;

/**
 * 小狗
 */
@Component
public class Dog {

    @Autowired(DogFood.class)
    public DogFood dogFood;

    @Autowired(Meat.class)
    private Meat meat;

    /**
     * 吃骨头饼干
     */
//    @Deprecated
//    public void drink(){
//        System.out.println("吃" + dogFood.getType());
//    }

}