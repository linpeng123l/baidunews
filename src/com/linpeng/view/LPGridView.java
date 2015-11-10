package com.linpeng.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class LPGridView extends View{
	private List<LPGridTabText> gridTabTexts;  //标题集合
	private int textColor;         //普通色
	private int viewWidth;       //界面宽度
	private int viewHeight;      //界面高度
	private Paint paint;         //画笔

	private int textSize;        //字体大小
	private int gridHorizontalDistance;        //标题间行距
	private int gridVerticalDistance;         //标题间列距
	private int gridTabHorizontalNumbers = 4; //每行tab的数量
	private int gridTabWidth;
	private int gridTabHeight;
	private int radius ;
	public LPGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		textColor = Color.parseColor("#464646");
	}
	
	public void initMeasureView(){
		viewWidth = LPGridView.this.getMeasuredWidth();
		viewHeight = LPGridView.this.getMeasuredHeight();
		gridHorizontalDistance = viewWidth/20;
		gridVerticalDistance = viewWidth/25;
		gridTabWidth = (viewWidth - (gridTabHorizontalNumbers-1)*gridHorizontalDistance)/gridTabHorizontalNumbers;
		gridTabHeight = viewWidth/12;
		textSize = gridTabHeight/2;
		paint.setTextSize(textSize);
		radius = viewWidth/70;
		initGridTabs();
	}

	protected void initGridTabs() {
		float startX = 0; 
		float startY = 0; 
		for(int i = 0; i<gridTabTexts.size();i++){
			LPGridTabText tabText = gridTabTexts.get(i);
			tabText.drawStartX = startX;
			tabText.drawStartY = startY;
			if((i+1)%4==0){
				startY = startY + gridTabHeight+gridVerticalDistance;
				startX = 0;
			}else{
				startX = gridTabWidth + startX + gridHorizontalDistance;
			}
		}
	}

	public void setTitles(List<String> titles){
		gridTabTexts = new ArrayList<LPGridView.LPGridTabText>();
		for(String title : titles){
			gridTabTexts.add(new LPGridTabText(title, 0, 0));
		}
		initGridTabs();
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if(viewHeight==0){
			initMeasureView();
		}
		drawGrid(canvas);
	}

	public void drawGrid(Canvas canvas){
		for(int i = 0;i<gridTabTexts.size();i++){
			LPGridTabText tabText = gridTabTexts.get(i);
			float startX = tabText.drawStartX;
			float startY = tabText.drawStartY;
			RectF rectFL = new RectF(startX, startY,startX+gridTabWidth, startY+gridTabHeight);
			RectF rectF = new RectF(startX+1, startY+1,startX+gridTabWidth-1, startY+gridTabHeight-1);
			paint.setColor(Color.parseColor("#D5D5D5"));
			canvas.drawRoundRect(rectFL, radius, radius, paint);
			paint.setColor(Color.parseColor("#f8f8f8"));
			canvas.drawRoundRect(rectF, radius, radius, paint);
			
			paint.setColor(textColor);
			float textLength = measureTextLength(tabText.text);
			if(textLength<gridTabWidth){
				canvas.drawText(tabText.text, startX+(gridTabWidth-textLength)/2, startY+(gridTabHeight-textSize)/2+textSize, paint);
			}else{
				textLength = measureTextLength(tabText.text.substring(0,4));
				canvas.drawText(tabText.text.substring(0, 3)+"..", startX+(gridTabWidth-textLength)/2, startY+(gridTabHeight-textSize)/2+textSize, paint);
			}
		}

	}

	public int measureTextLength(String title){
		return textSize*title.length();
	}
	
	private class LPGridTabText{
		private String text;
		private float drawStartX;
		private float drawStartY;
		public LPGridTabText(String text, float drawStartX, float drawStartY) {
			super();
			this.text = text;
			this.drawStartX = drawStartX;
			this.drawStartY = drawStartY;
		}
		public boolean checkClick(float x){
			return false;
		}
	}
}
