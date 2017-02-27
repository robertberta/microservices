package com.visma.approval.ruleengine;

import com.visma.approval.ruleengine.dto.ProcessState;
import com.visma.approval.ruleengine.dto.ProcessStep;
import com.visma.approval.ruleengine.dto.ApproverAction;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by robert on 23.02.2017.
 */
@Entity
@Data
public class Process {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long processId;
    private Long documentId;
    @Lob
    private List<ProcessStep> processSteps;
    @Lob
    private List<ApproverAction> approverActions;
    private Integer currentStep;
    private ProcessState state;
}
