package com.example.book_a_court.ui.complexPages.gallery_images;

public class ImageUploadInfo {

    public String imageName;

    public String imageURL;


    public ImageUploadInfo() {

    }
    public ImageUploadInfo(String name, String url) {

        this.imageName = name;
        this.imageURL= url;
        //this.videoURL=v_url;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

}