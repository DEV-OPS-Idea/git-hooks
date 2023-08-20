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
        System.out.println( "Hello ::"+args[0] );
        boolean isInValidCommitMsg = GITCommitMsgHook.check_valid_commit(args[0],args[1],args[2]);
        if(isInValidCommitMsg){
            System.exit(1);
        }
        System.out.println("Program completed successfully");
    }
}
