package com.qw.ioc.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)  //表示当前自定义的注解只能作用于属性上面
@Retention(RetentionPolicy.RUNTIME)  //运行时
public @interface Autowired {

    Class value(); //等于注解中的属性

}
