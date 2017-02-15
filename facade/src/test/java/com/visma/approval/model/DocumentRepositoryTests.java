package com.visma.approval.model;

import com.visma.approval.TestUtils;
import com.visma.approval.controller.DocumentProvider;
import com.visma.approval.controller.exceptions.ParserException;
import com.visma.approval.model.dto.ProcessingProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Created by robert on 13.02.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentRepositoryTests {
    @Autowired
    DocumentProvider documentProvider;
    @Autowired
    DocumentRepository repository;

    @Test
    public void saveDocument() throws IOException,ParserException{
        String xml = TestUtils.resourceFileContent("document/valid/costRequest.xml");
        Document document = documentProvider.parseXml(xml);
        repository.save(document);
        Document readDocument = repository.findAll().iterator().next();
        if (Math.abs(readDocument.sentTime.getTime()-document.sentTime.getTime())<1000){
            readDocument.sentTime = document.sentTime;
        }
        Assert.assertEquals(document,readDocument);
        ProcessingProvider processing = new ProcessingProvider();
    }
}
