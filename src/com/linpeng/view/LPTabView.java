package com.linpeng.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class LPTabView extends View{



	private List<LPTabText> tabTexts;  //标题集合
	private int textColor;         //普通色
	private int textColorSelected; //选中色
	private int viewWidth;       //界面宽度
	private int viewHeight;      //界面高度
	private Paint paint;         //画笔


	private int textSize;        //字体大小
	private int tabDistance;        //标题间距
	private int currentIndex = 0;//当前选中标题
//	private int lastSatrtOffset; //viewpager偏移到最后时的偏移位置
	private int startOffset;     //起始偏移位置
	private int endOffset;      //结束item最多偏移位置

	private int flag = FLAG_MOVE_VIEWPAGER;             //用户什么操作导致重新作图
	private final static int FLAG_MOVE_VIEWPAGER = 0;  //标识用户移动viewpager
	private final static int FLAG_MOVE_TAB = 1;        //标识用户移动tab

	public LPTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setColor(Color.parseColor("#eeeeee"));
		ViewTreeObserver vto2 = this.getViewTreeObserver();    
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
			@SuppressWarnings("deprecation")
			@Override    
			public void onGlobalLayout() {
				//界面布局后初始化各变量
				LPTabView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);   
				viewWidth = LPTabView.this.getMeasuredWidth();
				viewHeight = LPTabView.this.getMeasuredHeight();
				textSize = viewHeight*8/20;
				tabDistance = viewWidth/15;
				startOffset = tabDistance/2;
				endOffset = tabDistance/2;
				paint.setStrokeWidth(10);
				moveTabsToFirst();
//				lastSatrtOffset = measureLastDrawWidth();
			}   
		});
		textColor = Color.parseColor("#555555");
		textColorSelected = Color.parseColor("#3993CF");
	}
	public void setTitles(List<String> titles){
		tabTexts = new ArrayList<LPTabView.LPTabText>();
		for(String title : titles){
			tabTexts.add(new LPTabText(title, 0, 0));
		}
		moveTabsToFirst();
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if(currentIndex==0&&flag==FLAG_MOVE_VIEWPAGER){
			drawView(currentIndex, canvas);
		}else{
			drawView( currentIndex, canvas);
		}
	}

	/**
	 * 作图函数
	 * @param offset 起始位置
	 * @param currentIndex 当前item
	 * @param canvas 画图
	 */
	public void drawView(int currentIndex,Canvas canvas){
		if(tabTexts!=null){
			paint.setTextSize(textSize);
			for(int i =0; i<tabTexts.size(); i++){
				LPTabText tabText = tabTexts.get(i);
				String title = tabText.text;
				if(i==currentIndex){
					paint.setColor(textColorSelected);
					canvas.drawLine(tabText.drawStartX-tabDistance/3, viewHeight, 
							tabText.drawEndX+tabDistance/3, viewHeight, paint);
				}else{
					paint.setColor(textColor);
				}
				canvas.drawText(title, tabText.drawStartX, (viewHeight-textSize)/2+textSize, paint);
			}
		}
	}

	/**
	 * tab向右偏移
	 */
	public void moveRight(){
		currentIndex++;
		moveTabsToCurrentTab(currentIndex);
		flag = FLAG_MOVE_VIEWPAGER;
		postInvalidate();
	}
	/**
	 * tab向左偏移
	 */
	public void moveLeft(){
		currentIndex--;
		moveTabsToCurrentTab(currentIndex);
		flag = FLAG_MOVE_VIEWPAGER;
		postInvalidate();
	}


	/**
	 * 初始化各个标签的位置
	 */
	private void moveTabsToFirst() {
		float startX = startOffset; 
		for(int i = 0; i<tabTexts.size();i++){
			LPTabText tabText = tabTexts.get(i);
			float endX = measureTextLength(tabText.text)+startX;
			tabText.drawStartX = startX;
			tabText.drawEndX = endX;
			startX = endX + tabDistance;
		}
	} 
	/**
	 * 将tabs移动到最后
	 */
	private void moveTabsToLast(){
		float endX = viewWidth-endOffset; 
		for(int i = tabTexts.size()-1; i>=0;i--){
			LPTabText tabText = tabTexts.get(i);
			float startX = endX - measureTextLength(tabText.text);
			tabText.drawStartX = startX;
			tabText.drawEndX = endX;
			endX = startX - tabDistance;
		}
	}
	
	/**
	 * 根据当前选择移动每一个标签
	 * @param currentTab 当前选择标签
	 */
	public void moveTabsToCurrentTab(int currentTab){
		float moveDistance = countMoveDistance(currentTab);
		moveTabsToFirst();
		moveTabsWithDistance(moveDistance);
	}
	/**
	 * 根据当前选中标签计算每个标签距初始位置的移动距离
	 * @param currentTab 当前选中标签
	 */
	public float countMoveDistance(int currentTab){
		float moveDistance = -tabDistance;
		for(int i = 0;i<currentTab;i++){
			moveDistance +=measureTextLength(tabTexts.get(i).text)+tabDistance;
		}
		return moveDistance;
	}
	
	/**
	 * 移动tabs 
	 * @param distance 距离正向左移，负向右移
	 */
	public void moveTabsWithDistance(float moveDistance){
		if(moveDistance>0){
			LPTabText tabText = tabTexts.get(tabTexts.size()-1);
			if(tabText.drawEndX-moveDistance<=viewWidth-endOffset){
				moveTabsToLast();
				return;
			}
		}else{
			LPTabText tabText = tabTexts.get(0);
			if(tabText.drawStartX-moveDistance>tabDistance){
				moveTabsToFirst();
				return;
			}
		}
		for(LPTabText tabText : tabTexts){
			tabText.drawStartX = tabText.drawStartX - moveDistance;
			tabText.drawEndX = tabText.drawEndX - moveDistance;
		}
	}
	
	/**
	 * 确定viewpager偏移到最后时作图的偏移位置
	 * @param currentIndex
	 */
	private int measureLastDrawWidth(){
		int totalWidth = 0;
		for(int i = 0; i<tabTexts.size(); i++){
			String title = tabTexts.get(i).text;
			totalWidth += measureTextLength(title) + tabDistance;
		}
		return viewWidth - totalWidth ;
	}

	public int measureTextLength(String title){
		return textSize*title.length();
	}

	
	private float startX;
	private float startY;
	private float currentX;
	private float currentY;
	/**
	 * 用户触摸事件重新函数
	 * 实现移动标题头，选择谋个Tab的监听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			currentX = event.getX();
			currentY = event.getY();
			startX = currentX;
			startY = currentX;
			break;
		case MotionEvent.ACTION_UP:
			currentX = event.getX();
			//判断用户是否是点击事件（是的话判断用户点击哪个item，并产生Tab点击事件给外界）
			if(Math.abs(startX-currentX)<viewWidth/10){
				for(int i = 0; i<tabTexts.size(); i++){
					LPTabText tabText = tabTexts.get(i);
					if(tabText.checkClick(currentX)){
						currentIndex = i;
						invalidate();
						tabClickListener.onClickTab(i);
						break;
					}
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float lastX = currentX;
			float lastY = currentY;
			currentX = event.getX();
			currentY = event.getY();
			flag = FLAG_MOVE_TAB;
			moveTabsWithDistance(lastX-currentX);
			postInvalidate();
			//判断移动方向
			if(currentX-lastX>0){
				postInvalidate();
			}else{
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		default:
			break;
		}
		return true;
	}
	
	/**
	 * 自定义数据类，代表一个tab头
	 * @author linpeng123l
	 *
	 */
	private class LPTabText{
		private String text;
		private float drawStartX;
		private float drawEndX;
		public LPTabText(String text, float drawStartX, float drawEndX) {
			super();
			this.text = text;
			this.drawStartX = drawStartX;
			this.drawEndX = drawEndX;
		}
		public boolean checkClick(float x){
			if(drawStartX<x&&drawEndX>x){
				return true;
			}
			return false;
		}
	}

	/**
	 * 用户选择谋个tab时响应函数的绑定
	 */
	private OnTabClickListener tabClickListener;
	public void setOnTabClickListener(OnTabClickListener onTabClickListener){
		this.tabClickListener = onTabClickListener;
	}

	public interface OnTabClickListener{
		/**
		 * 点击tab监听返回函数
		 * @param index tab标签位置
		 */
		public void onClickTab(int index);
	}

}
