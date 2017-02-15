package com.visma.approval.view;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.visma.approval.controller.exceptions.ParserException;

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
    private final static List<String> primitives = Arrays.asList("Byte","Short","Integer","Long","Float","Double","Boolean","String","Date");
    private static Gson gson = new Gson();

    public static Class<?> getClassForName(String name) throws ClassNotFoundException {
        if (primitives.contains(name)) {
            if (name.equals("Date")){
                return Class.forName("java.util.Date");
            }
            else {
                return Class.forName("java.lang." + name);
            }
        } else {
            return Class.forName("com.visma.approval.model.dto." + name);
        }
    }

    public static <T> T getObjectFromJson(String json,String strCls) throws ParserException,ClassNotFoundException {
        Class<?> cls = getClassForName(strCls);
        return getObjectFromJson(json,cls);
    }

    public static <T> T getObjectFromJson(String json,Class<?> T) throws ParserException {
        if (T.equals(String.class)){
            return (T)json;
        }
        if (T.equals(Date.class)){
            return (T)extractDate(json);
        }

        Object result;
        try {
            result = gson.fromJson(json, T);
        }
        catch (JsonSyntaxException e){
           throw new ParserException("Wrong type. " + json + " expected type is " + T.getName());
        }
        return (T) result;

    }

    private static Date extractDate(String strSentTime) throws ParserException {
        Date date;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        try {
            date = format.parse(strSentTime);
        } catch (ParseException parseException) {
            throw new ParserException("Failed to parse " + strSentTime);
        }
        return date;
    }
}
