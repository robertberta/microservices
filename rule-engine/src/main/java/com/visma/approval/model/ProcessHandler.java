package com.visma.approval.model;

import com.visma.approval.facade.Document;
import com.visma.approval.ruleengine.dto.ProcessState;
import com.visma.approval.ruleengine.dto.ProcessStep;
import com.visma.approval.ruleengine.dto.ApproverAction;
import lombok.Data;
import com.visma.approval.ruleengine.Process;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by robert on 24.02.2017.
 */
@Data
public class ProcessHandler {

    public Process createNewProcess(Document document){
        Process process;
        process = new Process();
        process.setDocumentId(document.getId());
        process.setState(ProcessState.INPROGRESS);
        process.setCurrentStep(1);
        ProcessStep processStep = new ProcessStep(Arrays.asList(200L));
        process.setProcessSteps(Arrays.asList(processStep));
        process.setApproverActions(new ArrayList<ApproverAction>());
        return process;
    }
}
