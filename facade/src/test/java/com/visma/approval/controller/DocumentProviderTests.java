package com.visma.approval.controller;

import com.visma.approval.TestUtils;
import com.visma.approval.facade.Document;
import com.visma.approval.facade.dto.Amount;
import com.visma.approval.facade.dto.FieldDescriptor;
import com.visma.approval.facade.ClassHandler;
import com.visma.approval.model.DocumentProvider;
import com.visma.approval.view.FieldDescriptorStore;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by robert on 07.02.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentProviderTests {
    @Autowired
    DocumentProvider documentProvider;
    @Autowired
    FieldDescriptorStore store;

    @Test
    public void costRequestFieldsDontMix() throws IOException, ParseException {
        String xml = TestUtils.resourceFileContent("document/valid/costRequest.xml");

        Document document = documentProvider.parseXml(xml);

        Assert.assertEquals("Visma.net Cost Request 1.0", document.getProcessing().getApplicationName());

        Assert.assertEquals(200990066, (int) document.get("companyId", Integer.class));
        Assert.assertEquals(new Amount(13.0, "EUR"), document.get("amount", Amount.class));
        Assert.assertEquals("Test CostRequest 5afd6dce-720b-41e0-a94f-8f6918bd56ef", document.get("description"));
        Assert.assertEquals("2017-01-26T15:04:32.888+02:00", document.get("costRequestDate"));
        Assert.assertEquals(new Amount(58.5, "RON"), document.get("foreignAmount", Amount.class));

        Date expectedSentTime = ClassHandler.getObjectFromJson("2017-01-26T15:04:32.888+02:00", Date.class);
        Assert.assertEquals(expectedSentTime.toString(), document.getSentTime().toString());
    }

    @Test
    public void allDocumentsFieldsAreExtracted() throws IOException, ParseException, ClassNotFoundException {
        List<String> xmls = TestUtils.resourceFolderFileContents("document/valid");

        for (String xml : xmls) {
            Document document = documentProvider.parseXml(xml);

            Collection<String> fieldNames = document.getFields().keySet();

            final String applicationName = document.getProcessing().getApplicationName();
            final Map<String, String> fieldDescriptors = store.getAllFields(applicationName);
            for (String fieldName : fieldNames) {
                String fieldType = fieldDescriptors.get(fieldName);
                Class<?> cls = ClassHandler.getClassForName(fieldType);
                document.get(fieldName, cls);
                document.get(fieldName);
            }
        }
    }

    @Test
    public void mandatoryFieldsNonEmpty() throws IOException, ParseException {
        List<String> xmls = TestUtils.resourceFolderFileContents("document/valid");

        for (String xml : xmls) {
            Document document = documentProvider.parseXml(xml);

            final String applicationName = document.getProcessing().getApplicationName();
            final List<FieldDescriptor> mandatoryFields = store.getMandatoryFields(applicationName);
            for (FieldDescriptor mandatoryField : mandatoryFields) {
                assertFalse(document.get(mandatoryField.name).isEmpty());
            }
        }
    }

    @Test
    public void missingMandatoryThrowsException() throws IOException{
        Boolean parseException = false;
        String xml = TestUtils.resourceFileContent("document/invalid/missingMandatory.xml");

        try {
            documentProvider.parseXml(xml);
        }
        catch (ParseException ex){
           parseException = true;
        }

        assertTrue(parseException);
    }
}
