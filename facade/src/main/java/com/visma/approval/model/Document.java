package com.visma.approval.model;

import com.google.gson.Gson;
import com.visma.approval.controller.exceptions.ParserException;
import com.visma.approval.model.dto.Processing;
import com.visma.approval.view.ClassHandler;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by robert on 06.02.2017.
 */
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    public Date sentTime;
    public Processing processing;
    @Lob
    public HashMap<String, String> fields = new HashMap<>();

    private static Gson gson = new Gson();

    public Map<String, String> getFields() {return fields;}

    public String get(String fieldName) {
        return fields.get(fieldName);
    }

    public <T> T get(String fieldName, Class T) {
        String json = fields.get(fieldName);
        Object result;
        try {
            result = ClassHandler.getObjectFromJson(json, T);
        }
        catch (ParserException e){
            result = null;
        }
        return (T) result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document document = (Document) o;

        if (id != null ? !id.equals(document.id) : document.id != null) return false;
        if (sentTime != null ? !sentTime.equals(document.sentTime) : document.sentTime != null) return false;
        if (processing != null ? !processing.equals(document.processing) : document.processing != null) return false;
        return fields != null ? fields.equals(document.fields) : document.fields == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sentTime != null ? sentTime.hashCode() : 0);
        result = 31 * result + (processing != null ? processing.hashCode() : 0);
        result = 31 * result + (fields != null ? fields.hashCode() : 0);
        return result;
    }
}
