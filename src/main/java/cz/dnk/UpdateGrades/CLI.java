package cz.dnk.UpdateGrades;

import com.beust.jcommander.JCommander;

/**
 * Created with IntelliJ IDEA.
 * User: jirka
 * Date: 11/24/12
 * Time: 8:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CLI {
    public static boolean DEBUG = false;
    public static boolean LOGHTTP = false;

    public static void ExitWithDisclaimer() {
        System.out.println("If the problem persists, IS may have been changed and this script may be therefore broken");
        System.exit(1);
    }
    public static void main(String[] args) {
        CommandLineArgs myCommandLineArgs = new CommandLineArgs();
        try {
            JCommander jc = new JCommander(myCommandLineArgs, args);
            if (myCommandLineArgs.help) {
                jc.usage();
                System.exit(0);
            }
        } catch (com.beust.jcommander.ParameterException e) {
            System.out.println("Chyba pri zadavani parametru");
            System.out.println(e);
            return;
        }

        if (CLI.LOGHTTP) {
            java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
            java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);

            System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
            System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
            System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
            System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "debug");
            System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "debug");
        }

        if (myCommandLineArgs.help) {

        }

        if (myCommandLineArgs.ACTION == null) {
            System.out.println("Parameter '--action' needs to be specified");
            return;
        }

        IAction action;
        if (myCommandLineArgs.ACTION.equals("grades")) {
            action = new GradesAction();
        } else if(myCommandLineArgs.ACTION.equals("file")) {
            action = new FileAction();
        } else {
                System.out.println("The value of parameter '--action' does not match any implemented action");
                return;
        }

        String UCO = null;
        String PASSWORD = null;

        if (myCommandLineArgs.PARAMETERS) {
            action.printHelp();
            return;
        }

        Credentials myCredentials = new Credentials(myCommandLineArgs.UCO, myCommandLineArgs.PASSWORD, "password.txt");
        if (myCredentials.getName() == null) {
            System.out.println("Could not get UCO to use for logging in");
            return;
        }
        if  (myCredentials.getPass() == null) {
            System.out.println("Could not get password to use for logging in");
            return;
        }

        ISConnection connection = new ISConnection();
        connection.authenticate(myCredentials);

        action.perform(connection, myCommandLineArgs.DPARAMS);
    }
}
