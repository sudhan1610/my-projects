package com.example.rxjavaretrofitroomdb;

import com.example.rxjavaretrofitroomdb.data.RetrofitAlbum;
import com.example.rxjavaretrofitroomdb.model.Post;
import com.example.rxjavaretrofitroomdb.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AlbumApis {

    @GET("/photos")
    Call<List<RetrofitAlbum>> getAllAlbums();

    @POST("register")
    Call<String> registerUser(@Header("Content-Range") String contentRange,
                              @Body User user);

    @FormUrlEncoded
    @POST("register")
    Call<String> registerUser(@Field("username") String username,
                              @Field("password") String password);

    @FormUrlEncoded
    @POST("login")
    Call<String> loginUser(@Field("username") String username,
                           @Field("password") String password);

    @POST("upload")
    @Multipart
    Call<Void> uploadData(@Header("x-access-token") String token,
                          @Header("x-mailid") String mailId,
                          @Part MultipartBody.Part image);

    @POST("posts")
    @FormUrlEncoded
    Call<Post> savePost(@Field("title") String title,
                        @Field("body") String body,
                        @Field("userId") long userId);
}
