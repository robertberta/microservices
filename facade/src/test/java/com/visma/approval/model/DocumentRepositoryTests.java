package com.visma.approval.model;

import com.visma.approval.TestUtils;
import com.visma.approval.facade.Document;
import com.visma.approval.facade.DocumentRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;

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
    public void saveDocument() throws IOException,ParseException {
        String xml = TestUtils.resourceFileContent("document/valid/costRequest.xml");
        Document document = documentProvider.parseXml(xml);
        repository.save(document);
        Document readDocument = repository.findAll().iterator().next();
        Assert.assertEquals(document.getFields(),readDocument.getFields());
        Assert.assertEquals(document.getProcessing(),readDocument.getProcessing());

    }
}
