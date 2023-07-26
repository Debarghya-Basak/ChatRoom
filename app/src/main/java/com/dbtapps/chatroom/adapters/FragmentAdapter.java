package com.dbtapps.chatroom.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.fragments.CallFragment;
import com.dbtapps.chatroom.fragments.ChatFragment;
import com.dbtapps.chatroom.fragments.StatusFragment;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0)
            return new ChatFragment();
        else if (position == 1)
            return new StatusFragment();
        else if(position == 2)
            return new CallFragment();
        else
            return new ChatFragment();
    }

    @Override
    public int getItemCount() {
        return Constants.getMainPageFragmentCount();
    }
}
