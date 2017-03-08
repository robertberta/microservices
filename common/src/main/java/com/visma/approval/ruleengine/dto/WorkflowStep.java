package com.visma.approval.ruleengine.dto;

import com.visma.approval.ruleengine.Workflow;
import lombok.*;

import javax.persistence.*;

/**
 * Created by robert on 27.02.2017.
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class WorkflowStep {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Setter
    @ManyToOne
    private Workflow workflow;
    private Integer stage;
    private Integer step;
    private Long userId;
    private String queryName;
}
