package cz.dnk.UpdateGrades;

/**
 * Created with IntelliJ IDEA.
 * User: jirka
 * Date: 10/21/12
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Credentials {
    String name;
    String pass;

    public Credentials(String name, String pass, String path) {
        this.name = null;
        this.pass = null;

        // the user provided in on command line
        if (name != null) {
            this.name = name;
            if (pass != null) {
                this.pass = pass;
                return;
            }
        }

        // get the missing credentials from a file
        PasswordFile passwordFile = new PasswordFile(path);
        passwordFile.loadCredentials();
        if (this.name != null) {
            if (passwordFile.getName() != null) {
                if (!passwordFile.getName().equals(this.name)) {
                    System.out.println("Name entered on command line does not match the name in the config file.");
                    return;
                }
            }
        } else {
            this.name = passwordFile.getName();
        }
        if (this.pass == null) {
            this.pass = passwordFile.getPass();
        }

        // the last resort, show interactive dialog
        if (this.name == null || this.pass == null) {
            PasswordDialog passwordDialog = new PasswordDialog();
            if (passwordDialog.showDialog(this.name)) {
                this.name = passwordDialog.getName();
                this.pass = passwordDialog.getPass();
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }
}
