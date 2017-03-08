package com.visma.approval.facade;

import com.google.gson.Gson;
import com.visma.approval.facade.dto.Processing;
import lombok.*;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;

import javax.persistence.*;
import java.text.ParseException;
import java.util.HashMap;

/**
 * Created by robert on 06.02.2017.
 */
@Entity(name="document")
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Log4j
public class Document {
    private static Gson gson = new Gson();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long sentTime;
    private Processing processing;
    @Lob
    private HashMap<String, String> fields;

    public String get(String fieldName) {
        if (!fields.containsKey(fieldName)){
            throw new RuntimeException("Field " + fieldName + " missing");
        }
        return fields.get(fieldName);
    }

    public String getSubfield(String fieldName,String subfieldName){
        String json = get(fieldName);
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject.get(subfieldName).toString();
    }

    public <T> T get(String fieldName, Class T) {
        String json = fields.get(fieldName);
        Object result;
        try {
            result = ClassHandler.getObjectFromJson(json, T);
        }
        catch (ParseException e){
            log.error("Could not parse " + fieldName);
            log.error(e.getMessage());
            result = null;
        }
        return (T) result;
    }

}
