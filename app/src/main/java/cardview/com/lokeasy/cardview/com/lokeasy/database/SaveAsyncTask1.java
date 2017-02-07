package cardview.com.lokeasy.cardview.com.lokeasy.database;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class SaveAsyncTask1 extends AsyncTask<MySupport, Void, Boolean> {

    @Override
    protected Boolean doInBackground(MySupport... arg0) {
        try
        {
            MySupport support = arg0[0];

            QueryBuilder1 qb = new QueryBuilder1();

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(qb.buildSupportsSaveURL());

            StringEntity params =new StringEntity(qb.createSupport(support));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            if(response.getStatusLine().getStatusCode()<205)
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (Exception e) {
            //e.getCause();
            String val = e.getMessage();
            String val2 = val;
            return false;
        }
    }

}
