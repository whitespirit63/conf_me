package com.hfad.conf_me;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class ProfileActivity extends AppCompatActivity {

    static String NAME;
/*
    private TextView userName;
    private Button sign_out;
    LinearLayout root;
    private LinearLayout editProfile;
String currentUid;
private Button arbutton;


*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        /*
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle("Profile");
*/

        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //bnv.setOnNavigationItemSelectedListener(getBottomNavigationListener());
        bnv.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
        new ProfileFragment()).commit();
/*
        userName = (TextView) findViewById(R.id.user_name);
        arbutton = (Button) findViewById(R.id.arbutton);
        sign_out = (Button) findViewById(R.id.sign_out);
        editProfile = (LinearLayout) findViewById(R.id.edit_profile);

        FirebaseUser userId = FirebaseAuth.getInstance().getCurrentUser() ;
        currentUid = userId.getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = database.child("Users/");



        myRef.child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getName());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });


        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                finish();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));
                finish();
            }
        });
*/
/*
        User user = new User("kek", "kek@gmail.com", "111111", "6243683");
        reference.push().setValue(user);

*/

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
    new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    selectedFragment = new ProfileFragment();

                    break;
                case R.id.navigation_dialogs:
                    selectedFragment = new DialogsFragment();

                    break;
                case R.id.navigation_search:
                    selectedFragment = new SearchFragment();

                    break;
                case R.id.navigation_ar:
                    selectedFragment = new ARFragment();

                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };


/*
    @NonNull
    private BottomNavigationView.OnNavigationItemSelectedListener getBottomNavigationListener() {
    return (item) -> {
        switch (item.getItemId()) {
            case R.id.navigation_profile:
                profile.setVisibility(View.VISIBLE);
                dialogs.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                ar.setVisibility(View.GONE);
                break;
            case R.id.navigation_dialogs:
                profile.setVisibility(View.GONE);
                dialogs.setVisibility(View.VISIBLE);
                search.setVisibility(View.GONE);
                ar.setVisibility(View.GONE);
                break;
            case R.id.navigation_search:
                profile.setVisibility(View.GONE);
                dialogs.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                ar.setVisibility(View.GONE);
                break;
            case R.id.navigation_ar:
                profile.setVisibility(View.GONE);
                dialogs.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                ar.setVisibility(View.VISIBLE);
                break;
        }
        return true;
    };

    }
*/

}