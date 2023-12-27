package com.example.textapp2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.textapp2.ui.login.LoginFragment;

public class TabsAdapter extends FragmentStateAdapter {

    public TabsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new LoginFragment();
        } else if (position == 1) {
            return new StonksFragment();
        } else {
            return new SharesPlaceholderFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
