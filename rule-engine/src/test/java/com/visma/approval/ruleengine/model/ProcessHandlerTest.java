package com.visma.approval.ruleengine.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.visma.approval.ruleengine.ProcessRepository;
import com.visma.approval.ruleengine.Workflow;
import com.visma.approval.ruleengine.WorkflowRepository;
import com.visma.approval.ruleengine.controller.exceptions.ProcessException;
import com.visma.approval.facade.Document;
import com.visma.approval.facade.DocumentRepository;
import com.visma.approval.ruleengine.Process;
import com.visma.approval.ruleengine.controller.exceptions.WorkflowException;
import com.visma.approval.ruleengine.dto.Action;
import com.visma.approval.ruleengine.dto.ApproverAction;
import com.visma.approval.ruleengine.dto.ProcessStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.AssertTrue;
import java.text.ParseException;

import static org.junit.Assert.assertTrue;

/**
 * Created by robert on 02.03.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessHandlerTest {
    @Autowired
    ProcessHandler processHandler;
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    ProcessRepository processRepository;
    @Autowired
    WorkflowRepository workflowRepository;
    @Autowired
    WorkflowHandler workflowHandler;

    @Test
    public void testNewProcess() throws ProcessException {
        Process process = newProcess();
        Assert.assertEquals(process.getStatus(), ProcessStatus.INPROGRESS);
    }

    @Test
    public void testReject() throws ProcessException {
        Process process = addApproverAction(newProcess(), 1, Action.REJECT);
        Assert.assertEquals(process.getStatus(), ProcessStatus.REJECTED);
    }

    @Test
    public void testApprove() throws ProcessException {
        Process process = addApproverAction(newProcess(), 1, Action.APPROVE);
        Assert.assertEquals(process.getStatus(), ProcessStatus.APPROVED);
    }

    @Test
    public void rejectingAnAlreadyRejectedStepShouldThrowException() throws ProcessException{
        Boolean processException = false;

        Process process = addApproverAction(newProcess(), 1, Action.REJECT);
        try {
            addApproverAction(process, 1, Action.REJECT);
        }
        catch (ProcessException ex){
            processException = true;
        }

        assertTrue(processException);
    }

    @Test
    public void testReject2Stages() throws ProcessException,WorkflowException{

        Workflow workflow = workflowRepository.findOneByApplicationName("Visma.net Expense 1.0");
        workflowHandler.activateWorkflow(workflow.getId());

        Process process = addApproverAction(newProcess(), 1, Action.REJECT);

        Assert.assertEquals(process.getStatus(), ProcessStatus.REJECTED);
    }

    private Process addApproverAction(Process process, Integer stepNo, Action action) throws ProcessException{

        Long userId = process.getSteps().stream()
                .filter(step -> step.getStep() == stepNo)
                .map(step -> step.getAction().getIdUser())
                .findFirst()
                .get();

        processHandler.addApproverAction(process.getId(),stepNo,new ApproverAction(userId,action));

        Process updatedProcess = processRepository.findOne(process.getId());

        return updatedProcess;
    }

    private Process newProcess() throws ProcessException{
        Document document = documentRepository.findOneByProcessingApplicationName("Visma.net Cost Request 1.0");
        return processHandler.newProcess(document);
    }
}
