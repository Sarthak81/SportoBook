package com.example.book_a_court;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class navCom extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfigurationCom;
    CircleImageView drawerImage;
    TextView drawerName;
    FirebaseAuth auth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_com);
        Toolbar toolbar = findViewById(R.id.toolbarCom);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        DrawerLayout drawer = findViewById(R.id.drawer_layout_com);
        NavigationView navigationView = findViewById(R.id.nav_view_com);
        updateHeader();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfigurationCom = new AppBarConfiguration.Builder(
                R.id.nav_homeCom, R.id.nav_profileCom, R.id.nav_gallery,R.id.nav_manage,R.id.nav_chatComplex)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_complex);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfigurationCom);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_com, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_complex);
        return NavigationUI.navigateUp(navController, mAppBarConfigurationCom)
                || super.onSupportNavigateUp();
    }

    public void updateHeader(){
        NavigationView navigationView = findViewById(R.id.nav_view_com);
        View headerView = navigationView.getHeaderView(0);
        drawerImage = headerView.findViewById(R.id.drawerImageCom);
        drawerName = headerView.findViewById(R.id.drawerNameCom);
        DocumentReference df = fStore.collection("users").document(auth.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getString("url")!=null){
                    Picasso.get().load(documentSnapshot.getString("url")).into(drawerImage);
//                    Toast.makeText(navCom.this, ""+drawerImage + " " + documentSnapshot.getString("url"), Toast.LENGTH_SHORT).show();
                }
                drawerName.setText(documentSnapshot.getString("fName"));
            }
        });
    }
}