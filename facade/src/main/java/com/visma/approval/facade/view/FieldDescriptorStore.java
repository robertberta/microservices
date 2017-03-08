package com.visma.approval.facade.view;

import com.visma.approval.facade.dto.FieldDescriptor;
import org.springframework.stereotype.Component;
import org.apache.commons.collections.ListUtils;

import java.text.ParseException;
import java.util.*;

/**
 * Created by robert on 09.02.2017.
 */

@Component
public class FieldDescriptorStore {
    private final Map<String, List<FieldDescriptor>> mapMandatory = new HashMap<>();
    private final Map<String, List<FieldDescriptor>> mapOptional = new HashMap<>();
    private final List<FieldDescriptor> listWorkflowRequest = new ArrayList<>();
    private final Map<String, Map<String, String>> mapAllFields = new HashMap<>();

    public FieldDescriptorStore() {
        listWorkflowRequest.add(new FieldDescriptor("sentTime", "String"));
        listWorkflowRequest.add(new FieldDescriptor("submitter", "Integer"));
        listWorkflowRequest.add(new FieldDescriptor("processing", "ProcessingAdapter"));

        List<FieldDescriptor> mandatory = new ArrayList<>();
        List<FieldDescriptor> optional = new ArrayList<>();

        mandatory.add(new FieldDescriptor("companyId", "Integer"));
        mandatory.add(new FieldDescriptor("documentId", "DocumentId"));
        mandatory.add(new FieldDescriptor("amount", "Amount"));
        optional.add(new FieldDescriptor("description", "String"));
        optional.add(new FieldDescriptor("costRequestDate", "Date"));
        optional.add(new FieldDescriptor("foreignAmount", "Amount"));
        addWorkflowDocument("Visma.net Cost Request 1.0", mandatory, optional);

        mandatory.add(new FieldDescriptor("companyId", "Integer"));
        mandatory.add(new FieldDescriptor("documentId", "DocumentId"));
        mandatory.add(new FieldDescriptor("amount", "Amount"));
        optional.add(new FieldDescriptor("description", "String"));
        optional.add(new FieldDescriptor("expenseDate", "Date"));
        optional.add(new FieldDescriptor("foreignAmount", "Amount"));
        addWorkflowDocument("Visma.net Expense 1.0", mandatory, optional);

    }

    public List<FieldDescriptor> getMandatoryFields(String workflowDocumentName) throws ParseException {
        check(workflowDocumentName);
        return mapMandatory.get(workflowDocumentName);
    }

    public List<FieldDescriptor> getOptionalFields(String workflowDocumentName) throws ParseException {
        check(workflowDocumentName);
        return mapOptional.get(workflowDocumentName);
    }

    public Set<String> getWorkflowDocumentNames() {
        return mapMandatory.keySet();
    }

    public Map<String, String> getAllFields(String workflowDocumentName) throws ParseException {
        check(workflowDocumentName);
        return mapAllFields.get(workflowDocumentName);
    }

    private void addWorkflowDocument(String workflowDocumentName, List<FieldDescriptor> mandatory, List<FieldDescriptor> optional) {
        List clonedMandatory = new ArrayList<>();
        clonedMandatory.addAll(mandatory);
        mandatory.clear();
        List clonedOptional = new ArrayList<>();
        clonedOptional.addAll(optional);
        optional.clear();

        mapMandatory.put(workflowDocumentName, clonedMandatory);
        mapOptional.put(workflowDocumentName, clonedOptional);

        List<FieldDescriptor> fieldDescriptors = ListUtils.union(clonedMandatory, clonedOptional);
        fieldDescriptors = ListUtils.union(fieldDescriptors, listWorkflowRequest);

        Map<String, String> fieldType = new HashMap<String, String>();

        for (FieldDescriptor fieldDescriptor : fieldDescriptors) {
            fieldType.put(fieldDescriptor.name, fieldDescriptor.type);
        }

        mapAllFields.put(workflowDocumentName, fieldType);

    }

    private void check(String workflowDocumentName) throws ParseException {
        if (workflowDocumentName == null) {
            throw new ParseException("WorkflowDocument not defined",0);
        }
        if (!mapOptional.containsKey(workflowDocumentName) ||
            !mapMandatory.containsKey(workflowDocumentName) ||
            !mapAllFields.containsKey(workflowDocumentName)) {
            throw new ParseException("Fields not defined for " + workflowDocumentName,0);
        }
    }

}
