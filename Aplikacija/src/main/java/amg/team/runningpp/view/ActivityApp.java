package amg.team.runningpp.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import amg.team.runningpp.R;
import amg.team.runningpp.controller.FragmentPagerAdapder;
import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListKorisnik;
import amg.team.runningpp.entities.Korisnik;
import amg.team.runningpp.services.ListFriendsServices;

public class ActivityApp extends AppCompatActivity {
    private ViewPager viewPagerL;
    private TabLayout tabLayout;
    SharedPreferences preferences;
    FragmentPagerAdapder adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        viewPagerL = (ViewPager)findViewById(R.id.viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        preferences  = this.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);

        setupViewPager(viewPagerL);
        tabLayout.setupWithViewPager(viewPagerL);


        tabLayout.getTabAt(0).setText("News");
        tabLayout.getTabAt(1).setText("Activity");
        tabLayout.getTabAt(2).setText("Leaderboard");
        tabLayout.getTabAt(3).setText("Profile");

        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        setViewPager(0);
                        break;
                    case 1:
                        setViewPager(1);
                        break;
                    case 2:
                        setViewPager(2);
                        break;
                    case 3:
                        setViewPager(3);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        ListFriendsServices listFriendsServices = new ListFriendsServices();
        listFriendsServices.execute();
        viewPagerL.setCurrentItem(1);
    }

    private void setupViewPager(ViewPager viewPager){
        adapter = new FragmentPagerAdapder(getSupportFragmentManager());
        adapter.addFragment(new FragmentNews(),"News",null);
        adapter.addFragment(new FragmentActivity(),"Activity",null);
        adapter.addFragment(new FragmentLeaderboard(),"Leaderboard",null);
        adapter.addFragment(new FragmentProfil(),"Profile",null);
        viewPager.setAdapter(adapter);
    }

    protected void changeFragment(Fragment fragment,int i){
        try{
            adapter.changeFragment(fragment,i);
            viewPagerL.setAdapter(adapter);
            viewPagerL.setCurrentItem(3);
            tabLayout.getTabAt(0).setText("News");
            tabLayout.getTabAt(1).setText("Activity");
            tabLayout.getTabAt(2).setText("Leaderboard");
            tabLayout.getTabAt(3).setText("Profile");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void changeCurrentFragment(Fragment fragment,int index,int currendFragment){
        try{
            adapter.changeFragment(fragment,index);
            viewPagerL.setAdapter(adapter);
            viewPagerL.setCurrentItem(currendFragment);
            tabLayout.getTabAt(0).setText("News");
            tabLayout.getTabAt(1).setText("Activity");
            tabLayout.getTabAt(2).setText("Leaderboard");
            tabLayout.getTabAt(3).setText("Profile");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void setBundle(int i, Bundle bundle){
        adapter.setBundle(i,bundle);
    }

    protected  Bundle getBundle(int i){
        return adapter.getBundle(i);
    }

    protected void setViewPager(int fragmentIndex){
        viewPagerL.setCurrentItem(fragmentIndex);
    }

    @Override
    public void onBackPressed() {

    }
}
