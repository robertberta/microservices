package com.visma.approval.ruleengine.dto;

import lombok.Value;

import java.util.List;

/**
 * Created by robert on 27.02.2017.
 */
@Value
public class WorkflowStep {
    private List<WorkflowStepType> stepTypes;
}
