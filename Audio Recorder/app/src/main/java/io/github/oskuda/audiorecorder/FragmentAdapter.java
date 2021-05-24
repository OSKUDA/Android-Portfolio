package io.github.oskuda.audiorecorder;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {
    private static final String TAG = "FragmentAdapter";
    FragmentManager mFragmentManager;

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        mFragmentManager = fragmentManager;
        Log.d(TAG, "FragmentAdapter: called");
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d(TAG, "createFragment: called");

        switch (position) {
            case 1:
                return new SavedRecordingsFragment();
        }

        return new RecordFragment();
    }


    @Override
    public int getItemCount() {
        return 2;
    }
}
