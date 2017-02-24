package com.visma.approval.ruleengine;

import com.visma.approval.facade.Document;
import com.visma.approval.ruleengine.dto.ProcessState;
import com.visma.approval.ruleengine.dto.Step;
import com.visma.approval.ruleengine.dto.UserAction;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.List;

/**
 * Created by robert on 23.02.2017.
 */
@Entity
@Data
public class Process {
    @Id
    private Integer documentId;
    @Lob
    private List<Step> steps;
    @Lob
    private List<UserAction> userActions;
    private Integer currentStep;
    private ProcessState state;
}
