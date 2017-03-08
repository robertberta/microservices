package com.visma.approval.ruleengine.view;

import com.google.gson.Gson;
import com.visma.approval.facade.Document;
import com.visma.approval.facade.DocumentRepository;
import com.visma.approval.facade.dto.Amount;
import com.visma.approval.facade.dto.Processing;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by robert on 01.03.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkflowStepQueryStoreTest {
    @Autowired
    WorkflowStepQueryStore queryStore;
    @Autowired
    DocumentRepository documentRepository;

    private static Gson gson = new Gson();

    private Long now;
    private HashMap<String,String> fields;

    @Before
    public void init(){
        now = System.currentTimeMillis();
        Amount amount = new Amount(14.7,"EUR");
        fields = new HashMap<>();
        fields.put("amount",gson.toJson(amount));
    }

    @Test
    public void testAmountBelow(){

        Document document = documentRepository.findOneByProcessingApplicationName("Visma.net Cost Request 1.0");
        long idUser = queryStore.getApproverIdUser(document,"amount above");
        assertEquals(idUser,1001);
    }

    @Test
    public void testAllQueries(){

        Map<String,String> queryNames = queryStore.getQueryNames();

        queryNames.forEach((appName,queryName) -> {
            Processing processing = new Processing(appName,false);
            Document document = new Document(2L, now, processing, fields);
            Long idUser = queryStore.getApproverIdUser(document, queryName);
            assertTrue(idUser != null && idUser!=0);
        });
    }

//    @Test
//    public void tmp(){
//        WorkflowStep stage1 = new WorkflowStep(null,null,new HashSet<>(Arrays.asList(new WorkflowStep(null,null,1002L,null))));
//        WorkflowStep stage2 = new WorkflowStep(null,null,new HashSet<>(Arrays.asList(new WorkflowStep(null,null,1003L,null),new WorkflowStep(null,"amount above"))));
//        Workflow workflow = new Workflow(null,Arrays.asList(stage1,stage2),"Visma.net Cost Request 1.0",true);
//        String result = gson.toJson(workflow);
//        System.out.println(result);
//    }
}
