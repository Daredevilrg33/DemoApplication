package com.example.rohitgupta3.demoapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private Uri fileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnUpload = (Button) findViewById(R.id.btn_upload);
        imageView = (ImageView) findViewById(R.id.image_view);
        btnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_upload:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermissionGrantedForCamera();
                } else {
                    selectImage();
                }

                break;
        }

    }


    private void selectImage() {


        final CharSequence[] items = {getString(R.string.take_new_picture), getString(R.string.select_picture_from_gallery), getString(R.string.open_file_explorer), getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.upload_attachment));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.take_new_picture))) {
                    boolean checkPermission = hasPermissionInManifest(MainActivity.this, "android.permission.CAMERA");
                    if (checkPermission) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        fileUri = getOutputMediaFileUri(ApplicationConstant.getMediaTypeImage());
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            intent.setClipData(ClipData.newRawUri("", fileUri));
                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        }


                        // start the image capture Intent
                        startActivityForResult(intent, ApplicationConstant.getPickFromCamera());
                    }
                } else if (items[item].equals(getString(R.string.select_picture_from_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");

                    startActivityForResult(Intent.createChooser(intent, "Select File"), ApplicationConstant.getPickFromGallery());
                } else if (items[item].equals(getString(R.string.open_file_explorer))) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        intent.putExtra("CONTENT_TYPE", "file/*");
                        startActivityForResult(intent, ApplicationConstant.getPickFromFileExplorer());
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
//                    showAlertDialog(QuestionActivity.this,"",getResources().getString(R.string.error_msg_file_explorer),false);
                    }
                } else if (items[item].equals(getString(R.string.cancel))) {

                    dialog.dismiss();

                }

            }
        });
        builder.show();
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ApplicationConstant.getPickFromCamera()) {
                if (fileUri.getPath() != null) {

                    String path = fileUri.getPath();

                    uploadFile(path);
                    Log.d("Camera Image Path", "" + path);
                } else {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Log.d("Bitmap", "" + photo.toString());
                    imageView.setImageBitmap(photo);
                }
            } else if (requestCode == ApplicationConstant.getPickFromGallery()) {
                try {

                    String path = getRealPathFromURI(data.getData());
                    Log.d("GALLERY Image Path", "" + path);
                    uploadFile(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == ApplicationConstant.getPickFromFileExplorer()) {
                try {
//                    String path = getRealPathFromURIFileExplorer(Uri.parse(data.getDataString()));
//                    Log.d("EXPLORER Image Path", "" + path);
                    String path1 = getRealPathFromURI(data.getData());
                    Log.d("EXPLORER Image Path", "" + path1);
                    uploadFile(path1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void uploadFile(String filePath) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        bmOptions.inSampleSize = 2;
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inTempStorage = new byte[16 * 1024];
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        imageView.setImageBitmap(bitmap);

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermissionGrantedForCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.CAMERA)) {

            }

            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    ApplicationConstant.CAMERA_PERMISSIONS_REQUEST);
        } else {
            checkPermissionGrantedForReadExternalStorage();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermissionGrantedForReadExternalStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    ApplicationConstant.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST);
        } else {
            checkPermissionGrantedForWriteExternalStorage();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermissionGrantedForWriteExternalStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            }

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ApplicationConstant.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST);
        } else {
            selectImage();
        }
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == ApplicationConstant.CAMERA_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                checkPermissionGrantedForReadExternalStorage();
            } else {
//                Toast.makeText(this, "Camera Request Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == ApplicationConstant.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                checkPermissionGrantedForWriteExternalStorage();
            } else {
//                Toast.makeText(this, "Read External Storage permission denied", Toast.LENGTH_SHORT).show();

            }
        } else if (requestCode == ApplicationConstant.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                selectImage();
            } else {
//                Toast.makeText(this, "Read External Storage permission denied", Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public boolean hasPermissionInManifest(Context context, String permissionName) {
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method will return the Media File.
     *
     * @param type
     * @return
     */
    public static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "Camera");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(ApplicationConstant.getImageDirectoryName(), "Oops! Failed create " + ApplicationConstant.getImageDirectoryName() + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile = null;
        if (type == ApplicationConstant.getMediaTypeImage()) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".jpg");
        }
        return mediaFile;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = this.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

}
