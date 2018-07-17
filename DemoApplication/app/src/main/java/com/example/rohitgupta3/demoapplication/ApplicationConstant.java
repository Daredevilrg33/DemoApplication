package com.example.rohitgupta3.demoapplication;

public class ApplicationConstant {

    public static final int ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST = 1;
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST = 2;
    public static final int RECORD_AUDIO_PERMISSION_REQUEST = 3;
    public static final int CAMERA_PERMISSIONS_REQUEST = 4;
    public static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 5;

    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private static final int PICK_FROM_FILE_EXPLORER = 3;
    private static final String IMAGE_DIRECTORY_NAME = "Demo";

    public static int getMediaTypeImage() {
        return MEDIA_TYPE_IMAGE;
    }

    public static int getPickFromCamera() {
        return PICK_FROM_CAMERA;
    }

    public static int getPickFromGallery() {
        return PICK_FROM_GALLERY;
    }

    public static int getPickFromFileExplorer() {
        return PICK_FROM_FILE_EXPLORER;
    }

    public static String getImageDirectoryName() {
        return IMAGE_DIRECTORY_NAME;
    }

}
