package cz.dnk.UpdateGrades;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: jirka
 * Date: 2/10/13
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 * --action file -Dfile="0316.txt" -Dnazev="mNazev" -Dpopis="mPopis" -Dnjmeno="mNjmeno" -Dopt="wr" -Dfolder="/www/374368/38786227/"
 */
public class FileAction implements IAction {
    @Override
    public void perform(ISConnection connection, Map<String, String> params) {
        List<String> keys = new ArrayList<String>();
        keys.add("folder");
        keys.add("file");
        keys.add("nazev");
        keys.add("popis");
        keys.add("njmeno");
        keys.add("opt");

        boolean failed = false;
        for(String key:keys) {
            if (params.get(key) == null) {
                System.out.println("The -D" + key + " Additional parameter is not specified.");
                failed = true;
            }
        }

        if (!params.get("opt").equals("er") &&  !params.get("opt").equals("wr") && !params.get("opt").equals("re")) {
            System.out.println("The -Dopt Additional parameter must be 'er', 'wr' or 're'");
            failed = true;
        }

        File file = null;
        if (params.get("file") != null) {
            file = new File(params.get("file"));
            if (!file.exists()) {
                System.out.println("File '" + params.get("file") + "' does not exist.");
                failed = true;
            }
        }

        if (failed) {
            return;
        }

        String magicNumber;

        HttpPost openForm = OpenForm();
        String form = connection.performRequest(openForm);
        magicNumber = ISConnection.ExtractMagicNumber(form);
        HttpPost sendFile = SendFile(params.get("folder"),
                file,
                params.get("nazev"),
                params.get("popis"),
                params.get("njmeno"),
                params.get("opt"),
                magicNumber);
        String status = connection.performRequest(sendFile);
        String msg = ISConnection.ExtractMessage(status);
        if (msg != null) {
            System.out.println(msg);
        } else {
            System.out.println("Processing finished. Could not parse status message.");
            CLI.ExitWithDisclaimer();
        }
    }

    @Override
    public void printHelp() {

    }

    public HttpPost OpenForm() {
        URIBuilder builder = new URIBuilder();
        builder
                .setScheme("https")
                .setHost("is.muni.cz")
                .setPath("/auth/dok/rfmgr.pl");
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        HttpPost httpPost = new HttpPost(uri);

        MultipartEntity multipartEntity = new MultipartEntity();

        try {
            multipartEntity.addPart("use", new StringBody("fmgrfo"));
            multipartEntity.addPart("so", new StringBody("pd"));
            multipartEntity.addPart("op", new StringBody("vlso"));
            multipartEntity.addPart("furl2", new StringBody(""));
            multipartEntity.addPart("ch2", new StringBody(""));
            multipartEntity.addPart("furlf", new StringBody("/www/374368/"));

            multipartEntity.addPart("submit", new StringBody("Použít"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        httpPost.setEntity(multipartEntity);
        return httpPost;
    }

    /**
     *
     * @param file
     * @param nazev
     * @param popis
     * @param njmeno
     * @param opt er ohlásit chybu, wr přepsat, re nový přejmenovat
     * @param magicNumber
     * @return
     */
    public HttpPost SendFile(String furl, File file, String nazev, String popis, String njmeno, String opt, String magicNumber) {
        URIBuilder builder = new URIBuilder();
        builder
                .setScheme("https")
                .setHost("is.muni.cz")
                .setPath("/auth/dok/rfmgr.pl");
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        HttpPost httpPost = new HttpPost(uri);

        MultipartEntity multipartEntity = new MultipartEntity();

        try {
            multipartEntity.addPart("op", new StringBody("vlso"));
            multipartEntity.addPart("furl", new StringBody(furl));
            multipartEntity.addPart("so", new StringBody("pd"));
            multipartEntity.addPart("info", new StringBody("1"));
            multipartEntity.addPart("_", new StringBody(magicNumber));
            multipartEntity.addPart("furl2", new StringBody(""));
            multipartEntity.addPart("FILE_1", new FileBody(file));
            multipartEntity.addPart("NAZEV_1", new StringBody(nazev));
            multipartEntity.addPart("POPIS_1", new StringBody(popis));
            multipartEntity.addPart("NJMENO_1", new StringBody(njmeno));
            multipartEntity.addPart("OPT", new StringBody(opt));
            multipartEntity.addPart("proved", new StringBody("Zavést"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        httpPost.setEntity(multipartEntity);
        return httpPost;
    }
}
