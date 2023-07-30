package com.java.git.hook.core;

import com.java.git.hook.util.AppUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GITCommitMsgHook {

    public static boolean check_valid_commit(String currentBranches){
        boolean blockedBranches=checkBlockedBranches(currentBranches);
        if(blockedBranches){
            System.out.println("ERROR: Unable to commit to branch "+currentBranches+". It is blocked for Commit. Please contact System Administrator.");
        }
        return blockedBranches;
    }
   public static boolean checkBlockedBranches(String currentBranches){
       List<String> blockedBranchesList=null;
       try {
           String blockedBranches=new AppUtil().getValues("/config.properties","BLOCKED_BRANCHES");
           blockedBranchesList = new ArrayList<String>(Arrays.asList(blockedBranches.split(",")));
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
       if(blockedBranchesList!=null && blockedBranchesList.contains(currentBranches)) {
           return true;
       }else{
           return false;
       }
   }
}
