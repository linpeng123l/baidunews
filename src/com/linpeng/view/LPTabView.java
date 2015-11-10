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



	private List<LPTabText> tabTexts;  //���⼯��
	private int textColor;         //��ͨɫ
	private int textColorSelected; //ѡ��ɫ
	private int viewWidth;       //������
	private int viewHeight;      //����߶�
	private Paint paint;         //����


	private int textSize;        //�����С
	private int tabDistance;        //������
	private int currentIndex = 0;//��ǰѡ�б���
//	private int lastSatrtOffset; //viewpagerƫ�Ƶ����ʱ��ƫ��λ��
	private int startOffset;     //��ʼƫ��λ��
	private int endOffset;      //����item���ƫ��λ��

	private int flag = FLAG_MOVE_VIEWPAGER;             //�û�ʲô��������������ͼ
	private final static int FLAG_MOVE_VIEWPAGER = 0;  //��ʶ�û��ƶ�viewpager
	private final static int FLAG_MOVE_TAB = 1;        //��ʶ�û��ƶ�tab

	public LPTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setColor(Color.parseColor("#eeeeee"));
		ViewTreeObserver vto2 = this.getViewTreeObserver();    
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
			@SuppressWarnings("deprecation")
			@Override    
			public void onGlobalLayout() {
				//���沼�ֺ��ʼ��������
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
	 * ��ͼ����
	 * @param offset ��ʼλ��
	 * @param currentIndex ��ǰitem
	 * @param canvas ��ͼ
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
	 * tab����ƫ��
	 */
	public void moveRight(){
		currentIndex++;
		moveTabsToCurrentTab(currentIndex);
		flag = FLAG_MOVE_VIEWPAGER;
		postInvalidate();
	}
	/**
	 * tab����ƫ��
	 */
	public void moveLeft(){
		currentIndex--;
		moveTabsToCurrentTab(currentIndex);
		flag = FLAG_MOVE_VIEWPAGER;
		postInvalidate();
	}


	/**
	 * ��ʼ��������ǩ��λ��
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
	 * ��tabs�ƶ������
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
	 * ���ݵ�ǰѡ���ƶ�ÿһ����ǩ
	 * @param currentTab ��ǰѡ���ǩ
	 */
	public void moveTabsToCurrentTab(int currentTab){
		float moveDistance = countMoveDistance(currentTab);
		moveTabsToFirst();
		moveTabsWithDistance(moveDistance);
	}
	/**
	 * ���ݵ�ǰѡ�б�ǩ����ÿ����ǩ���ʼλ�õ��ƶ�����
	 * @param currentTab ��ǰѡ�б�ǩ
	 */
	public float countMoveDistance(int currentTab){
		float moveDistance = -tabDistance;
		for(int i = 0;i<currentTab;i++){
			moveDistance +=measureTextLength(tabTexts.get(i).text)+tabDistance;
		}
		return moveDistance;
	}
	
	/**
	 * �ƶ�tabs 
	 * @param distance �����������ƣ���������
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
	 * ȷ��viewpagerƫ�Ƶ����ʱ��ͼ��ƫ��λ��
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
	 * �û������¼����º���
	 * ʵ���ƶ�����ͷ��ѡ��ı��Tab�ļ���
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
			//�ж��û��Ƿ��ǵ���¼����ǵĻ��ж��û�����ĸ�item��������Tab����¼�����磩
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
			//�ж��ƶ�����
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
	 * �Զ��������࣬����һ��tabͷ
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
	 * �û�ѡ��ı��tabʱ��Ӧ�����İ�
	 */
	private OnTabClickListener tabClickListener;
	public void setOnTabClickListener(OnTabClickListener onTabClickListener){
		this.tabClickListener = onTabClickListener;
	}

	public interface OnTabClickListener{
		/**
		 * ���tab�������غ���
		 * @param index tab��ǩλ��
		 */
		public void onClickTab(int index);
	}

}
