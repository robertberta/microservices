package com.visma.approval.model;

import com.visma.approval.facade.Document;
import com.visma.approval.ruleengine.dto.ProcessState;
import com.visma.approval.ruleengine.dto.Step;
import com.visma.approval.ruleengine.dto.UserAction;
import lombok.Data;
import com.visma.approval.ruleengine.Process;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by robert on 24.02.2017.
 */
@Data
public class ProcessProvider {
    private Process process;
    public ProcessProvider(Document document){
        process = new Process();
        process.setCurrentStep(1);
        process.setDocumentId(document.getId());
        process.setState(ProcessState.INPROGRESS);
        process.setCurrentStep(1);
        Step step = new Step(Arrays.asList(200L));
        process.setSteps(Arrays.asList(step));
        process.setUserActions(new ArrayList<UserAction>());
    }

}
