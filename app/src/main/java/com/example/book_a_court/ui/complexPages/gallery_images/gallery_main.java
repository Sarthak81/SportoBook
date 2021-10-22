package com.example.book_a_court.ui.complexPages.gallery_images;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
//import android.support.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.example.book_a_court.R;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.book_a_court.ui.complexPages.GalleryFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

public class gallery_main extends AppCompatActivity {

    // Folder path for Firebase Storage.
    String Storage_Path = "Gallery_Image_Uploads/";

    // Root Database Name for Firebase Database.
    public static final String Database_Path = "Gallery_Image_Uploads";

    // Creating button.
    Button ChooseButton, UploadButton, DisplayImageButton;

    // Creating EditText.
    EditText ImageName ;

    // Creating ImageView.
    ImageView SelectImage;

    // Creating URI.
    Uri FilePathUri;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;

    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;

    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_main);

        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();

        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        //Assign ID'S to button.
        ChooseButton = (Button)findViewById(R.id.ButtonChooseImage);
        UploadButton = (Button)findViewById(R.id.ButtonUploadImage);

        DisplayImageButton = (Button)findViewById(R.id.DisplayImagesButton);

        // Assign ID's to EditText.
        ImageName = (EditText)findViewById(R.id.ImageNameEditText);

        // Assign ID'S to image view.
        SelectImage = (ImageView)findViewById(R.id.ShowImageView);

        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(gallery_main.this);

        // Adding click listener to Choose image button.
        ChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Creating intent.
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
                CropImage.activity().start(gallery_main.this);

            }
        });


        // Adding click listener to Upload image button.
        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling method to upload selected image on Firebase storage.
                UploadImageFileToFirebaseStorage();

            }
        });


        DisplayImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(gallery_main.this, GalleryFragment.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

         {
             if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {
                 CropImage.ActivityResult result = CropImage.getActivityResult(data);
                 // Getting selected image into Bitmap.
                 assert result != null;
                 FilePathUri = result.getUri();
                 //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                 // Setting up bitmap selected image into ImageView.
                // SelectImage.setImageBitmap(bitmap);
                 Picasso.get().load(FilePathUri).into(SelectImage);
                 // After selecting image change choose button above text.
                 ChooseButton.setText("Image Selected");
             }

         }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            String TempImageName = ImageName.getText().toString().trim();
                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener< Uri >() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri profileImageUrl = uri;
                                    //Do what you want with the url
                                    Toast.makeText(gallery_main.this, "Upload done", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, profileImageUrl.toString());

                                    String ImageUploadId = databaseReference.push().getKey();
//
//                                    // Adding image upload id s child element into databaseReference.
                                 databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                                }
                                //Toast.makeText(, "Upload Done", Toast.LENGTH_LONG.show();

                                // Hiding the progressDialog after done uploading.
//                            progressDialog.dismiss();
//                            storageReference2nd.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Uri> task) {
//                                    String profileImageUrl=task.getResult().toString();
//                                   // Log.i("URL",profileImageUrl);
//                                    Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
//
//                                    @SuppressWarnings("VisibleForTests")
//                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, profileImageUrl);
//                                    String ImageUploadId = databaseReference.push().getKey();
//
//                                    // Adding image upload id s child element into databaseReference.
//                                    databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
//                                }
//                            });

                                // Showing toast message after done uploading.


                                // Getting image upload ID.


                            });
                        }
                        })

                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(gallery_main.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");

                        }
                    });
        }
        else {

            Toast.makeText(gallery_main.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }


}