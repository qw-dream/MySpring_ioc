package com.qw.ioc.framework;


import com.qw.ioc.Dog;
import com.qw.ioc.DogFood;
import com.qw.ioc.Test2;
import com.qw.ioc.framework.annotation.Autowired;
import com.qw.ioc.framework.annotation.Component;
import com.qw.ioc.framework.exception.UnexpectedBeanDefitionalException;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *    自定义的 ioc 框架
 */
public class InjectContext {

    private final static HashMap<Class, List<Class>> fatherSonHashMap = new HashMap<>();

    //beansMap用来放初始化好的对象。 Id ： 对象
    public static final HashMap<String,Object> beansMap = new HashMap<>();

    //所有的class全部找到
    private static final List<Class> beansList = new ArrayList<>();

    private String basePackage;

    private InjectContext() {
    }

    //有参构造初始化init方法
    private InjectContext(String basePackage) {
        this.basePackage = basePackage;

        try {
            init(basePackage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getBean(String id){
        return beansMap.get(id);
    }

    /**
     * 属性注入
     * @param dogClass
     * @return
     * @throws Exception
     */
    private Object inject(Class dogClass) throws Exception {
        try {
            //通过反射给1dog中的DogFood赋值
//        Class<Dog> dogClass = Dog.class;
            //获取所有属性,getDeclaredFields()：获得某个类的所有声明的字段
            Field[] declaredFields = dogClass.getDeclaredFields();
//            T dog = dogClass.newInstance();

            //保证单例
            String id1 = getId(dogClass);
            Object dog = beansMap.get(id1);
            if (dog == null){
                dog = dogClass.newInstance();
                beansMap.put(id1,dog);
            }


            //遍历所有元素，找到dogfood
            for (int i = 0; i < declaredFields.length; i++) {
                Field field = declaredFields[i];
                //允许私有属性通过反射进行赋值
                field.setAccessible(true);
                //获取属性上是否有Autowired注解
                Autowired annotation = field.getAnnotation(Autowired.class);
                if (annotation != null){
                    //getType()：获取变量类型
                    //如果此处是一个接口，则需要通过接口找到其所有的实现类，如果有多个实现类，则通过属性名称来判断使用哪一个实现类
                    Class<?> aClass = field.getType();

                    //isInterface : 判断是否是接口
                    if (aClass.isInterface()){
                        //获取接口的所有实现类
                        List<Class> classList = fatherSonHashMap.get(aClass);

                        //如果此处是一个接口，则需要通过接口找到其所有的实现类，如果有多个实现类，则通过属性名称来判断使用哪一个实现类
                        //获取接口的所有实现类
                        if (classList == null) {
                            throw new NullPointerException();
                        }

                        //一个接口只有一个实现类。直接注入
                        if (classList != null && classList.size() == 1){
                            Class sonClass = classList.get(0);
                            //
                            field.set(dog,sonClass.newInstance());
                        } else {
                            //顶一个标签，用来表示这种情况：有多个实现类，但是一个都没有匹配成功
                            boolean isAutowiredFail = true;

                            for (int j = 0; j < classList.size(); j++) {
                                Class sonClass = classList.get(j);
                                String fieldName = field.getName();
                                //将实现类转换为首位大写的格式
                                //截取
                                String substring = fieldName.substring(1, fieldName.length());
                                //将字符串中的小写字符全部转换成大写
                                String upperCase = fieldName.toUpperCase();
                                char c = upperCase.charAt(0);
                                String className = c + substring;
                                //对比sonClass的名称和属性的名称是否一致，如果一致则找到需要注入的对象。没有找到这抛出异常
                                if (sonClass.getName().endsWith(className)) {
                                    //找到了名称一致的接口的实现类的对象，并且完成注入
                                    field.set(dog,sonClass.newInstance());
                                    isAutowiredFail = false;
                                }
                            }

                            if (isAutowiredFail){
                                throw new UnexpectedBeanDefitionalException("原本期望找到1个对象注入，但是了找到了2个，无法识别应该注入哪一个");
                            }
                        }

                    } else {
                        //当变量的类型不是一个接口
                        //参数1：属性持有者
//                        field.set(dog,aClass.newInstance());

                        //解决单例问题
                        String id = getId(aClass);
                        Object bean = beansMap.get(id);
                        if (bean == null){
                            Object instance = aClass.newInstance();
                            field.set(dog,instance);
                            beansMap.put(id,instance);
                        } else {
                            field.set(dog,bean);
                        }
                    }

                }

//                //getName:  获取当前属性的名称
//                if ("dogFood".equals(field.getName())){
//                    //参数1：属性的持有者
//                    field.set(dog,new DogFood());
//                }

            }

            return dog;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 找到所有的标注有@Component注解中
     * @param basePackage
     * @throws ClassNotFoundException
     */
    private void init(String basePackage) throws Exception {

//        String basePackage = "com.qw.ioc";
//        getResource：方法是得到文件路径的函数。
        //如果该方法参数中以“/”开头表示的是src根目录下开始查找。如果不是以“/”开头的则表示从当前类的包中开始查找
        URL url = Test2.class.getResource("/");
        // getPath得到的是构造参数的路径
        System.out.println(url.getPath());
        String resourcePath = url.getPath();
        //第一个参数regex代表在该字符串中通过正则表达式查找符合添加的子字符串并使用第二个参数replacement替换掉
        String replaceAll = basePackage.replaceAll("\\.", "/");
        String path = resourcePath + replaceAll;

        //通过此类获取当前路径下有哪些文件
        File file = new File(path);
        File[] files = file.listFiles();
        for (File f : files) {
            //获取属性名
            String name = f.getName();
            //获取java源文件,是文件 否存在返回 判断指定的文件是否存在且为文件
            if (f.isFile() && name.endsWith(".class")){
                String[] split = name.split("\\.");
                //找到哪些类上面有Component注解
                Class<?> aClass = Class.forName(basePackage + "." + split[0]);
                Component annotation = aClass.getAnnotation(Component.class);
                if (annotation != null){

                    //为下面的依赖注入做准备
                    beansList.add(aClass);

                    //getInterfaces : 它能够获得这个对象所实现的接口
                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (int i = 0; i < interfaces.length; i++) {
                        Class<?> anInterface = interfaces[i];

                        List<Class> sonList = fatherSonHashMap.get(anInterface);
                        if (sonList == null){
                            //第一次找到这个father，就保存到map
                            ArrayList<Class> sonClassList = new ArrayList<>();
                            //把标注有@Component注解的存储在集合中
                            sonClassList.add(aClass);
                            fatherSonHashMap.put(anInterface,sonClassList);
                        } else {
                            //第二次找到，就将father对应的son集合获取到，并且将新的son加入集合中
                            sonList.add(aClass);
                        }
                    }

                }

                System.out.println(fatherSonHashMap);
                //在有component注解的类上找他们的父类或者接口
            }
        }

        //size=beansList.size(): 在前面，效率更高，只会执行一次
        //此处就已经找到了所有被Component标注的类 （要对找到了类进行依赖注入）
        for (int i = 0,size=beansList.size(); i < size; i++) {
            Class aClass = beansList.get(i);
            Object obj = inject(aClass);
            String id = getId(aClass);
            beansMap.put(id,obj);
        }

    }

    /**
     * 获取对象对应的Id
     * @param aClass
     * @return
     */
    private String getId(Class aClass){
        String tempId = aClass.getName();
        //获取最后一个“.”的下标
        int lastIndexOf = tempId.lastIndexOf(".");
        tempId = tempId.substring(lastIndexOf, tempId.length());
        char charAt = tempId.toLowerCase().charAt(0);
        String substring = tempId.substring(1, tempId.length());
        String id = charAt + substring;
        return id;
    }

}