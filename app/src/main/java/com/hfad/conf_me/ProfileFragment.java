package com.hfad.conf_me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.conf_me.models.User;



public class ProfileFragment extends Fragment implements View.OnClickListener {
    private TextView userName;
    private final int REQUEST_PREMISSION_CAMERA = 1;
    private static final int SWIPE_MIN_DISTANCE = 130;
    private static final int SWIPE_MAX_DISTANCE = 300;
    private static final int SWIPE_MIN_VELOCITY = 200;
    RelativeLayout mainLayout;

    String currentUid;
    private Camera camera;
    private LinearLayout editProfile;

    FragmentManager myFragmentManager;
    EditProfileFragment editProfileFragment;

    @SuppressLint("ClickableViewAccessibility")
    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View rootView =
                inflater.inflate(R.layout.fragment_profile, container, false);
        camera = null;
        //CAMERA--------------
       /* cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try{
            for(String cameraID : cameraManager.getCameraIdList()){
                CameraCharacteristics chars = cameraManager.getCameraCharacteristics(cameraID);
                Integer facing = chars.get(CameraCharacteristics.LENS_FACING);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }*/

        //-----------------------------------------------
        GestureDetectorCompat lSwipeDetector = new GestureDetectorCompat(getActivity(), new MyGestureListener());
        mainLayout = (RelativeLayout) rootView.findViewById(R.id.profile_fragment_main);

        mainLayout.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return lSwipeDetector.onTouchEvent(event);
            }
        });

        Button arbutton = (Button) rootView.findViewById(R.id.arbutton);
        Button sign_out = (Button) rootView.findViewById(R.id.sign_out);
        LinearLayout editProfile = (LinearLayout) rootView.findViewById(R.id.edit_profile);

        Button button = (Button) rootView.findViewById(R.id.arbutton);
        TextView userName = (TextView) rootView.findViewById(R.id.user_name);

        FirebaseUser userId = FirebaseAuth.getInstance().getCurrentUser() ;
        currentUid = userId.getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = database.child("Users/");

        myRef.child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getName()+" "+ user.getSurname());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });


        button.setOnClickListener(this);

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditProfileFragment editProfileFragment = new EditProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editProfileFragment).addToBackStack(null).commit();
            }

        });

        return rootView;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case REQUEST_PREMISSION_CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT);
                }
        }

    }

    private void requestPermission(String permissionName, int permissionRequestCode){
        ActivityCompat.requestPermissions(getActivity(), new String[]{permissionName}, permissionRequestCode);
    }


    @Override
    public void onClick(View v) {

    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e){
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
            if(Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_DISTANCE)
                return false;
            if(e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_MIN_VELOCITY){
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    if(!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)){
                        requestPermission(Manifest.permission.CAMERA, REQUEST_PREMISSION_CAMERA);
                    }
                }
                else{
                    //Проверка на наличие камеры
                    try{
                        if(getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
                            CameraFragment cameraFragment = new CameraFragment();
                            getActivity().getSupportFragmentManager().beginTransaction().
                                    replace(R.id.fragment_container, cameraFragment).addToBackStack(null).commit();
                        }
                    }catch (Exception e){
                        //TODO Вывести предупреждение что камеры нет, такая функция недоступна
                    }
                }
            }
            return false;
        }
    }

}



