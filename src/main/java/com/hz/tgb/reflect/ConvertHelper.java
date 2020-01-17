package com.hz.tgb.reflect;

import com.hz.tgb.entity.Student;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.lang.reflect.InvocationTargetException;

/** 类转换助手
 * @Author hezhao
 * @Time 2018-05-04 1:44
 * @Description 无
 * @Version V 1.0
 */
public class ConvertHelper {
    private ConvertHelper() {}

    public static <T> T convert(Object value,Class<T>targetType){
        return (T)ConvertUtils.convert(value, targetType);
    }

    public static <T> T mapConvertToBean(Map<String, Object> map, Class<T> beanClass) {
        Object bean = null;
        try {
            bean = beanClass.newInstance();
            for (String name : map.keySet()) {
                BeanUtils.setProperty(bean, name, map.get(name));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (T)bean;
    }

    /**
     * Bean转map
     *
     * @param bean
     * @return
     */
    public static LinkedHashMap<String,Object> beanConvertToMap(Object bean) {
        return beanConvertToMap(bean, false, FMT.NONE);
    }

    /**
     * Bean转map
     *
     * @param bean
     * @param setNull	如果值为空时，设置值为""
     * @param fmt		格式：FMT.NONE(-1),   TOUPPER(0),   TOLOWER(1)
     * @return
     */
    private static LinkedHashMap<String,Object> beanConvertToMap(Object bean, boolean setNull,FMT fmt) {

        LinkedHashMap<String,Object> map = new LinkedHashMap<String, Object>();
        Class<?> clazz = bean.getClass();//获取当前对象的类
        try {
            Field[] fields = getFields(clazz);//获取该类所有的字段信息

            List<String> ignoreField = Arrays.asList("serialVersionUID","logger");

            if(bean != null){
                for (Field field : fields) {
                    if(ignoreField.contains(field.getName())){//忽略serialVersionUID 和logger
                        continue;
                    }
                    field.setAccessible(true);
                    try{
                        Object value =field.get(bean);
                        if(setNull && (value == null || value.equals(""))){
                            if(fmt == FMT.TOLOWER){//小写
                                map.put(field.getName().toLowerCase(), "");
                            }else if(fmt == FMT.TOUPPER){//大写
                                map.put(field.getName().toUpperCase(), "");
                            }else{
                                map.put(field.getName(), "");
                            }
                        }else  if(value != null && !value.equals("")){
                            if(fmt == FMT.TOLOWER){//小写
                                map.put(field.getName().toLowerCase(), field.get(bean));
                            }else if(fmt == FMT.TOUPPER){//大写
                                map.put(field.getName().toUpperCase(), field.get(bean));
                            }else{
                                map.put(field.getName(), field.get(bean));
                            }
                        }
                    }catch(Exception e){
                        System.err.println("Bean转map时出错，字段名："+field.getName()+"，类"+clazz.getSimpleName()+"："+e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 返回包含字段的map信息
     * @param clazz
     * @return
     */
    public static Map<String,Field> getFieldsForMap(Class<?> clazz){
        Field[]  fields = getFields(clazz);
        Map<String,Field> map = new HashMap<String, Field>();
        for (Field field : fields) {
            map.put(field.getName(), field);
        }
        return map;
    }

    /**
     * 获取所有字段（包括父类）
     *
     * @param clazz
     * @return
     */
    public static Field[] getFields(Class<?> clazz){
        // 获取父类
        if ( clazz == Object.class ) {
            return null;
        }
        return combineFields(getFields(clazz.getSuperclass()),clazz.getDeclaredFields());
    }
    /**
     * 获取所有字段（包括父类）
     *
     * @param clazz
     * @return
     */
    public static List<Field[]> getFieldsList(Class<?> clazz){
        // 获取父类
        if ( clazz == Object.class ) {
            return null;
        }

        Field[] f = clazz.getDeclaredFields();
        List<Field[]> flist = new ArrayList<Field[]>();
        flist.add(f);

        Class<?> superClazz = clazz.getSuperclass();
        while(superClazz != null){
            f = superClazz.getDeclaredFields();
            flist.add(f);
            superClazz = superClazz.getSuperclass();
        }

        return flist;
    }

    /**
     * 合并2个Filed数组
     *
     * @param f1
     * @param f2
     * @return
     */
    public static Field[] combineFields(Field[] f1,Field[] f2){

        if(f1==null && f2==null){
            return null;
        }else	if(f2==null ){
            return f1;
        }else	if(f1==null ){
            return f2;
        }

        int length = f1.length+f2.length;
        Field[] fields = new Field[length];
        int i=0;
        for(Field field:f1){
            fields[i++]=field;
        }
        for(Field field:f2){
            fields[i++]=field;
        }
        return fields;
    }

    /**
     * 判断某个字段是否在某个类中
     *
     * @param field
     * @param clazz
     * @return
     */
    public static boolean existField(Field field,Class<?> clazz){
        boolean exist=false;
        try {
            Field f = clazz.getDeclaredField(field.getName());
            field =f;
            exist=true;
        } catch (Exception e) {
            System.err.println("判断某个字段是否在"+clazz+"类中时出错："+e.getMessage());
        }

        return exist;
    }

    /**
     * 去掉默认值
     *
     * @param bean
     * @param clazz
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> void removeDefault(T bean, Class<T> clazz){

        List<String> ignoreField = Arrays.asList("serialVersionUID","logger");
        try {
            T o = clazz.newInstance();// 产生实例，用于去掉默认值信息

            Field[] fields = getFields(clazz);// 获取该类所有的字段信息
            for (Field field : fields) {
                if(ignoreField.contains(field.getName())){//忽略serialVersionUID 和logger
                    continue;
                }
                field.setAccessible(true);
                try {
                    Object v = field.get(o);
                    if (v != null) {// 如果有默认值，则置为null
                        field.set(bean, null);
                    }
                } catch (Exception e) {
                    System.err.println("去掉默认值时出错 :"+ e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("去掉默认值时出错："+ e.getMessage());
        }
    }


    /**
     * 格式化枚举类
     *
     * @author arron
     * @date 2016年2月15日 下午4:00:12
     * @version 1.0
     */
    public static enum FMT{
        NONE(-1),
        TOUPPER(0),
        TOLOWER(1),
        ;
        private int code;
        private FMT(int code){
            this.code = code;
        }
        public int getCode() {
            return code;
        }
    }

    public static void main(String[] args) {
        Student s = new Student();
        s.setName("张三");
        s.setBirthday(new Date());
        s.setAge(12);
        s.setId(1);
        s.setSex(true);

        LinkedHashMap<String, Object> map = ConvertHelper.beanConvertToMap(s);
        System.out.println(map);

        Student student = ConvertHelper.mapConvertToBean(map, Student.class);
        System.out.println(student.toString());
    }
}
