package com.hfad.conf_me;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import retrofit2.Call;
import android.util.Base64;

import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;


import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hfad.conf_me.Network.NetworkService;
import com.hfad.conf_me.Network.PhotoUploadAPI;
import com.hfad.conf_me.Network.Result;
import com.hfad.conf_me.models.User;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CameraXFragment extends Fragment {

    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private long countOfFrames;
    private Executor executor = Executors.newSingleThreadExecutor();
    PreviewView mPreviewView;
    View rootView;
    private boolean opened = false;
    //--------------
    private HashSet<User> users = new HashSet<User>();
    private DatabaseReference dataBase;
    private DatabaseReference usersDB;
    private FirebaseUser currentUser;
    private ListView listViewUsers;
    UserListAdapter adapter;
    HashSet<User> usersInCamera;
    Retrofit retrofit;
    PhotoUploadAPI photoUploadAPI;
    Call call;
    final Handler handlerTimer = new Handler();
    //------------------
    private int prev;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        prev = 0;
        countOfFrames = 0;
        usersInCamera = new HashSet<User>();
        rootView =
                inflater.inflate(R.layout.camera_ar_layout, container, false);
        mPreviewView = (PreviewView) rootView.findViewById(R.id.camera);

        dataBase = FirebaseDatabase.getInstance().getReference("Users/");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        retrofit = NetworkService.getRetrofit(getActivity());
        photoUploadAPI = retrofit.create(PhotoUploadAPI.class);

        dataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {
                    User user = singleSnapshot.getValue(User.class);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(allPermissionsGranted()){
            startCamera();
        }
        else{
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        ImageButton btn = (ImageButton) rootView.findViewById(R.id.btn);
        RelativeLayout list_of_users = (RelativeLayout) rootView.findViewById(R.id.list_of_users);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) list_of_users.getLayoutParams();
                if (opened == false){
                    params.height = 1500;
                    opened = true;
                    btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
                else{
                    params.height = 700;
                    opened = false;
                    btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                }

                list_of_users.setLayoutParams(params);


            }
        });


        return rootView;
    }

    private void startCamera(){
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(getActivity()));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(480, 640))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
        imageAnalysis.setAnalyzer(executor, new ImageAnalysis.Analyzer() {
            @SuppressLint("UnsafeExperimentalUsageError")
            @Override
            public void analyze(@NonNull ImageProxy image) {

                int rotationDegrees = image.getImageInfo().getRotationDegrees();
                byte[] byteImage = imageToByteArray(image.getImage());

                String encodedImage = Base64.encodeToString(byteImage, Base64.DEFAULT);
                if(countOfFrames % 10 == 0) {
                    call = photoUploadAPI.uploadImage(encodedImage);

                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            Log.e("onResponse: ", "TRUE");
                            Object body = response.body();
                            if (body != null) {
                                String foundList = body.toString();
                                String str[] = foundList.replaceAll("\\[", "").replaceAll("]", "")
                                        .replaceAll("\"", "").split(",");
                                List<String> ids = new ArrayList<String>(Arrays.asList(str));
                                int count = 0;
                                int maxCount = ids.size();
                                if (ids.size() != 0 && users.size() != 0 && !foundList.equals("[]")) {
                                    for (String id : ids) {
                                        for (User current : users) {
                                            if (count == maxCount - 1) {
                                                if (current.user_id.equals(id.substring(0, id.length() - 1))) {
                                                    usersInCamera.add(current);
                                                    break;
                                                }
                                            } else {
                                                if (current.user_id.equals(id)) {
                                                    usersInCamera.add(current);
                                                    break;
                                                }
                                            }
                                            count++;
                                        }
                                    }
                                    if (usersInCamera.size() != 0 && prev != usersInCamera.size()) {
                                        listViewUsers = (ListView) rootView.findViewById(R.id.camera_user_list);
                                        List<User> tmpUsers = new ArrayList<User>(usersInCamera);
                                        adapter = new UserListAdapter(getActivity(), tmpUsers);
                                        listViewUsers.setAdapter(adapter);
                                        prev = usersInCamera.size();
                                        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                // получаем пользователя, которого выбрали в списке
                                                User selectedUser = (User) parent.getItemAtPosition(position);
                                                UserProfileFragment userProfile = new UserProfileFragment();
                                                Bundle bundle = new Bundle();
                                                //Здесь заполняем bundle. решил не передавать класс, потому что долго
                                                bundle.putString("name", selectedUser.name);
                                                bundle.putString("surname", selectedUser.surname);
                                                bundle.putString("pass", selectedUser.pass);
                                                bundle.putString("phone", selectedUser.phone);
                                                bundle.putString("email", selectedUser.email);
                                                bundle.putString("description", selectedUser.description);
                                                bundle.putString("user_id", selectedUser.user_id);
                                                bundle.putString("tag1", selectedUser.tag1);
                                                bundle.putString("tag2", selectedUser.tag2);
                                                bundle.putString("tag3", selectedUser.tag3);
                                                bundle.putString("nickname", selectedUser.nickname);

                                                //----------------------------------
                                                userProfile.setArguments(bundle);
                                                getFragmentManager().beginTransaction().replace(R.id.fragment_container, userProfile).
                                                        commit();
                                            }
                                        };
                                        listViewUsers.setOnItemClickListener(itemListener);
                                    }
                                }

                            } else {
                                Log.e("CAMERA_X_FRAGMENT", "SO BAD");
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.e("CAMERA_X_FRAGMENT", "Cannot connect to the server");
                        }
                    });
                }
                countOfFrames++;
                image.close();
            }
        });

        ImageCapture.Builder builder = new ImageCapture.Builder();

        //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

        // Query if extension is available (optional).
        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis);

        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider(camera.getCameraInfo()));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                startCamera();
            } else{
                Toast.makeText(getActivity(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }
    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }


    private byte[] imageToByteArray(Image image) {
        byte[] data = null;
        if (image.getFormat() == ImageFormat.JPEG) {
            Image.Plane[] planes = image.getPlanes();
            ByteBuffer buffer = planes[0].getBuffer();
            data = new byte[buffer.capacity()];
            buffer.get(data);
            return data;
        } else if (image.getFormat() == ImageFormat.YUV_420_888) {
            data = NV21toJPEG(
                    YUV_420_888toNV21(image),
                    image.getWidth(), image.getHeight());
        }
        return data;
    }

    private byte[] YUV_420_888toNV21(Image image) {
        byte[] nv21;
        ByteBuffer yBuffer = image.getPlanes()[0].getBuffer();
        ByteBuffer uBuffer = image.getPlanes()[1].getBuffer();
        ByteBuffer vBuffer = image.getPlanes()[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        nv21 = new byte[ySize + uSize + vSize];

        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        return nv21;
    }

    private byte[] NV21toJPEG(byte[] nv21, int width, int height) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YuvImage yuv = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
        yuv.compressToJpeg(new Rect(0, 0, width, height), 100, out);
        return out.toByteArray();
    }
}
