package com.visma.approval.controller;

import com.visma.approval.TestUtils;
import com.visma.approval.controller.DocumentProvider;
import com.visma.approval.controller.exceptions.ParserException;
import com.visma.approval.model.Document;
import com.visma.approval.model.dto.Amount;
import com.visma.approval.view.ClassHandler;
import com.visma.approval.view.FieldDescriptorStore;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
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
    public void costRequestFieldsDontMix() throws IOException, ParserException {
        String xml = TestUtils.resourceFileContent("document/valid/costRequest.xml");

        Document document = documentProvider.parseXml(xml);

        Assert.assertEquals("Visma.net Cost Request 1.0" , document.processing.applicationName);

        Assert.assertEquals(200990066,(int) document.get("companyId",Integer.class));
        Assert.assertEquals(new Amount(13.0,"EUR"), document.get("amount",Amount.class));
        Assert.assertEquals("Test CostRequest 5afd6dce-720b-41e0-a94f-8f6918bd56ef", document.get("description"));
        Assert.assertEquals("2017-01-26T15:04:32.888+02:00", document.get("costRequestDate"));
        Assert.assertEquals(new Amount(58.5,"RON"), document.get("foreignAmount",Amount.class));

        Date expectedSentTime = ClassHandler.getObjectFromJson("2017-01-26T15:04:32.888+02:00",Date.class);
        Assert.assertEquals(expectedSentTime.toString(), document.sentTime.toString());
    }

    @Test
    public void allDocumentsFieldsAreExtracted() throws IOException, ParserException, ClassNotFoundException {
        List<String> xmls = TestUtils.resourceFolderFileContents("document/valid");

        for (String xml:xmls){
            Document document = documentProvider.parseXml(xml);

            Collection<String> fieldNames = document.getFields().keySet();

            final String applicationName = document.processing.applicationName;
            final Map<String,String> fieldDescriptors = store.getAllFields(applicationName);
            for (String fieldName:fieldNames){
                String fieldType = fieldDescriptors.get(fieldName);
                Class<?> cls = ClassHandler.getClassForName(fieldType);
                document.get(fieldName,cls);
                document.get(fieldName);
            }
        }

    }

}
