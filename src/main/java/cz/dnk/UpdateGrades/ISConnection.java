package cz.dnk.UpdateGrades;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: jirka
 * Date: 10/20/12
 * Time: 11:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ISConnection {
    private CookieStore cookieStore;
    private HttpContext localContext;
    private DefaultHttpClient httpClient;

    public static String ExtractMagicNumber(String html) {
        Pattern pattern = Pattern.compile("<INPUT TYPE=hidden NAME=\"_\" VALUE=\"(\\d+)\">", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);
        String magicNumber = null;
        if (matcher.find()) {
            magicNumber = matcher.group(1);
        }
        return magicNumber;
    }

    public static String ExtractMessage(String html) {
//        System.out.println(html);
        Pattern pattern = Pattern.compile(
                "<div class=\"((chyba)|(potvrzeni))\"  ><h3>(.*)</h3></div>",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);
        String msg = null;
        if (matcher.find()) {
            msg = matcher.group(1) + " : " + matcher.group(4);
        } else {
            msg = null;
        }
        return msg;
    }

    public ISConnection() {
        cookieStore = new BasicCookieStore();
        localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        httpClient = new DefaultHttpClient();

        // handle also 301 and 302
        // http://stackoverflow.com/questions/3658721/httpclient-4-error-302-how-to-redirect
        httpClient.setRedirectStrategy(new DefaultRedirectStrategy() {
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)  {
                boolean isRedirect=false;
                try {
                    isRedirect = super.isRedirected(request, response, context);
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                if (!isRedirect) {
                    int responseCode = response.getStatusLine().getStatusCode();
                    if (responseCode == 301 || responseCode == 302) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void authenticate(Credentials credentials) {
        HttpPost httpPost = login("https://is.muni.cz/auth/", credentials.getName(), credentials.getPass());
        performRequest(httpPost);
    }

    public String performRequest(HttpRequestBase request) {
        return performRequest(request, false);
    }
    public String performRequest(HttpRequestBase request, boolean consume) {
        try {
            if (consume) {
                HttpResponse response = httpClient.execute(request, localContext);
                int statusCode = response.getStatusLine().getStatusCode();
                if (200 == statusCode) {
                    return "";
                }
            } else {
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String response = httpClient.execute(request, responseHandler, localContext);
                return response;
            }
        } catch (HttpResponseException e) {
            System.out.println("HTTP request failed '" + e.toString() + "'.");
            System.out.println("--possibly incorrect credentials.");
            CLI.ExitWithDisclaimer();
        } catch (ClientProtocolException e) {
            System.out.println("HTTP error '" + e.toString() + "'.");
            CLI.ExitWithDisclaimer();
        } catch (IOException e) {
            System.out.println("IO error '" + e.toString() + "'.");
            CLI.ExitWithDisclaimer();
        }
        return null;
    }

    public static HttpPost login(String destination, String username, String password) {
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("destination", destination));
        formParams.add(new BasicNameValuePair("credential_0", username));
        formParams.add(new BasicNameValuePair("credential_1", password));
        formParams.add(new BasicNameValuePair("submit", "1"));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formParams, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        HttpPost httpPost = new HttpPost("https://is.muni.cz/system/login_form.pl");
        httpPost.setEntity(entity);

        return httpPost;
    }
}
