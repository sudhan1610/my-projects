package com.example.rxjavaretrofitroomdb;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rxjavaretrofitroomdb.adapter.RetrofitAlbumAdapter;
import com.example.rxjavaretrofitroomdb.data.RetrofitAlbum;
import com.example.rxjavaretrofitroomdb.model.Post;
import com.example.rxjavaretrofitroomdb.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {

    private static final String TAG = RetrofitActivity.class.getSimpleName();
    Button loadAlbumBtn;
    TextView responseText;
    RecyclerView recyclerView;
    RetrofitAlbumAdapter albumAdapter;
    Retrofit retrofit = null, retrofitSingPost=null;
    AlbumApis alubumApi;
    String sessionToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);


        Bundle bundle;
        Log.d(TAG, "onCreate: firebase init");
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        CollectionReference restaurants = mFirestore.collection("restaurants");
        for (int i=0; i<1; i++){
            Restaurant restaurant = new Restaurant();
            restaurants.add(restaurant);
            Log.d(TAG,"added restaurant : "+i+" "+restaurant.getName());
            bundle = new Bundle();
            bundle.putString("name",restaurant.getName());
            bundle.putString("place",restaurant.getPlace());
            firebaseAnalytics.logEvent("Add",bundle);
        }

        Query mQuery = mFirestore.collection("restaurants");
        mQuery.get().addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG, "onComplete: query");
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                for (DocumentSnapshot snapShot : documents){
                    Log.d(TAG, "initFirestore: "+snapShot.toString());

                }
                Bundle bundle = new Bundle();
                bundle.putInt("size",documents.size());
                firebaseAnalytics.logEvent("Fetched",bundle);
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Query");
            }
        }).addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "onSuccess: query");
            }
        });




        /*Intent intent = new Intent(RetrofitActivity.this,MainActivity.class);
        startActivity(intent);*/

        /*loadAlbumBtn = (Button)findViewById(R.id.load_album);
        responseText = (TextView)findViewById(R.id.response_text);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        albumAdapter = new RetrofitAlbumAdapter(new ArrayList<>());
        recyclerView.setAdapter(albumAdapter);

        loadAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadAlbum();
                //sendPost("xyz","abc");
            }
        });

        ((Button)findViewById(R.id.sample_retro_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sampleRetroPost("abc","xyz");
            }
        });

        ((Button)findViewById(R.id.sample_http_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sampleHttpPost();
            }
        });

        String username = "abc4";
        String password = "abc";
        ((Button)findViewById(R.id.register_retro_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserRetrofit(username,password);
            }
        });

        ((Button)findViewById(R.id.login_retro_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserRetrofit(username,password);
            }
        });

        ((Button)findViewById(R.id.upload_retro_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDataRetro();
            }
        });

        ((Button)findViewById(R.id.register_http_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserHttp();
            }
        });

        ((Button)findViewById(R.id.register_okhttp_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserOkHttp();
            }
        });

        ((Button)findViewById(R.id.register_unirest_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserUnirest();
            }
        });*/

    }

    private void loadAlbum(){
        Call<List<RetrofitAlbum>> call = alubumApi.getAllAlbums();
        call.enqueue(new Callback<List<RetrofitAlbum>>() {
            @Override
            public void onResponse(Call<List<RetrofitAlbum>> call, Response<List<RetrofitAlbum>> response) {
                Log.d(TAG,"response size: "+response.body().size());
                albumAdapter.loadAlbumList(response.body());
            }

            @Override
            public void onFailure(Call<List<RetrofitAlbum>> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Failed to fetch data",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void registerUserRetrofit(String username,String password){
        Log.d(TAG,"registerUserRetrofit username: "+username+" password: "+password);
        AlbumApis apis = getRetrofitInstanceForSingPost().create(AlbumApis.class);
        //Call<String> registerCall = apis.registerUser("application/x-www-form-urlencoded",new User(username,password));
        Call<String> registerCall = apis.registerUser(username,password);

        registerCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG,"registerCall onResponse body:"+response.body() +" toString:"+response.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = "Code:"+response.code()+" message:"+response.message()+"\n"+response.body();
                        responseText.setText(text);
                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG,"onFailure "+call+" : "+t);
            }
        });


        IntentFilter btPairedFilter = new IntentFilter();
        btPairedFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        btPairedFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        btPairedFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        btPairedFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        registerReceiver(BTPairedReceiver,btPairedFilter);

    }

    private final BroadcastReceiver BTPairedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch(action){
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    Log.d(TAG, "onReceive: ACTION_ACL_CONNECTED");
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                    Log.d(TAG, "onReceive: ACTION_ACL_DISCONNECT_REQUESTED");
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    Log.d(TAG, "onReceive: ACTION_ACL_DISCONNECTED");
                    break;
                case BluetoothDevice.ACTION_PAIRING_REQUEST:
                    Log.d(TAG, "onReceive: ACTION_PAIRING_REQUEST");
                    break;
            }
        }
    };

    private void loginUserRetrofit(String username,String password){
        Log.d(TAG,"loginUserRetrofit username: "+username+" password: "+password);
        AlbumApis apis = getRetrofitInstanceForSingPost().create(AlbumApis.class);
        Call<String> loginCall = apis.loginUser(username,password);
        loginCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG,"loginCall onResponse body:"+response.body()+" toString: "+response.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = "\nCode:"+response.code()+" message:"+response.message()+"\n\nbody: "+response.body();
                        responseText.setText(text);
                        sessionToken = response.body();
                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG,"call1 onFailure "+call+" : "+t);
            }
        });
    }

    private void uploadDataRetro(){
        String mailId = "xyz@gmail.com";
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        Log.d(TAG,"path : "+path);
        String imageName = "download.jpg";
        String imagePath = path+"/"+imageName;
        File file = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image",file.getName(),requestBody);

        AlbumApis apis = getRetrofitInstanceForSingPost().create(AlbumApis.class);
        Call<Void> call = apis.uploadData(sessionToken,mailId,image);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG,"uploadDataRetro onResponse body:"+response.body()+" toString: "+response.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = "\nCode:"+response.code()+" message:"+response.message()+"\n\nbody: "+response.body();
                        responseText.setText(text);
                    }
                });
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG,"uploadDataRetro onFailure "+call+" : "+t);
            }
        });
    }



    private void registerUserHttp(){
        HashMap<String,String> params = new LinkedHashMap<>();
        params.put("username","abc");
        params.put("password","abc");
        try {
            String dataString = getDataString(params);
            Log.v(TAG,"registerUserHttp dataString: "+dataString);
            new PostTask("http://192.168.43.14:4000/login/",dataString).execute();
        }catch (Exception e){
            Log.e(TAG,"registerUserHttp Exception : "+e);
        }
    }

    private void registerUserOkHttp(){
        new OkHttpTask().execute();
    }

    private void registerUserUnirest(){
        new UnirestTask().execute();
    }

    class OkHttpTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "username=abc&password=abc");
            Request request = new Request.Builder()
                    .url("http://192.168.43.14:4000/login")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();
                Log.v(TAG,"registerUserOkHttp responsecode: "+response.code()+" "+response.toString());
            }catch (Exception e){
                Log.e(TAG,"registerUserOkHttp Exception : "+e);
            }
            return null;
        }
    }

    class UnirestTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Unirest.setTimeouts(0, 0);
                HttpResponse<String> response = Unirest.post("http://192.168.43.14:4000/login")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .field("username", "abc")
                        .field("password", "abc")
                        .asString();
                Log.d(TAG,"registerUserUnirest code: "+response.getCode()+" "+response.getBody());
            }catch (Exception e){
                Log.e(TAG,"registerUserUnirest Exception : "+e);
            }
            return null;
        }
    }

    class PostTask extends AsyncTask<Void,Void,Void>{

        String url;
        String postParameters;
        public PostTask(String url, String param){
            this.url = url;
            this.postParameters = param;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{

                URL urlToRequest = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                //
                urlConnection.setInstanceFollowRedirects(false);

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                //
                urlConnection.setRequestProperty("charset", "utf-8");
                urlConnection.setRequestProperty("Content-Length", Integer.toString(postParameters.getBytes(StandardCharsets.UTF_8).length ));
                urlConnection.setUseCaches(false);
                OutputStream outputStream = urlConnection.getOutputStream();
                try(DataOutputStream wr = new DataOutputStream(outputStream)) {
                    wr.write( postParameters.getBytes(StandardCharsets.UTF_8) );
                    wr.flush();
                    wr.close();
                    outputStream.close();
                }




                /*urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(postParameters);
                out.close();*/

                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    // throw some exception
                    Log.e(TAG,"sendPostRequest statusCode: "+statusCode);
                }else{
                    Log.d(TAG,"sendPostRequest success");
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String response;
                while ((response=br.readLine()) != null){
                    Log.d(TAG,"response from server: "+response);
                }

            }catch (Exception e){
                Log.e(TAG,"sendPostRequest Exception : "+e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    public void sampleRetroPost(String title, String body) {
        AlbumApis apis = getRetrofitInstance().create(AlbumApis.class);
        apis.savePost(title, body, 1).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if(response.isSuccessful()) {
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    private void sampleHttpPost(){
        HashMap<String,String> params = new LinkedHashMap<>();
        params.put("title","abc");
        params.put("body","xyz");
        params.put("userId","1");
        try {
            String dataString = getDataString(params);
            Log.v(TAG,"sampleHttpPost dataString: "+dataString);
            new PostTask("http://jsonplaceholder.typicode.com/posts/", dataString).execute();
        }catch (Exception e){
            Log.e(TAG,"sampleHttpPost Exception : "+e);
        }
    }



    private Retrofit getRetrofitInstance(){
        String BASE_URL = "http://jsonplaceholder.typicode.com/";
        if (retrofit == null){
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private Retrofit getRetrofitInstanceForSingPost(){
        String BASE_URL = "http://192.168.43.14:4000/";
        if (retrofitSingPost == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofitSingPost = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitSingPost;
    }

    private String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
