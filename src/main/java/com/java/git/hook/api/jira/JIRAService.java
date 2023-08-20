package com.java.git.hook.api.jira;


import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.java.git.hook.constant.JiraConstant;

import java.net.URI;

public class JIRAService {
    private final JiraRestClient restClient;

    public JIRAService() {
        this.restClient = getJiraRestClient();
    }

    public JiraRestClient getJiraRestClient() {
        return new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(
                URI.create(JiraConstant.JIRA_URL),JiraConstant.username,JiraConstant.password
        );

    }

    public boolean check_jira_status_api(String issueKey, String loggedInUser) {
        Issue issue = null;
        boolean isCommit = false;
        try {
            issue = this.getIssue(issueKey);
        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println("ERROR: Issue with " + issueKey + "Does Not Exist");
            isCommit = false;
        }
        if (issue != null) {
            String issueStatus = issue.getStatus().getStatusCategory().getName();
            String username = null;
            String displayName = null;
            String emailName = null;
            User user = issue.getAssignee();
            if (user == null) {
                System.out.println(
                        "ERROR: " + issueKey + "is in unassigned state. Please assign it before commit");
                isCommit = false;
            } else {
                username = user.getName();
                displayName = user.getDisplayName();
                emailName = user.getEmailAddress();
                isCommit = true;
            }
            if(isCommit) {
                if (username != null && username.equals(loggedInUser)) {
                    isCommit = true;
                } else {
                    System.out.println("Error: " + issueKey + " is not your username. Please check issue status");
                    isCommit = false;
                }
            }
            if(isCommit) {
                if(issueStatus!=null && issueStatus.equals("In Progress")){
                    isCommit=true;
                }else{
                    System.out.println("Error: " + issueKey + " is not In Progress Status. Please check issue status");
                    isCommit = false;
                }
            }
        }
       return isCommit;
    }

    private Issue getIssue(String issueKey) {
       return restClient.getIssueClient().getIssue(issueKey).claim();
    }
}
