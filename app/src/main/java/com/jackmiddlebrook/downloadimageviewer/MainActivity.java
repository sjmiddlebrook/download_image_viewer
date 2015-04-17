package com.jackmiddlebrook.downloadimageviewer;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.jackmiddlebrook.downloadimageviewer.Utils.downloadImage;


public class MainActivity extends ActionBarActivity {

    private final String TAG = getClass().getSimpleName();

    private EditText mUrlEditText;

    private Button mDownloadButton;

    private Uri mDefaultUrl =
            Uri.parse("http://en.wikipedia.org/wiki/Lake_Tahoe#/media/File:Emerald_Bay.jpg");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUrlEditText = (EditText) findViewById(R.id.urlEditText);
        mDownloadButton = (Button) findViewById(R.id.downloadButton);

        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri imageUrl = getUrl();
                new DownloadImageTask().execute(imageUrl);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * Get the URL to download based on user input.
     */
    protected Uri getUrl() {
        Uri url = null;

        // Get the text the user typed in the edit text (if anything).
        url = Uri.parse(mUrlEditText.getText().toString());

        // If the user didn't provide a URL then use the default.
        String uri = url.toString();
        if (uri == null || uri.equals(""))
            url = mDefaultUrl;

        // Do a sanity check to ensure the URL is valid, popping up a
        // toast if the URL is invalid.
        if (URLUtil.isValidUrl(url.toString())) {
            Toast.makeText(this,
                    "URL: " + url,
                    Toast.LENGTH_SHORT).show();
            return url;
        } else {
            Toast.makeText(this,
                    "Invalid URL",
                    Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private class DownloadImageTask extends AsyncTask<Uri, Integer, Uri> {

        @Override
        protected Uri doInBackground(Uri... uris) {
            return downloadImage(getApplicationContext(), uris[0]);
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
            Toast.makeText(getApplicationContext(), "Image file: " + uri, Toast.LENGTH_LONG).show();
        }
    }
}
