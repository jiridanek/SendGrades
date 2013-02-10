package cz.dnk.UpdateGrades;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jirka
 * Date: 10/21/12
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordFile {
    String path;
    String name;
    String pass;
    public PasswordFile(String path) {
        this.path = path;
        this.name = null;
        this.pass = null;
    }

    public boolean loadCredentials() {
        try {
            FileReader fileReader = new FileReader(this.path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            try {
                this.name = bufferedReader.readLine().trim();
                if (this.name.equals("")) {
                    this.name = null;
                }
                this.pass = bufferedReader.readLine().trim();
                if (this.pass.equals("")) {
                    this.pass = null;
                }
            } catch (IOException e) {
                // ignore
            }
        } catch (FileNotFoundException e) {
            System.out.println("--password file file '" + path + "' does not exist");
        }

        return this.pass != null;
    }
    public String getName() {
        return this.name;
    }
    public String getPass() {
        return this.pass;
    }
}
