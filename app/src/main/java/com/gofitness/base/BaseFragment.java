package com.gofitness.base;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gofitness.R;
import com.gofitness.database.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class BaseFragment extends Fragment {


    protected List<Disposable> mDisposables = new ArrayList<>();

    protected static final String TAG = "BaseFragment";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init event bus
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // init toolbar
        try {
            String title = getActivity().getIntent().getStringExtra(Constants.TITLE);
            if (!TextUtils.isEmpty(title)) {
                BaseActivity activity = (BaseActivity) getActivity();
                activity.setSupportActionBar(getView().findViewWithTag("toolbar"));
                setHasOptionsMenu(true);
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setTitle(title);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposables != null && mDisposables.size() != 0) { // 统一释放资源
            for (Disposable disposable : mDisposables) {
                if (!disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 关闭键盘
     */
    protected void closeKeyboard() {
        InputMethodManager inputMethodManager = ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示 toast
     */
    protected void showToast(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

}
