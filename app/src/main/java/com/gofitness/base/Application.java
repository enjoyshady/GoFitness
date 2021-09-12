package com.gofitness.base;

import android.view.Gravity;

import com.gofitness.database.Constants;
import com.gofitness.database.entity.ExerciseProjectEntity;
import com.gofitness.database.repository.ExerciseProjectRepository;
import com.gofitness.utils.SharedPreferenceUtils;
import com.hjq.toast.ToastUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 ToastUtils
        ToastUtils.init(this);
         ToastUtils.setGravity(Gravity.BOTTOM, 0, 300);
        // 在这里初始化数据库默认数据 （ Exercise Project ）
        new Thread(this::initializeApplication).start();
    }

    /**
     * 初始化数据库默认数据 （ Exercise Project ）
     */
    private void initializeApplication() {
        if (SharedPreferenceUtils.getBoolean(this, Constants.IS_INITIALIZATION_DATABASE, false)) {
            return; // 已经初始化过了
        }
        try {
            // 取出数据
            StringBuilder content = new StringBuilder();
            InputStream inputStream = getAssets().open("DefaultExerciseProjectData.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line; // 分行读取
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            inputStream.close();
            // 转为list
            List<ExerciseProjectEntity> entities = ExerciseProjectEntity.getListForJsonString(content.toString());
            // 写入数据库
            ExerciseProjectRepository.getInstance(this).insertProjectData(entities);
            // 更新初始化状态
            SharedPreferenceUtils.putBoolean(this, Constants.IS_INITIALIZATION_DATABASE, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
