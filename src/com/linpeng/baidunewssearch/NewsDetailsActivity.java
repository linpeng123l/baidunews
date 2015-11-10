package com.linpeng.baidunewssearch;


import javax.crypto.Cipher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.linpeng.baidunewssearch.R;
import com.linpeng.util.FileUtil;

public class NewsDetailsActivity extends Activity implements OnTouchListener{

	private WebView webView;
	private WebView showWebView;
	private float startX;
	private float startY;
	private RelativeLayout relativeLayout;
	private String url;
	private long countLoad;
	private boolean isLoaded = false;
	private Handler handler = new Handler(){
		@SuppressLint("SetJavaScriptEnabled") @Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				System.out.println(msg.obj.toString().length());
				if(msg.obj.toString().length()<2500&&countLoad<=1){
					Intent intent = new Intent(NewsDetailsActivity.this, NewsDetailsActivity.class);
					intent.putExtra("url", url);
					countLoad ++;
					intent.putExtra("countLoad",countLoad);
					startActivity(intent);
					finish();
				}else if(msg.obj.toString().length()<2500){
					Toast.makeText(getApplicationContext(), "加载失败", Toast.LENGTH_SHORT).show();
				}else{
					showWebView.getSettings().setBlockNetworkImage(false);
					showWebView.getSettings().setJavaScriptEnabled(true);
					showWebView.loadDataWithBaseURL("http://m.baidu.com/news/", msg.obj.toString().replace("data-url", "src")
							.replace("class=\"lazy-load\"","").replace("本日点击排行榜", "")
							.replace("查看原图", ""), "text/html", "utf-8",null);
					showWebView.setVisibility(View.VISIBLE);
				}
			}
		}
	};

	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		url = getIntent().getStringExtra("url");
		countLoad = getIntent().getLongExtra("countLoad", 1);
		relativeLayout = (RelativeLayout)findViewById(R.id.activity_news_details_relative_is_loading);
		showWebView = (WebView)findViewById(R.id.news_details_webview);
		showWebView.getSettings().setJavaScriptEnabled(true);
		webView = new WebView(getApplicationContext());
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
		webView.setWebViewClient(new MyWebViewClient());
		webView.getSettings().setBlockNetworkImage(true);
		webView.loadUrl("http://m.baidu.com/news?tn=bdbody&src="+url+"&pu=sz@1320_2001,usm@4,ta@iphone_1_4.3_3_533&bd_page_type=1");
		webView.setOnTouchListener(this);
		relativeLayout.setOnTouchListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * javascript坚挺函数 
	 * @author linpeng123l
	 *
	 */

	final class InJavaScriptLocalObj{
		/**
		 * 将取得的html中不需要的内容去掉
		 * @param html
		 */
		@JavascriptInterface
		public void showSource(String html) {
			FileUtil.addFile(html, Environment.getExternalStorageDirectory().getPath()+"/apmt/", "aa.html");
			Log.i("jiazai", html);
			/*if(html.contains("加载中，请稍候")){
				Toast.makeText(getApplicationContext(), "加载错误", 1).show();
				webView.reload();
			}else */if(!isLoaded){
				Document document = Jsoup.parse(html);
				Elements imgs = document.getElementsByTag("img");
				if (imgs!=null&&imgs.size() != 0){
					for (Element img : imgs) {
						img.attr("style", "width:100%;height:auto;");
					}
				}
				Elements elements = document.getElementsByClass("page-view-article");
				elements.remove(elements.select(".img-eye"));
				elements.select(".img-eye").remove();

				Message message = new Message();
				message.obj = document.head()+elements.toString();
				message.what = 1;
				handler.sendMessage(message);
				isLoaded = true;
			}else{
								Toast.makeText(getApplicationContext(), "finish2", 1).show();
			}
		}
	}

	/*
	 * 监听返回按钮
	 */
	public void back(View view){
		finish();
	}


	/**
	 * webview监听函数
	 * @author linpeng123l
	 *
	 */
	final class MyWebViewClient extends WebViewClient{ 
		public int count;

		public void onPageFinished(WebView view, String url) {
			if(isLoaded){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				view.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
						"document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			}
		}
	}

	/**
	 * 左滑动返回监听
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			startY = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			float endX = event.getX();
			float endY = event.getY();
			if(Math.abs(endX-startX)>330&&((endY-startY)==0||Math.abs((endX-startX)/(endY-startY))>2)){
				finish();
			}
			break;
		default:
			break;
		}
		return false;
	}
}
