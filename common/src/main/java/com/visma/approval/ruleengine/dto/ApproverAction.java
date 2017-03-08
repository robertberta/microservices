package com.visma.approval.ruleengine.dto;

import lombok.*;

import javax.persistence.Embeddable;

/**
 * Created by robert on 24.02.2017.
 */
@Embeddable
@Value
@NoArgsConstructor(force=true)
@AllArgsConstructor
public class ApproverAction {
    private Long idUser;
    private Action action;
}
