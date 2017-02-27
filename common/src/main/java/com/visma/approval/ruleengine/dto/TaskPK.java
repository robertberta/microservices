package com.visma.approval.ruleengine.dto;

import lombok.Value;

import javax.persistence.Embeddable;

/**
 * Created by robert on 27.02.2017.
 */
@Value
@Embeddable
public class TaskPK {
    private Long idCompany;
    private Long idUser;
    private Integer idProcess;
}
