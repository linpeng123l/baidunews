package com.linpeng.baidunewssearch;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.linpeng.animation.AnimationFactory;
import com.linpeng.database.LoveNewsManage;
import com.linpeng.fragment.FragmentNews;
import com.linpeng.view.LPGridView;
import com.linpeng.view.LPTabView;
import com.linpeng.view.LPTabView.OnTabClickListener;

import cpm.linpeng.adapter.NewsViewPagerAdapter;

/**
 * �ٶ���������
 * @author linpeng123l
 *
 */
public class MainActivity extends FragmentActivity {

	private ViewPager viewPager;
	private NewsViewPagerAdapter adapter;
	List<Fragment> fragments = new ArrayList<Fragment>();
	private LPTabView lpTabView ;
	private LPGridView lpGridView ;
	private List<String> titles = new ArrayList<String>();
	MyPageChangeListener myPageChangeListener = new MyPageChangeListener();
	private View view1 ; 
	private View view2 ;
	private LoveNewsManage loveNewsManage;
	private EditText editTextLoveNews ; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loveNewsManage = new LoveNewsManage(this);
		initView1();
		initView2();
	}

	/**
	 * ����1��������
	 */
	private void initView1() {
		view1 = (View)findViewById(R.id.activity_main_view1);
		titles = getDatas();
		//עLPView���Զ���View��ʵ����Tab�Ĳ���(�ɻ���,��ѡ��..)
		lpTabView = (LPTabView)findViewById(R.id.activity_main_lptab);
		lpTabView.setTitles(titles);
		lpTabView.postInvalidate();
		lpTabView.setOnTabClickListener(new OnTabClickListener() {
			@Override
			public void onClickTab(int index) {
				myPageChangeListener.isScroll = false;
				viewPager.setCurrentItem(index);
			}
		});

		for(String title : titles){
			FragmentNews fragmentNews = FragmentNews.newInstance(title);
			fragments.add(fragmentNews);
		}
		adapter = new NewsViewPagerAdapter(getSupportFragmentManager(), fragments);
		viewPager = (ViewPager)findViewById(R.id.activity_main_news_tab_viewpager);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(myPageChangeListener);
	}
	
	/**
	 * ��ʼ������2��������ϲ�������Ž���
	 */
	private void initView2() {
		view2 = (View)findViewById(R.id.activity_main_view2);
		editTextLoveNews = (EditText)findViewById(R.id.activity_main_view2_edit_love_news);
		//����Ҳ���Զ���Ľ��棬Ϊ���Ժ�������Ӹ��ӵĴ���,������קɾ����
		lpGridView = (LPGridView)findViewById(R.id.activity_main_lpgrid);
		lpGridView.setTitles(titles);
		lpGridView.postInvalidate();
	}

	/**
	 * ViewPager��������
	 * @author linpeng123l
	 *
	 */
	public class MyPageChangeListener implements OnPageChangeListener{
		private int currentIndex = 0;
		private boolean isScroll = true;
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			isScroll = true;
		}
		@Override
		public void onPageSelected(int arg0) {
			if(isScroll){
				if(arg0>currentIndex){
					lpTabView.moveRight();
				}else{
					lpTabView.moveLeft();
				}
			}
			currentIndex = arg0;
		}
	}

	/**
	 * ���ݻ�ȡ
	 * @return
	 */
	private List<String> getDatas() {
		List<String> strs = new ArrayList<String>();
		strs.add("������");
		strs.add("�Ϲ���");
		strs.add("�ȸ�");
		strs.add("�Ϸ�");
		strs.add("�Ƽ�");
		strs.add("��ӡ");
		strs.add("С��");
		strs.add("ƻ��");
		strs.add("�ֻ�");
		strs.add("��׿");
		strs.addAll(loveNewsManage.getAllNewsTitles());
		return strs;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu); 
		return true;
	}


	
	private boolean isNewSelectBtnDown = true;
	/**
	 * �û�����������ŵļ�ͷ��ť�ļ�������
	 */
	public void btnNewsSelectClick(View view){
		if(isNewSelectBtnDown){
			view2.setVisibility(View.VISIBLE);
			TranslateAnimation translateAnimation = AnimationFactory.getTranslateAnimation(0, 0, -view1.getHeight(), 0, 400);
			view2.startAnimation(translateAnimation);
			translateAnimation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					view1.setVisibility(View.GONE);
				}
			});
		}else{
			view1.setVisibility(View.VISIBLE);
			TranslateAnimation translateAnimation = AnimationFactory.getTranslateAnimation(0, 0, 0, -view2.getHeight(), 400);
			view2.startAnimation(translateAnimation);
			view2.setVisibility(View.GONE);
		}
		isNewSelectBtnDown = !isNewSelectBtnDown;
	}

	/**
	 * �û�����������ŵİ�ť��������
	 * @param view
	 */
	public void btnAddLoveNews(View view){
		if(TextUtils.isEmpty(editTextLoveNews.getText())){
			Toast.makeText(getApplicationContext(), "������������Ȥ������.", Toast.LENGTH_SHORT).show();
		}else if(checkLoveNewsExist(editTextLoveNews.getText().toString())){
			Toast.makeText(getApplicationContext(), "������������Ѵ���.", Toast.LENGTH_SHORT).show();
		}else{
			titles.add(editTextLoveNews.getText().toString());
			lpTabView.setTitles(titles);
			lpGridView.setTitles(titles);
			lpGridView.postInvalidate();
			adapter.addFragment(FragmentNews.newInstance(editTextLoveNews.getText().toString()));
			adapter.notifyDataSetChanged();
			loveNewsManage.addLoveNews(editTextLoveNews.getText().toString());
		}
		editTextLoveNews.setText("");
	}
	/**
	 * ��鴫������ű����Ƿ����
	 * @param string
	 * @return
	 */
	private boolean checkLoveNewsExist(String string) {
		for(String title:titles){
			if(title.equals(string)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_share:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain"); 
			intent.putExtra(Intent.EXTRA_TEXT, "����һ�����Զ��ƹؼ��ʵ�����app,��ӭ����"); 
			startActivity(Intent.createChooser(intent, getTitle()));
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {  
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {  
				try {  
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);  
					m.setAccessible(true);  
					m.invoke(menu, true);  
				} catch (Exception e) {  
					e.printStackTrace();
				}  
			}  
		}  
		return super.onMenuOpened(featureId, menu);
	}

}