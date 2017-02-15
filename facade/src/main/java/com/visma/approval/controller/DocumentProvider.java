package com.visma.approval.controller;

import com.visma.approval.controller.exceptions.ParserException;
import com.visma.approval.model.Document;
import com.visma.approval.model.dto.ProcessingProvider;
import com.visma.approval.view.FieldDescriptorStore;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.visma.approval.view.ClassHandler.getObjectFromJson;

/**
 * Created by robert on 06.02.2017.
 */
@Component
public class DocumentProvider {
    @Autowired
    private FieldDescriptorStore store;

    public Document parseXml(String xml) throws ParserException {
        Document result = new Document();

        xml = stripNs(xml);
        JSONObject jsonObject = XML.toJSONObject(xml);
        jsonObject = jsonObject.getJSONObject("workflowRequest");

        ProcessingProvider processing = getObjectFromJson(jsonObject.getJSONObject("processing").toString(), ProcessingProvider.class);
        result.processing = processing.get();

        Map<String, String> fieldDescriptors = store.getAllFields(processing.getApplicationFullName());

        Map<String, String> workflowRequestFields = jsonToMap(jsonObject, fieldDescriptors);

        Date sentTime = getObjectFromJson(workflowRequestFields.get("sentTime"),Date.class);
        result.sentTime = sentTime;

        jsonObject = jsonObject.getJSONObject("workflowDocument");
        HashMap<String, String> workflowDocumentfields = jsonToMap(jsonObject, fieldDescriptors);

        result.fields = workflowDocumentfields;

        return result;
    }

    private HashMap<String, String> jsonToMap(JSONObject jsonObject, Map<String, String> mapFieldType) throws ParserException {
        HashMap<String, String> result = new HashMap<String, String>();

        Iterator<?> itFieldName = jsonObject.keys();
        while (itFieldName.hasNext()) {
            String fieldName = stripNs((String) itFieldName.next());

            String json = jsonObject.get(fieldName).toString();

            if (mapFieldType.containsKey(fieldName)) {
                String fieldType = mapFieldType.get(fieldName);
                try {
                    getObjectFromJson(json, fieldType);
                }
                catch (ClassNotFoundException e) {
                    throw new ParserException("Type " + fieldType + " is not defined");
                }
                result.put(fieldName, json);
            }
        }
        return result;
    }

    private String stripNs(String xml) {
        String result = xml.replaceAll("ns[0-9]:", "");
        return result.replaceAll(":ns[0-9]", "");
    }
}
