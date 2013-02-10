package cz.dnk.UpdateGrades;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jirka
 * Date: 11/24/12
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IAction {
    public void perform(ISConnection connection, Map<String,String> params);
    public void printHelp();
}
