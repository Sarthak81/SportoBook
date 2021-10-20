package com.example.book_a_court.ui.complexPages.gallery_video;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.widget.Toast;

public class videoUploadInfo {


    public String videoURL;


    public videoUploadInfo() {

    }

    public videoUploadInfo(String URL) {

        this.videoURL= URL;
        //this.videoURL=v_url;"
    }

    public String getVideoURL(){return videoURL;}

//    public void setVideoURL(String videourl){
//        videoURL=videourl;
//    }
}