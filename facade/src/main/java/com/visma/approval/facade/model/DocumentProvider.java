package com.visma.approval.facade.model;

import com.visma.approval.facade.Document;
import com.visma.approval.facade.dto.FieldDescriptor;
import com.visma.approval.facade.dto.Processing;
import com.visma.approval.facade.view.FieldDescriptorStore;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

import static com.visma.approval.facade.ClassHandler.getObjectFromJson;

/**
 * Created by robert on 06.02.2017.
 */
@Component
public class DocumentProvider {
    @Autowired
    private FieldDescriptorStore store;

    public Document parseXml(String xml) throws ParseException {

        xml = stripNs(xml);
        JSONObject jsonObject = XML.toJSONObject(xml);
        jsonObject = jsonObject.getJSONObject("workflowRequest");

        ProcessingAdapter processingAdapter = getObjectFromJson(jsonObject.getJSONObject("processing").toString(), ProcessingAdapter.class);
        Processing processing = processingAdapter.get();

        Map<String, String> fieldDescriptors = store.getAllFields(processing.getApplicationName());

        Map<String, String> workflowRequestFields = jsonToMap(jsonObject, fieldDescriptors);

        Date sentTime = getObjectFromJson(workflowRequestFields.get("sentTime"),Date.class);

        jsonObject = jsonObject.getJSONObject("workflowDocument");
        HashMap<String, String> workflowDocumentfields = jsonToMap(jsonObject, fieldDescriptors);


        Document document = new Document(null,sentTime.getTime(), processingAdapter.get(),workflowDocumentfields);
        checkMandatory(document);

        return document;
    }

    private HashMap<String, String> jsonToMap(JSONObject jsonObject, Map<String, String> mapFieldType) throws ParseException {
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
                    throw new ParseException("Type " + fieldType + " is not defined",0);
                }
                result.put(fieldName, json);
            }
        }
        return result;
    }

    private void checkMandatory(Document document) throws ParseException{
        Processing processing = document.getProcessing();
        List<FieldDescriptor> mandatoryFields = store.getMandatoryFields(processing.getApplicationName());
        for (FieldDescriptor mandatoryField:mandatoryFields){
            try{
                document.get(mandatoryField.name);
            }
            catch (RuntimeException ex){
                throw new ParseException("Mandatory field " +  mandatoryField.name + "is missing",0);
            }
        }
    }

    private String stripNs(String xml) {
        String result = xml.replaceAll("ns[0-9]:", "");
        return result.replaceAll(":ns[0-9]", "");
    }
}
