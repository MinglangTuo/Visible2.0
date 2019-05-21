package com.example.visible2.fragment;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.visible2.ChangePass;

import com.example.visible2.MainActivity;
import com.example.visible2.R;

import com.example.visible2.json_obj.user_info;
import com.example.visible2.myapp.BottomDialog;
import com.example.visible2.myapp.CircleImageView;
import com.google.gson.Gson;
import com.shehuan.niv.NiceImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements BottomDialog.OnBottomMenuItemClickListener {

    public static final int TAKE_PHOTO = 1;
    public static final int SELECT_PHOTO = 2;
    private Uri imageUri;

    private TextView nickname;


    NiceImageView avatar;
    private LinearLayout changepassword;

    private BottomDialog bottomDialog;
    private View rootView;
    private TextView creditsView;
    private TextView user_nickname;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String host = "http://kyrie.top:8888/";
    private final String host_register = host + "api/user";
    private final String host_login = "http://kyrie.top:8888/api/user/login";
    private final String host_getTasks = "http://kyrie.top:8888/api/task/";
    private final String host_getTasks_test = "http://kyrie.top:8888/api/task/5ccd5e5593011e5364ba23f4";


    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_me, container, false);
        creditsView = rootView.findViewById(R.id.user_credits);
        user_nickname = rootView.findViewById(R.id.nickname);
        creditsView.setText(String.valueOf(TaskFragment.current_credits));
        user_nickname.setText(TaskFragment.current_nickname);
        bottomDialog = new BottomDialog(this.getActivity(), R.layout.dialog_bottom_layout, new int[]{R.id.pick_photo_album, R.id.pick_photo_camera, R.id.pick_photo_cancel});
        bottomDialog.setOnBottomMenuItemClickListener(this);

        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            creditsView.setText(String.valueOf(TaskFragment.current_credits));
        }else{
            creditsView.setText(String.valueOf(TaskFragment.current_credits));
        }
    }

    public void onStart() {
        super.onStart();
        initView();
    }

    public void initView() {

        this.avatar = getActivity().findViewById(R.id.Avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.show();
            }
        });



        this.changepassword = getActivity().findViewById(R.id.changepassword);
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ChangePass.class);
                startActivity(intent);
            }
        });

    }





    public void onBottomMenuItemClick(BottomDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.pick_photo_album:
                //popupWindow.dismiss();
                select_photo();
                //检查是否有权限
                break;
            case R.id.pick_photo_camera:
                take_photo();
                break;
        }
    }


    public void take_photo() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            //创建File对象，用于存储拍照后的图片
            File outputImage = new File(getActivity().getExternalCacheDir(), "output_image.jpg");
            try {
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= 24) {
                imageUri = FileProvider.getUriForFile(this.getActivity(), "com.llk.study.activity.PhotoActivity", outputImage);
            } else {
                imageUri = Uri.fromFile(outputImage);
            }
            //启动相机程序
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
        } else {

            Toast.makeText(getActivity(), "没有储存卡", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 从相册中获取图片
     */
    public void select_photo() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }

    /**
     * 打开相册的方法
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == getActivity().RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        avatar.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case SELECT_PHOTO:
                if (resultCode == getActivity().RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImgeOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 4.4以下系统处理图片的方法
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 4.4及以上系统处理图片的方法
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImgeOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //解析出数字格式的id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //如果是content类型的uri，则使用普通方式处理
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                //如果是file类型的uri，直接获取图片路径即可
                imagePath = uri.getPath();
            }
            //根据图片路径显示图片
            displayImage(imagePath);
        }
    }

    /**
     * 根据图片路径显示图片的方法
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            avatar.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 通过uri和selection来获取真实的图片路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {

        super.onPause();
        //new Thread(login).start();
    }

    public Runnable login = new Runnable() {
        @Override
        public void run() {
            String response_string = "";
            JSONObject login = new JSONObject();
            try {
                login.put("nickname", "test1");
                login.put("password", "abcd1");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            OkHttpClient okHttpClient = new OkHttpClient();
            String json = login.toString();
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(host_login)
                    .post(requestBody)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    response_string = response.body().string();
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", response_string);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Gson gson = new Gson();
            user_info current_user = gson.fromJson(val, user_info.class);
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            intent.putExtra("nickname", current_user.get_user_info().nickname);
            intent.putExtra("credits", current_user.get_user_info().credits);
            intent.putExtra("id", current_user.get_user_info()._id);
            startActivity(intent);
        }
    };
}