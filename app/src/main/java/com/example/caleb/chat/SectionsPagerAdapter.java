package com.example.caleb.chat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Switch;

/**
 * Created by Caleb on 4/2/2018.
 */
/*************************************************************
 *SectionsPagerAdapter
 *
 * sets up the the three tabs at the top of the screen
 *
 * requests
 * chat
 * friends
 *
 *
 *************************************************************/
class SectionsPagerAdapter extends FragmentPagerAdapter{

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /****************************************************************
     *getItem()
     *
     * figure out where the user is return the current tab
     *
     ********************************************************************/
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                ReqFragment reqFragment = new ReqFragment();
                return reqFragment;
            case 1:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 2:
                FriendFragment friendFragment = new FriendFragment();
                return friendFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3; // 3 tabs
    }
    /*************************************************************
     *CharSequence()
     *
     * sets the title for each tab
     *
     *************************************************************/
    @Override
    public CharSequence getPageTitle(int position) {

        switch(position){
            case 0:
                return "REQUESTS";
            case 1:
                return "CHAT";
            case 2:
                return "FRIENDS";
            default:
                return null;
        }

    }
}
