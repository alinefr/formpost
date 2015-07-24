package br.com.alinefreitas.formpost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    EditText msgTextField;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);

        //make message text field object
        msgTextField = (EditText) findViewById(R.id.msgTextField);
        //make button object
        sendButton = (Button) findViewById(R.id.sendButton);
    }

    public void send(View v)
    {
        //get message from message box
        String  msg = msgTextField.getText().toString();

        //check whether the msg empty or not
        if(!msg.isEmpty()) {
            new PostData().execute(msg);
        } else {
            //display message if text field is empty
            Toast.makeText(getBaseContext(), "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }

    public class PostData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://formpost.dlapp.co/serverside-script.php");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("id", "01"));
                nameValuePairs.add(new BasicNameValuePair("message", params[0]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    String op = EntityUtils.toString(response.getEntity(), "UTF-8");//The response you get from your script
                    return op;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //reset the message text field
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            msgTextField.setText("");
            Toast.makeText(getBaseContext(), "Sent", Toast.LENGTH_SHORT).show();
        }
    }

}
