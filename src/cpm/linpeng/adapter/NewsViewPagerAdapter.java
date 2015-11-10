package cpm.linpeng.adapter;

import java.util.ArrayList;
import java.util.List;

import com.linpeng.fragment.FragmentNews;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;
public class NewsViewPagerAdapter extends FragmentPagerAdapter{

	private List<Fragment> fragments = new ArrayList<Fragment>();
	private FragmentManager fm;
	private FragmentTransaction mCurTransaction;
    private Fragment mCurrentPrimaryItem;

	public NewsViewPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		this.fm=fm;
		this.fragments = fragments;
	}

	public void addFragment(Fragment fragment){
		this.fragments.add(fragment);
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);

	}

	@Override
	public int getCount() {
		return fragments.size();
	} 
	

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (mCurTransaction == null) {
			mCurTransaction = fm.beginTransaction();
		}
//		mCurTransaction.detach((Fragment)object);
	}

	
}
