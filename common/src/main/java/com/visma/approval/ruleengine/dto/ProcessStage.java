package com.visma.approval.ruleengine.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

/**
 * Created by robert on 23.02.2017.
 */
@Value
public class ProcessStep {
  List<Long> userIds;
}
