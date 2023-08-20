package com.java.git.hook;

import com.java.git.hook.core.GITCommitMsgHook;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Commit Message::"+args[0] +"Branch Name::"+args[1]);
        boolean isBlockedBranches=GITCommitMsgHook.check_valid_commit(args[0],args[1]);
        if(isBlockedBranches){
            System.exit(1);
        }
        System.out.println( "Program Completed Successfully" );
    }
}
