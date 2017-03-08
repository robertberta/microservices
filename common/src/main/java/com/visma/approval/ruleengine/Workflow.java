package com.visma.approval.ruleengine;

import com.visma.approval.ruleengine.dto.WorkflowStep;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by robert on 27.02.2017.
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Workflow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,mappedBy="workflow")
    private Set<WorkflowStep> workflowSteps;
    private String applicationName;
    @Setter
    private Boolean active;
    public void prepareForSave()
    {
        for (WorkflowStep stage: workflowSteps){
            stage.setWorkflow(this);
        }
    }
}
