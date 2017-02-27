package com.visma.approval.ruleengine;

import com.visma.approval.ruleengine.dto.TaskPK;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;

/**
 * Created by robert on 27.02.2017.
 */
public class Task {
    @EmbeddedId
    private TaskPK pk;
    private Long dueDate;
}
