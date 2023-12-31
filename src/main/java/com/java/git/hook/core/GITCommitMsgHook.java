package com.java.git.hook.core;

import com.java.git.hook.api.jira.JIRAService;
import com.java.git.hook.constant.JiraConstant;
import com.java.git.hook.util.AppUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GITCommitMsgHook {


    public static boolean check_valid_commit(String commitMessage, String currentBranches, String logged_in_user) {
        boolean blockedBranches = checkBlockedBranches(currentBranches);
        String projectId=null;
        boolean isInvalidMsg = false;
        try {
             projectId = new AppUtil().getValues("/config.properties", "JIRA_PROJECT_ID");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (blockedBranches) {
            System.out.println(JiraConstant.ANSI_RED+"ERROR: Unable to commit to branch " + currentBranches + ". It is blocked for Commit. Please contact System Administrator." + JiraConstant.ANSI_RESET);
            isInvalidMsg = true;
        } else {
            isInvalidMsg = false;
        }
        boolean containsIssueNumber = check_issuenumber_in_begining_commit_message(commitMessage,projectId);
        if (!containsIssueNumber) {
            System.out.println(JiraConstant.ANSI_RED+"ERROR: Unable to commit as Commit Message should start with this pattern: "+projectId+"-1234 :[Message Description]"+ JiraConstant.ANSI_RESET);
            isInvalidMsg = true;
        } else {
            isInvalidMsg = false;
        }

        if (!isInvalidMsg) {
            String issueKey = commitMessage.substring(0,commitMessage.indexOf(":")).trim();
            boolean checkJiraStatus = new JIRAService().check_jira_status_api(issueKey,logged_in_user);
            return blockedBranches || !containsIssueNumber || !checkJiraStatus;
        } else {
            return isInvalidMsg;
        }
    }

    private static boolean check_issuenumber_in_begining_commit_message(String commitMessage, String projectId) {
        boolean startWithIssueNumber;
        String MESSAGE_REGEX = projectId + "-[0-9]+:.*";
        startWithIssueNumber = AppUtil.validRegex(MESSAGE_REGEX, commitMessage);
        return startWithIssueNumber;
    }

    private static boolean checkBlockedBranches(String currentBranches) {
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
