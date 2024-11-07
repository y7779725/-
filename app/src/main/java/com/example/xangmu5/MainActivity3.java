package com.example.xangmu5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity3 extends AppCompatActivity {
    private EditText ed_titel,ed_content;
    private Button b_camera,b_photo,b_save;
    private ImageView imageView;
    private String tmp_path="null";
    private sql sql;
    private SQLiteDatabase db;

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        sql = new sql(MainActivity3.this);
        db = sql.getWritableDatabase(); // 获取可写数据库

        ed_titel = findViewById(R.id.editTextTextPersonName2);
        ed_content = findViewById(R.id.editTextTextPersonName3);
        imageView = findViewById(R.id.imageView);
        b_camera=findViewById(R.id.button3);
        b_photo = findViewById(R.id.button4);
        b_save = findViewById(R.id.button5);

        Onclick();

    }
    private void Onclick(){
        b_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity3.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // 权限未授予，请求权限
                    ActivityCompat.requestPermissions(MainActivity3.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    // 权限已授予，启动相机
                    openCamera();
                }
            }
        });
        b_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity3.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // 请求权限
                    ActivityCompat.requestPermissions(MainActivity3.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                } else {
                    // 权限已授予，打开图库
                    openGallery();
                }

            }
        });
        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = formatter.format(date);

                ContentValues contentValues = new ContentValues();
                contentValues.put("title",ed_titel.getText().toString());
                contentValues.put("content",ed_content.getText().toString());
                contentValues.put("imgpath",tmp_path);
                contentValues.put("mtime",currentTime);
                db.insert("tb_memory",null,contentValues);
                Intent intent = new Intent(MainActivity3.this,MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限已被授予
                openCamera();
            } else {
                // 权限被拒绝
                Toast.makeText(this, "相机权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限已被授予，打开图库
                openGallery();
            } else {
                // 权限被拒绝
                Toast.makeText(this, "存储权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // 处理拍摄的图像
            Bundle extras = data.getExtras();
            // 例如将图片设置到 ImageView
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            tmp_path=saveBitmapToFile(imageBitmap);
        }
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // 显示选择的图片
                imageView.setImageURI(selectedImageUri);
                tmp_path=getRealPathFromURI(selectedImageUri);
                // 如果需要将图片转换为 Bitmap，可以使用以下代码
                // Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            }
        }
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    private String saveBitmapToFile(Bitmap bitmap) {
        // 指定保存图片的路径
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String fileName = "IMG_" + System.currentTimeMillis() + ".png"; // 以当前时间戳命名
        File imageFile = new File(storageDir, fileName);

        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            // 将 Bitmap 写入文件
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            // 显示保存成功的提示
            Toast.makeText(this, "图片保存到: " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "图片保存异常", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader cursorLoader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }


}