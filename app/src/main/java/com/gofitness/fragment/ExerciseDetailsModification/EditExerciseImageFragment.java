package com.gofitness.fragment.ExerciseDetailsModification;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.gofitness.R;
import com.gofitness.fragment.ExerciseDetails.ExerciseDetailsViewModel;
import com.gofitness.utils.FileUtils;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * 不能继承 BaseFragment  , 会导致 toolbar 重复设置
 */
public class EditExerciseImageFragment extends Fragment implements View.OnClickListener {

    private static final int PHOTOS_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAMERA_ACTIVITY_REQUEST_CODE = 200;

    private ExerciseDetailsViewModel mViewModel;

    private ImageView mImageView;
    private Uri photoURI = null;


    public static EditExerciseImageFragment newInstance() {
        return new EditExerciseImageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_exercise_image, container, false);
        mImageView = rootView.findViewById(R.id.details_image);
        mImageView.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ExerciseDetailsViewModel.class);
        mViewModel.getStringParam1().observe(getViewLifecycleOwner(),
                imageURL -> Glide.with(this).load(imageURL).into(mImageView));
    }

    /**
     * 设置图片源，利用 ViewModel 保存
     */
    public void setImagePicture(String imageURL) {
        mViewModel.setStringParam1(imageURL);
    }

    /**
     * 权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                int index = i;
                // TODO 这里没有用英文
                new AlertDialog.Builder(getContext())
                        .setMessage("必须同意权限才能操作哦")
                        .setPositiveButton("确定", (dialogInterface, x) -> requestPermissions(
                                new String[]{permissions[index]}, requestCode)).setCancelable(false)
                        .show();
                return;
            }
        }
        onClick(null);
    }

    /**
     * 图片点击事件
     */
    @Override
    public void onClick(View v) {
        //检查是否已经获得相机（相册）的权限
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
            return;
        }
        // 有权限直接打开选择框
        new AlertDialog.Builder(getContext())
                .setItems(new String[]{"Photos", "Camera"},
                        (dialogInterface, i) -> {
                            if (i == 0) {    // 相册
                                openPhotos();
                            } else if (i == 1) {   // 拍照
                                openCamera();
                            }
                        })
                .show();
    }

    /**
     * 打开相机
     */
    private void openPhotos() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, PHOTOS_ACTIVITY_REQUEST_CODE);
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        File imageFile = new File(FileUtils.getFilePath(getContext()), UUID.randomUUID().toString() + ".png");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { //7.0以下的系统用 Uri.fromFile(file)
            photoURI = Uri.fromFile(imageFile);
        } else {    // 7.0以上的系统用下面这种方案
            photoURI = FileProvider.getUriForFile(getContext(), "com.gofitness.FileProvider", imageFile);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); //将图片文件转化为一个uri传入
        startActivityForResult(intent, CAMERA_ACTIVITY_REQUEST_CODE);
    }

    /**
     * 返回页面回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // photos result
        if (resultCode == RESULT_OK && requestCode == PHOTOS_ACTIVITY_REQUEST_CODE && intent != null && intent.getData() != null) {
            CropImage.activity(intent.getData())
                    .setAspectRatio(1, 1)
                    .start(getContext(), this);
        }
        // camera result
        if (resultCode == RESULT_OK && requestCode == CAMERA_ACTIVITY_REQUEST_CODE && photoURI != null) {
            CropImage.activity(photoURI)
                    .setAspectRatio(1, 1)
                    .start(getContext(), this);
        }
        // crop result
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(intent);
            if (resultCode == RESULT_OK) {
                try {
                    File file = FileUtils.uri2File(getContext(), result.getUri());
                    Glide.with(this).load(file).into(mImageView);
                    if (getParentFragment() instanceof EditExerciseFragment) {
                        ((EditExerciseFragment) getParentFragment()).setExerciseImageURL(file.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                if (error != null)
                    error.printStackTrace();
            }
        }
    }


}