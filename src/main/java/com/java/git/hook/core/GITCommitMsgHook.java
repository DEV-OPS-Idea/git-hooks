package com.java.git.hook.core;

import com.java.git.hook.api.jira.JIRAService;
import com.java.git.hook.util.AppUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GITCommitMsgHook {

    public static boolean check_valid_commit(
            String commitMessage, String currentBranches, String logged_in_user){
        boolean blockedBranches = checkedBlockedBranches(currentBranches);
        String projectId= null;
        boolean isInvalidMsg = false;
        try {
            projectId = new AppUtil().getValues("/config.properties", "JIRA_PROJECT_ID");
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }

        if(blockedBranches) {
            System.out.println(
                    " Error: Unable to commit to branch "
                            + currentBranches
                            + ". It is blocked for commit. Please contact System Admin.");
            isInvalidMsg = true;
        } else {
            isInvalidMsg = false;
        }
        boolean containsIssueNumber =
                check_issue_number_in_begining_commit_message(commitMessage, projectId);
        if(!containsIssueNumber) {
            System.out.println(
                    "ERROR: Unable to commit as commit message should start with this pattern: "
                            + projectId
                            + "-1234 : [Message Description]");
            isInvalidMsg = true;
        } else {
            isInvalidMsg = false;
        }
        if(!isInvalidMsg){
            String issueKey = commitMessage.substring(0,commitMessage.indexOf(":")).trim();
            boolean checkJiraStatus = new JIRAService().check_jira_status_api(issueKey, logged_in_user);
            return blockedBranches || !containsIssueNumber || !checkJiraStatus;
        } else {
            return isInvalidMsg;
        }
    }

    private static boolean check_issue_number_in_begining_commit_message(
            String commitMessage, String projectId) {
        boolean startWithIssueNumber;
        String MESSAGE_REGEX = projectId + "-[0-9]+:.*";
        startWithIssueNumber = AppUtil.validRegex(MESSAGE_REGEX,commitMessage);
        return startWithIssueNumber;
    }

    private static boolean checkedBlockedBranches(String currentBranches) {
        List<String> blockedBranchesList = null;
        try {
            String blockedBranches = new AppUtil().getValues("/config.properties", "BLOCKED_BRANCHES");
            blockedBranchesList = new ArrayList<String>(Arrays.asList(blockedBranches.split(",")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (blockedBranchesList != null && blockedBranchesList.contains(currentBranches)) {
            return true;
        } else {
            return false;
        }
    }
}
