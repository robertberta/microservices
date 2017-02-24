package com.visma.approval.facade;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by robert on 10.02.2017.
 */
public class ClassHandler {
    private final static List<String> primitives = Arrays.asList("Byte","Short","Integer","Long","Float","Double","Boolean","String");
    private static Gson gson = new Gson();

    public static Class<?> getClassForName(String name) throws ClassNotFoundException {
        if (primitives.contains(name)) {
            return Class.forName("java.lang." + name);
        }
        if (name.equals("Date")){
            return Class.forName("java.util.Date");
        }
        if (name.equals("ProcessingProvider")){
            return Class.forName("com.visma.approval.model.ProcessingProvider");
        }
        return Class.forName("com.visma.approval.facade.dto." + name);
    }

    public static <T> T getObjectFromJson(String json,String strCls) throws ParseException,ClassNotFoundException {
        Class<?> cls = getClassForName(strCls);
        return getObjectFromJson(json,cls);
    }

    public static <T> T getObjectFromJson(String json,Class<?> T) throws ParseException,JsonSyntaxException {
        if (T.equals(String.class)){
            return (T)json;
        }
        if (T.equals(Date.class)){
            return (T)extractDate(json);
        }

        Object result;
        result = gson.fromJson(json, T);
        return (T) result;

    }

    private static Date extractDate(String strSentTime) throws ParseException {
        Date date;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        date = format.parse(strSentTime);
        return date;
    }
}
