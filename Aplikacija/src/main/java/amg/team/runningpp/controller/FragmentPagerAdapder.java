package amg.team.runningpp.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentPagerAdapder extends FragmentStatePagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();

    private  final List<String> fragmentTitleList = new ArrayList<>();

    private  final List<Bundle> fragmentBundleList = new ArrayList<>();

    public FragmentPagerAdapder(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment,String title,Bundle bundle){
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
        if(bundle == null){
            bundle = new Bundle();
        }
        fragmentBundleList.add(bundle);
    }

    public Bundle getBundle(int i){
        return fragmentBundleList.get(i);
    }

    public void setBundle(int i,Bundle bundle){
        fragmentBundleList.set(i,bundle);
    }

    public void changeFragment(Fragment fragment,int i){
        fragmentList.set(i,fragment);
    }
    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }
    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
