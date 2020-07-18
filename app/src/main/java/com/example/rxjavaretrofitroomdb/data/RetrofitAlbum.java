package com.example.rxjavaretrofitroomdb.data;

import com.google.gson.annotations.SerializedName;

public class RetrofitAlbum {

    @SerializedName("albumId")
    private int albumId;

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    public String title;

    @SerializedName("url")
    private String url;

    @SerializedName("thumbnailUrl")
    private String thumbnailUrl;

    public RetrofitAlbum(int albumId, int id, String title, String url, String thumbnailUrl){
        this.albumId = albumId;
        this.id = id;
        this.title = title;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }
}
