package com.qw.ioc;

import com.qw.ioc.framework.annotation.Component;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test2 {

    private final static HashMap<Class, List<Class>> fatherSonHashMap = new HashMap<>();
    /**
     * 1、找到所有的标注有@Component注解类
     * @param args
     */
//    public static void main(String[] args) throws ClassNotFoundException {
//
//        String basePackage = "com.qw.ioc";
////        getResource：方法是得到文件路径的函数。
//        URL url = Test2.class.getResource("/");
//        System.out.println(url.getPath());
//        String resourcePath = url.getPath();
//        String replaceAll = basePackage.replaceAll("\\.", "/");
//        String path = resourcePath + replaceAll;
//
//        //通过此类获取当前路径下有哪些文件
//        File file = new File(path);
//        File[] files = file.listFiles();
//        for (File f : files) {
//            //获取属性名
//            String name = f.getName();
//            //获取java源文件,是文件 否存在返回 判断指定的文件是否存在且为文件
//            if (f.isFile() && name.endsWith(".class")){
//                String[] split = name.split("\\.");
//                //找到哪些类上面有Component注解
//                Class<?> aClass = Class.forName(basePackage + "." + split[0]);
//                Component annotation = aClass.getAnnotation(Component.class);
//                if (annotation != null){
//                    //getInterfaces : 它能够获得这个对象所实现的接口
//                    Class<?>[] interfaces = aClass.getInterfaces();
//                    for (int i = 0; i < interfaces.length; i++) {
//                        Class<?> anInterface = interfaces[i];
//
//                        List<Class> sonList = fatherSonHashMap.get(anInterface);
//                        if (sonList == null){
//                            //第一次找到这个father，就保存到map
//                            ArrayList<Class> sonClassList = new ArrayList<>();
//                            //把标注有@Component注解的存储在集合中
//                            sonClassList.add(aClass);
//                            fatherSonHashMap.put(anInterface,sonClassList);
//                        } else {
//                            //第二次找到，就将father对应的son集合获取到，并且将新的son加入集合中
//                            sonList.add(aClass);
//                        }
//                    }
//                }
//
//                System.out.println(fatherSonHashMap);
//                //在有component注解的类上找他们的父类或者接口
//            }
//
//        }
//
//
//    }

}