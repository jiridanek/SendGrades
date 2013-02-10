package cz.dnk.UpdateGrades;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jirka
 * Date: 11/1/12
 * Time: 12:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommandLineArgs {
    @Parameter(names = "--uco", description = "UCO to use when logging into IS.")
    public String UCO;

    @Parameter(names = "--password", description = "Password to use when logging into IS. It is not encouraged to provide password as command line argument because of security concerns.")
    public String PASSWORD;

    @Parameter(names = "--action", description = "Acton to perform. Can be one of: 'grades' to upload grades, or 'file' to upload a file to document directory.")
    public String ACTION;

    @DynamicParameter(names = "-D", description = "Additional parameters for action. For example '-Dfile=znamky.csv'. Can be specified multiple times if there is more additional parameters to be specified.")
    public Map<String, String> DPARAMS = new HashMap<String, String>();

    @Parameter(names = "--parameters", description = "Print description for all additional parameters -Dsomething for a selected action. Use together with '--action'.")
    public boolean PARAMETERS = false;

    @Parameter(names = "--help", help = true)
    public boolean help;

    //test
    public static void main(String[] args) {
        CommandLineArgs myCommandLineArgs = new CommandLineArgs();
        new JCommander(myCommandLineArgs, args);
        System.out.println(myCommandLineArgs.UCO);
        System.out.println(myCommandLineArgs.PASSWORD);
        System.out.println(myCommandLineArgs.ACTION);
        System.out.println(myCommandLineArgs.DPARAMS);
        System.out.println(myCommandLineArgs.PARAMETERS);
    }
}
