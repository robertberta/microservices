package com.visma.approval.ruleengine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.*;

/**
 * Created by robert on 27.02.2017.
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(force=true)
public class Task {
    @Id
    private Long processStepId;
    private Long dueDate;
}
