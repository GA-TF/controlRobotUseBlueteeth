package com.ybr.ybrrobot.myfragment;

import com.ybr.ybrrobot.R;

import android.support.v4.app.Fragment;




public class FragmentFactory{
	public static Fragment getInstanceByIndex(int index) {  
        Fragment fragment = null;  
        switch (index) {  
            case R.id.dance:  
                fragment = new DanceFragment();  
                break;  
            case R.id.speak:  
                fragment = new SpeakFragment();  
                break;  
            case R.id.move:  
                fragment = new SupportFragment();  
                break;  

        }  
        return fragment;  
    }  
}
