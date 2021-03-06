package appewtc.masterung.myvideo;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ListVideo extends ListActivity{

    private SimpleCursorAdapter objSimpleCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_list_video);

        //Sync JSON to Service
        syncJSONtoService();

        //Create Listview
        createListview();

    }   // onCreate

    private void createListview() {

        ServiceTABLE objServiceTABLE = new ServiceTABLE(this);
        Cursor listStory = objServiceTABLE.readAllData();
        String[] from = new String[]{ServiceTABLE.COLUMN_STRORY};
        int[] target = new int[]{R.id.txtListStory};
        objSimpleCursor = new SimpleCursorAdapter(this, R.layout.activity_list_video, listStory, from, target);
        setListAdapter(objSimpleCursor);

    }   // createListview


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Cursor objCursor = (Cursor) l.getItemAtPosition(position);
        String strStory = objCursor.getString(objCursor.getColumnIndex(ServiceTABLE.COLUMN_STRORY));
        String strImageURL = objCursor.getString(objCursor.getColumnIndex(ServiceTABLE.COLUMN_IMAGE));
        String strVideoURL = objCursor.getString(objCursor.getColumnIndex(ServiceTABLE.COLUMN_VIDEO));

       // Toast.makeText(ListVideo.this, "You Click " + strStory, Toast.LENGTH_SHORT).show();

        //Intent to ResultActivity
        Intent objIntent = new Intent(ListVideo.this, ResultActivity.class);
        objIntent.putExtra("Story", strStory);
        objIntent.putExtra("ImageURL", strImageURL);
        objIntent.putExtra("VideoURL", strVideoURL);
        startActivity(objIntent);

    }   // onListItemClick

    private void syncJSONtoService() {

        //Setup Policy
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(myPolicy);
        }

        //Delete Alldata
        SQLiteDatabase objSQLite = openOrCreateDatabase("video.db", MODE_PRIVATE, null);
        objSQLite.delete("serviceTABLE", null, null);


        //Create InputStream
        InputStream objInputStream = null;
        String strJSON = "";

        try {

            HttpClient objHttpClient = new DefaultHttpClient();
            HttpPost objHttpPost = new HttpPost("http://swiftcodingthai.com/jan/php_get_data_service.php");
            HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
            HttpEntity objHttpEntity = objHttpResponse.getEntity();
            objInputStream = objHttpEntity.getContent();

        } catch (Exception e) {
            Log.d("video", "Error from InputStream ==> " + e.toString());
        }



        //Create strJSON
        try {

            BufferedReader objButteredReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
            StringBuilder objStringBuilder = new StringBuilder();
            String strLine = null;

            while ((strLine = objButteredReader.readLine()) != null ) {
                objStringBuilder.append(strLine);
            }   // while

            objInputStream.close();
            strJSON = objStringBuilder.toString();

        } catch (Exception e) {
            Log.d("video", "Error strJSON ==> " + e.toString());
        }


        //Update to SQLite
        try {

            final JSONArray objJSONArray = new JSONArray(strJSON);

            for (int i = 0; i < objJSONArray.length(); i++) {

                JSONObject objJSONObject = objJSONArray.getJSONObject(i);

                String strStory = objJSONObject.getString("Story");
                String strImage = objJSONObject.getString("Image");
                String strVideo = objJSONObject.getString("Video");

                ServiceTABLE objServiceTABLE = new ServiceTABLE(this);
                long addValue = objServiceTABLE.addValueToServie(ListVideo.this, strStory, strImage, strVideo);


            }   // for


        } catch (Exception e) {
            Log.d("video", "Error Update ==> " + e.toString());
        }




    }   // syncJSONtoService


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}   // Main Class
