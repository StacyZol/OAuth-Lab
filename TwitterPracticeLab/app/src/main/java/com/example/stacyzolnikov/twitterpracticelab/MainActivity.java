package com.example.stacyzolnikov.twitterpracticelab;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    public static final String ACCESS_TOKEN = "twitter_access_token";
    SharedPreferences mPrefs;

    private static final String TAG = "MainActivity";
    TextView mTextView;
    Button mButton;
    ListView mListView;
    ArrayList<String> mTweetList;
    EditText mEditText;

    BaseAdapter mAdapter;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       Log.i(TAG, "onCreate: test");
       mPrefs = getSharedPreferences("my_shared_prefs",MODE_PRIVATE);
       if (!mPrefs.contains(ACCESS_TOKEN)){
           Log.i(TAG, "Getting Access Token");
           doMyAuthentication();
       }
       else {
           Log.i(TAG, "Already have the access token");
        }
        mButton = (Button) findViewById(R.id.SearchButton);
        mEditText = (EditText)  findViewById(R.id.EditText);
        mListView = (ListView) findViewById(R.id.ListView);

        mTweetList = new ArrayList<>();

        mAdapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int i) {
                return mTweetList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View viewTwo = view;
                if (view ==null) {
                    LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    viewTwo = layoutInflater.inflate(R.layout.list_item, null);
                }
                    TextView tweet = (TextView) view.findViewById(R.id.Tweet);
                    tweet.setText(mTweetList.get(i));
                    return viewTwo;
                }
            };
            mListView.setAdapter(mAdapter);

        mButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                apiSearch(mEditText.getText().toString());

              // if (!mPrefs.contains(ACCESS_TOKEN)){
              //     Log.i(TAG, "Getting Access Token");
              //     doMyAuthentication();
              // }
              // else {
              //     mEditText.getText().toString();
              //     Log.i(TAG, "Already have the access token");
              // }

            }
        });



    }

    public void doMyAuthentication(){
        String consumer_key = "vshKCnSObeoEaGawoHqa85r4L";
        String consumer_secret = "tk9HTkIbQRM4EUk54iScckxbWPvh7fTAcElLmdQjNiik3Z73em";
        String encoded_key = consumer_key + ":" + consumer_secret;

        String base64EncodedString = Base64.encodeToString(encoded_key.getBytes(), Base64.NO_WRAP);
        Log.i(TAG, "Base64EcodedString: " + base64EncodedString);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("grant_type", "client_credentials").build();

        Request apiRequest = new Request.Builder().addHeader("Authorization", "Basic " + base64EncodedString).addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8").post(requestBody).url("https://api.twitter.com/oauth2/token").build();

        client.newCall(apiRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: You failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    //
                }
                else {
                    Gson gson = new Gson();
                    TwitterOAuthResponse oauthResponse = gson.fromJson(response.body().string(),TwitterOAuthResponse.class);

                    
                    Log.i(TAG, "onResponse: " + oauthResponse.access_token);
                    mPrefs.edit().putString(ACCESS_TOKEN,oauthResponse.access_token).commit();
                   Request newRequest = new Request.Builder()
                           .url("fsfg")
                           .addHeader("Authorization", "Bearer " + oauthResponse.access_token)
                           .build();
//

                }
            }

        });
    }

    public void apiSearch (String screenName) {
        OkHttpClient client = new OkHttpClient();

        Request apiRequest = new Request.Builder().url("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name="+screenName).addHeader("Authorization", "Bearer " + mPrefs.getString(ACCESS_TOKEN,null)).build();

        client.newCall(apiRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: You failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    //
                }
                else {
                    try {
                        Log.i(TAG, "onResponse: Test");

                    Gson gson = new Gson();
                        Type collectionType = new TypeToken<Collection<TweetOAuthResponse>>(){}.getType();
                    Collection<TweetOAuthResponse> mTweetList = (Collection<TweetOAuthResponse>) gson.fromJson(response.body().string(), collectionType);

                    Log.i(TAG, "onResponse: " + mTweetList);
                    }
                    catch (IllegalStateException e){
                        e.printStackTrace();
                    }

                }
            }


        });
    }







}
