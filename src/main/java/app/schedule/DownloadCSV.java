package app.schedule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class DownloadCSV {

    public void download() {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(
                    "https://spotifycharts.com/regional/global/daily/latest/download");

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("Request Url: " + request.getURI());
            System.out.println("Response Code: " + responseCode);

            InputStream is = entity.getContent();

            String filePath = "/Users/willwhitmey/MusicMarket-Back/src/main/java/app/schedule/Songs.csv";
            FileOutputStream fos = new FileOutputStream(new File(filePath));

            int inByte;
            while ((inByte = is.read()) != -1) {
                fos.write(inByte);
            }

            is.close();
            fos.close();

            client.close();
            System.out.println("File Download Completed!!!");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}