package com.dbtapps.chatroom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dbtapps.chatroom.R;
import com.dbtapps.chatroom.constants.Constants;
import com.dbtapps.chatroom.databinding.ActivityHomeBinding;
import com.dbtapps.chatroom.adapters.FragmentAdapter;
import com.dbtapps.chatroom.utilities.FinishCurrentActivity;
import com.dbtapps.chatroom.utilities.MakeToast;
import com.dbtapps.chatroom.utilities.PermissionManager;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;

public class HomePage extends AppCompatActivity {

    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.actionBar);
        setFragments();
        startViewPagerListener();
        startTabChangeListener();

        PermissionManager.permissionManager(this);
    }

    private void startTabChangeListener() {
        binding.tabsTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.mainFragmentVp2.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void startViewPagerListener() {
        binding.mainFragmentVp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabsTl.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void setFragments() {
        FragmentStateAdapter adapter = new FragmentAdapter(this);
        binding.mainFragmentVp2.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getTitle().toString()){
            case "Sign Out":
                MakeToast.makeToast(this, "Signing out");
                Map<String, Object> userData = new HashMap<>();
                userData.put(Constants.DB_NAME, Constants.getKeyName());
                userData.put(Constants.DB_PASSWORD, Constants.getKeyPassword());
                userData.put(Constants.DB_PHONE_NUMBER, Constants.getKeyPhone());
                userData.put(Constants.DB_PROFILE_PICTURE, Constants.getKeyProfilePicture());

                Constants.db.collection(Constants.DB_USERS)
                        .document(Constants.getKeyUserid())
                        .set(userData)
                        .addOnSuccessListener(unused -> {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                            Intent intent = new Intent(HomePage.this, AuthenticationPage.class);
                            startActivity(intent, options.toBundle());
                            FinishCurrentActivity.finish(this);
                        });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}