package com.visma.approval.ruleengine;

import com.visma.approval.ruleengine.dto.ProcessStep;
import com.visma.approval.ruleengine.dto.ProcessStatus;
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
    private Long id;
    private Long documentId;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,mappedBy="process")
    private List<ProcessStep> steps;
    private Integer currentStage;
    private ProcessStatus status;

    public void prepareForSave()
    {
        for (ProcessStep step: steps){
            step.setProcess(this);
        }
    }
}
