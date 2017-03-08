package com.visma.approval.ruleengine.model;

import com.visma.approval.ruleengine.*;
import com.visma.approval.ruleengine.Process;
import com.visma.approval.ruleengine.controller.exceptions.ProcessException;
import com.visma.approval.facade.Document;
import com.visma.approval.ruleengine.dto.*;
import com.visma.approval.ruleengine.view.WorkflowStepQueryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by robert on 24.02.2017.
 */
@Component
public class ProcessHandler {
    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private WorkflowStepQueryStore queryStore;

    @Autowired
    private ProcessRepository processRepository;

    public Process newProcess(Document document) throws ProcessException {
        Process process = new Process();
        process.setDocumentId(document.getId());
        process.setStatus(ProcessStatus.INPROGRESS);

        String applicationName = document.getProcessing().getApplicationName();

        Workflow workflow = workflowRepository.getActiveWorkflow(applicationName);
        if (workflow == null) {
            throw new ProcessException("No active workflow available for " + applicationName);
        }

        List<ProcessStep> processSteps = new ArrayList<>();

        Set<WorkflowStep> workflowSteps = workflow.getWorkflowSteps();
        for (WorkflowStep workflowStep : workflowSteps) {


            ApproverAction approverAction = null;
            Long idUser = workflowStep.getUserId();
            if (idUser != null) {
                approverAction = new ApproverAction(idUser, Action.NONE);
            } else {
                String queryName = workflowStep.getQueryName();
                idUser = queryStore.getApproverIdUser(document, queryName);
                if (idUser != null) {
                    approverAction = new ApproverAction(idUser, Action.NONE);
                } else {
                    process.setStatus(ProcessStatus.PENDING);
                }
            }

            ProcessStep processStep = new ProcessStep(null, null, workflowStep.getStage(), workflowStep.getStep(), approverAction);
            processSteps.add(processStep);
        }

        process.setCurrentStage(1);
        process.setSteps(processSteps);

        process.prepareForSave();

        Process updatedProcess = processRepository.save(process);

        newTasks(updatedProcess.getSteps());

        return process;
    }

    public void addApproverAction(Long processId, Integer stepId,ApproverAction action) throws ProcessException{
        Process process = processRepository.findOne(processId);

        List<ProcessStep> steps = process.getSteps();

        ProcessStep step = steps.stream()
                .filter(s -> s.getStep() == stepId)
                .findFirst()
                .orElseThrow(() -> new ProcessException("Step " + stepId + " not found in " + process.getId()));

        if (step.getAction().getAction() !=  Action.NONE){
            throw new ProcessException("Step " + stepId + " already contains an action ");
        }

        step.setAction(action);

        if (action.getAction() == Action.REJECT){
            deleteTasksForCurrentStage(process);
            process.setStatus(ProcessStatus.REJECTED);
        }

        if (action.getAction() == Action.APPROVE){
            Integer nextStage = process.getCurrentStage() + 1;
            List<ProcessStep> nextStageSteps = steps.stream()
                    .filter(s -> s.getStage() == nextStage)
                    .collect(Collectors.toList());
            if (nextStageSteps.isEmpty()){
                process.setStatus(ProcessStatus.APPROVED);
            }
            else{
                deleteTasksForCurrentStage(process);
                process.setCurrentStage(nextStage);
                newTasks(nextStageSteps);
            }
        }

        if (action.getAction() == Action.FORWARD){
            ProcessStep forwardStep = new ProcessStep(null, process, step.getStage(), step.getStep(), action);
            steps.add(forwardStep);
            deleteTask(step);
            newTask(forwardStep);
        }

        processRepository.save(process);
    }

    private void newTask(ProcessStep step){
        Task task = new Task(step.getId(),System.currentTimeMillis() + TimeUnit.MILLISECONDS.toDays(7));
        taskRepository.save(task);
    }

    private void newTasks(List<ProcessStep> steps){

        steps.stream().forEach(step -> newTask(step));
    }

    private void deleteTask(ProcessStep step){
        taskRepository.delete(step.getId());
    }

    private void deleteTasksForCurrentStage(Process process){
        process.getSteps().stream()
                .filter(step -> step.getStage() == process.getCurrentStage())
                .forEach(step -> deleteTask(step));
    }
}
