package cz.dnk.UpdateGrades;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jirka
 * Date: 11/24/12
 * Time: 8:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class GradesAction implements IAction {

    private String fakulta;
    private String obdobi;
    private String predmet;
    private String nbloku;
    private String exppar;

    @Override
    public void perform(ISConnection connection, Map<String, String> params) {
        fakulta = params.get("fakulta");
        obdobi = params.get("obdobi");
        predmet = params.get("predmet");
        // URL encode a vstupni kodovani!
        nbloku = params.get("nbloku");

        // no idea where this comes from
        exppar = "a";

        if (fakulta == null || obdobi == null || predmet == null || nbloku == null || exppar == null) {
            System.out.println("Some of the required Additional parameters is not specified.\nUse --parameters together with the --action to see the list.");
            return;
        }

        if (params.get("file") == null) {
            System.out.println("The -Dfile Additional parameter is not specified.");
            return;
        }

        if (params.get("slid") == null) {
            System.out.println("The -Dslid Additional parameter is not specified.");
            return;
        }

        if (params.get("slho") == null) {
            System.out.println("The -Dslho Additional parameter is not specified.");
            return;
        }

        if (params.get("nerozlisovat_bloky") == null) {
            System.out.println("The -Dnerozlisovat_bloky Additional parameter is not specified.");
            return;
        }

        if (params.get("ostry") == null) {
            System.out.println("The -Dostry Additional parameter is not specified.");
            return;
        }

        File file = new File(params.get("file"));

        String magicNumber;

        HttpGet openForm = OpenForm();
        String form = connection.performRequest(openForm);
        magicNumber = ISConnection.ExtractMagicNumber(form);
        System.out.println(magicNumber);

        HttpPost mySelectBlock = selectBlock(magicNumber);
        String block = connection.performRequest(mySelectBlock);
        magicNumber = ISConnection.ExtractMagicNumber(block);
        System.out.println(magicNumber);
//        System.out.println(block);

        HttpPost request = SendGradesFile(file,
                params.get("slid"),
                params.get("slho"),
                params.get("nerozlisovat_bloky"),
                params.get("ostry"),
                magicNumber);
        String status = connection.performRequest(request);
        System.out.println(status);
        String msg = ISConnection.ExtractMessage(status);
        System.out.println(msg);
    }

    @Override
    public void printHelp() {
        System.out.println("-Dfakulta=\"\" : use 1456 for ESF");
        System.out.println("-Dobdobi=\"\" : use 5785 for fall2012, 5786 for spring2013");
        System.out.println("-Dpredmet=\"\" : e.g, '705093'");
        System.out.println("-Dnbloku=\"\" : e.g. 'vstupn%C3%AD%20test'");

        System.out.println("-Dfile=\"\" : path to csv file with grades to upload");
        System.out.println("-Dslid=\"\" : pořadí sloupce s identifikátorem studia (čísl. od 1)");
        System.out.println("-Dslho=\"\" : pořadí sloupce nebo sloupců s obsahem bloku (více hodnot oddělujte mezerou");
        System.out.println("-Dnerozlisovat_bloky=\"\" : Ignorovat změnu z původně exportovaného bloku na blok importovaný");
        System.out.println("-Dostry=\"\" : 'n' or 'a'; n -- import pouze na zkoušku, pro kontrolu chyb, a -- import naostro");

        System.out.println("When in doubts what values to use for -Dpredmet and -Dnbloku, visit the uploading page in web browser and use the value in address bar=\"\" : use 1456 for ESF");
    }

    public HttpGet OpenForm() {
        URIBuilder builder = new URIBuilder();
        builder
                .setScheme("https")
                .setHost("is.muni.cz")
                .setPath("/auth/ucitel/blok_import.pl")
                .setParameter("fakulta", fakulta)
                .setParameter("obdobi", obdobi)
                .setParameter("predmet", predmet)
                .setParameter("nbloku", nbloku);
                //.setParameter("submit", "vyber_bloku");
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpGet httpGet = new HttpGet(uri);
        return httpGet;
    }

    public HttpPost selectBlock(String magicNumber) {
        URIBuilder builder = new URIBuilder();
        builder
                .setScheme("https")
                .setHost("is.muni.cz")
                .setPath("/auth/ucitel/blok_import.pl")
                .setParameter("fakulta", fakulta)
                .setParameter("obdobi", obdobi)
                .setParameter("predmet", predmet)
                .setParameter("nbloku", nbloku);
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpPost httpPost = new HttpPost(uri);

        MultipartEntity multipartEntity = new MultipartEntity();

        String decodednbloku = null;
        try {
            decodednbloku = URLDecoder.decode(nbloku, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            System.exit(1);
        }
        System.out.println(decodednbloku);

        try {
            multipartEntity.addPart("fakulta", new StringBody(fakulta));
            multipartEntity.addPart("obdobi", new StringBody(obdobi));
            multipartEntity.addPart("predmet", new StringBody(predmet));
            multipartEntity.addPart("lang", new StringBody("en"));
            multipartEntity.addPart("_", new StringBody(magicNumber));
            multipartEntity.addPart("nbloku", new StringBody(decodednbloku, "text/plain",
                    Charset.forName("UTF-8")));
            multipartEntity.addPart("nblokuall", new StringBody(decodednbloku, "text/plain",
                    Charset.forName("UTF-8")));
            multipartEntity.addPart("vyber_bloku", new StringBody("Go to the notebook selected"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpPost.setEntity(multipartEntity);
        return httpPost;
    }

    /**
     * @param file vyberte soubor ve svém počítači
     * @param slid pořadí sloupce s identifikátorem studia (čísl. od 1)
     * @param slho pořadí sloupce nebo sloupců s obsahem bloku (více hodnot oddělujte mezerou)
     * @param nerozlisovat_bloky Ignorovat změnu z původně exportovaného bloku na blok importovaný
     * @param ostry n -- import pouze na zkoušku, pro kontrolu chyb / a -- import naostro
     * @return HttpPost
     */
    public HttpPost SendGradesFile(File file, String slid, String slho, String nerozlisovat_bloky, String ostry, String magicNumber) {
        URIBuilder builder = new URIBuilder();
        builder
                .setScheme("https")
                .setHost("is.muni.cz")
                .setPath("/auth/ucitel/blok_import.pl")
                .setParameter("fakulta", fakulta)
                .setParameter("obdobi", obdobi)
                .setParameter("predmet", predmet)
                .setParameter("nbloku", nbloku);
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpPost httpPost = new HttpPost(uri);

        MultipartEntity multipartEntity = new MultipartEntity();

        String decodednbloku = null;
        try {
            decodednbloku = URLDecoder.decode(nbloku, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            System.exit(1);
        }

        try {
            multipartEntity.addPart("fakulta", new StringBody(fakulta));
            multipartEntity.addPart("obdobi", new StringBody(obdobi));
            multipartEntity.addPart("predmet", new StringBody(predmet));
            multipartEntity.addPart("lang", new StringBody("en"));
            multipartEntity.addPart("_", new StringBody(magicNumber));
            multipartEntity.addPart("nbloku", new StringBody(decodednbloku, "text/plain",
                    Charset.forName("UTF-8")));
            multipartEntity.addPart("exppar", new StringBody(exppar));
            multipartEntity.addPart("slid", new StringBody(slid));
            multipartEntity.addPart("slho", new StringBody(slho));
            //multipartEntity.addPart("nerozlisovat_bloky", new StringBody(nerozlisovat_bloky));
            multipartEntity.addPart("ostry", new StringBody(ostry));
            multipartEntity.addPart("soubor", new FileBody(file));
            multipartEntity.addPart("nacti", new StringBody("Import"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpPost.setEntity(multipartEntity);
        return httpPost;
    }

    String getMagicNumber(){
        return "";
    }
}
