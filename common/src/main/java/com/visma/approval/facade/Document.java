package com.visma.approval.facade;

import com.visma.approval.facade.dto.Processing;
import lombok.*;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by robert on 06.02.2017.
 */
@Entity
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Document {
    private static final Logger log = Logger.getLogger(Document.class.getName());
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long sentTime;
    private Processing processing;
    @Lob
    private HashMap<String, String> fields;

    public String get(String fieldName) {

        return fields.get(fieldName);
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
