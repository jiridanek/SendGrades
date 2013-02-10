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
    public static void main(String[] args) {
        CommandLineArgs myCommandLineArgs = new CommandLineArgs();
        try {
            new JCommander(myCommandLineArgs, args);
        } catch (com.beust.jcommander.ParameterException e) {
            System.out.println("Chyba pri zadavani parametru");
            System.out.println(e);
            return;
        }

//        java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
//        java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);
//
//        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
//        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
//        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
//        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "debug");
//        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "debug");

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

//        for (Cookie cooke : cookieStore.getCookies()) {
//            System.out.println(cooke.toString());
//        }
//
//        HttpHost target = (HttpHost) localContext.getAttribute(
//                ExecutionContext.HTTP_TARGET_HOST);
//        HttpUriRequest req = (HttpUriRequest) localContext.getAttribute(
//                ExecutionContext.HTTP_REQUEST);
//
//
//
//
//        System.out.println("Target host: " + target);
//        System.out.println("Final request URI: " + req.getURI()); // relative URI (no proxy used)
//        System.out.println("Final request method: " + req.getMethod());
        //        httpPost = myWebUpload.SendFile(file, "nazev", "popis", "njmeno", "er", magicNumber);
//
//
//        try {
//            HttpResponse httpResponse = httpClient.execute(httpPost, localContext);
//            HttpEntity httpEntity = httpResponse.getEntity();
//            InputStream inputStream = httpEntity.getContent();
//
//            InputStreamReader is = new InputStreamReader(inputStream);
//            BufferedReader br = new BufferedReader(is);
//            String read = br.readLine();
//
//            while(read != null) {
//                System.out.println(read);
//                read = br.readLine();
//            }
//
//            inputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//        for (Cookie cooke : cookieStore.getCookies()) {
//            System.out.println(cooke.toString());
//        }
//
//        target = (HttpHost) localContext.getAttribute(
//                ExecutionContext.HTTP_TARGET_HOST);
//        req = (HttpUriRequest) localContext.getAttribute(
//                ExecutionContext.HTTP_REQUEST);
//        System.out.println("Target host: " + target);
//        System.out.println("Final request URI: " + req.getURI()); // relative URI (no proxy used)
//        System.out.println("Final request method: " + req.getMethod());
//
//        httpClient.getConnectionManager().shutdown();
    }
}
