package com.visma.approval.ruleengine.view;

import com.visma.approval.facade.Document;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by robert on 28.02.2017.
 */
@Component
@Log4j
public class WorkflowStepQueryStore {
    private HashMap<String,String> queries = new HashMap<>();
    private HashMap<String,List<String>> applicationQueryNames = new HashMap<>();
    private List<String> commonQueryNames = new ArrayList<>();

    @Autowired
    JdbcTemplate jdbcTemplate;

    public WorkflowStepQueryStore(){

        queries.put("amount above","SELECT idUser FROM approvaldb.amountlimit " +
                "WHERE amount>document.amount.amount AND currency=\"document.amount.isoCode\" " +
                "ORDER BY amount DESC");

        queries.put("amount below","SELECT idUser FROM amountlimit " +
                "WHERE amount>document.amount.amount AND currency=\"document.amount.isoCode\" " +
                "ORDER BY amount ASC");

        commonQueryNames.add("amount above");

        List<String> queryNames = Arrays.asList("amount below");
        applicationQueryNames.put("Visma.net Cost Request 1.0",queryNames);
    }

    public List<String> getQueryNames(String applicationName) {
        return ListUtils.union(commonQueryNames , applicationQueryNames.get(applicationName));
    }

    public Map<String,String> getQueryNames() {
        Map <String,String> result = new HashMap<>();

        commonQueryNames.forEach( q-> result.put("Generic application",q));

        applicationQueryNames.forEach((appName,queryNames) ->
                queryNames.forEach(queryName ->
                        result.put(appName,queryName)
                ));

        return result;
    }

    public Long getApproverIdUser(Document document, String queryName){
        Long result = null;

        String applicationName = document.getProcessing().getApplicationName();

        if (commonQueryNames.contains(queryName) ||
            applicationQueryNames.getOrDefault(applicationName,new ArrayList<String>()).contains(queryName)){

            String query = queries.get(queryName);
            query = replaceDocumentFields(query,document);
            try {
                List<Long> results = jdbcTemplate.query(query,(rs, rowNum)->rs.getLong(1));
                if (!results.isEmpty()){
                    result = results.get(0);
                }
                else {
                    log.warn("Query " + query + " did not match any user");
                }

            }catch (InvalidResultSetAccessException ex){
                log.warn("Sql query " + queryName + " for " + applicationName + "not found or failed");
            }
        }
        return result;
    }

    private String replaceDocumentFields(String query, Document document) {
        Pattern p = Pattern.compile("(?<=document\\.)[[a-z],[A-Z],[1-9],\\.]*");
        Matcher m = p.matcher(query);
        StringBuffer strBuffer = new StringBuffer();
        while (m.find()) {
            String[] groups = m.group(0).split("\\.");
            if (groups.length == 1){
                m.appendReplacement(strBuffer, document.get(groups[0]));
            }
            else if (groups.length == 2){
                m.appendReplacement(strBuffer, document.getSubfield(groups[0],groups[1]));
            }
            else {
                throw new RuntimeException("Document field nesting level error. " + "Query:" + query);
            }
        }
        m.appendTail(strBuffer);
        String result  = strBuffer.toString().replaceAll("document\\.","");
        return result;
    }

}
