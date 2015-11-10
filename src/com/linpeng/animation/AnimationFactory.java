package com.linpeng.animation;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimationFactory {
	
	public static AlphaAnimation getAlphaAnimation(float fromAlpha,float toAlpha,int durationMillis){
		AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
		alphaAnimation.setDuration(durationMillis);
		return alphaAnimation;
	}

	public static TranslateAnimation getTranslateAnimation(float fromXDelta,float toXDelta,float fromYDelta,float toYDelta,int durationMillis){
		TranslateAnimation translateAnimation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
		translateAnimation.setDuration(durationMillis);
		return translateAnimation;
	}

	public static ScaleAnimation getCenterScaleAnimation(float fromX,float toX,float fromY,float toY,int durationMillis){
		ScaleAnimation scaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(durationMillis);
		return scaleAnimation;
	}
	public static ScaleAnimation getScaleAnimation(float fromX,float toX,float fromY,float toY,float pivotX,float pivotY,int durationMillis){
		ScaleAnimation scaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY);
		scaleAnimation.setDuration(durationMillis);
		return scaleAnimation;
	}

	public static RotateAnimation getCenterRotateAnimation(float fromDegrees,float toDegrees,int durationMillis){
		RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees,toDegrees, Animation.RELATIVE_TO_SELF, 0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(durationMillis);
		return rotateAnimation;
	}
	public static RotateAnimation getRotateAnimation(float fromDegrees,float toDegrees,float pivotX,float pivotY, int durationMillis){
		RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees,toDegrees, Animation.RELATIVE_TO_SELF, pivotX,  Animation.RELATIVE_TO_SELF, pivotY);
		rotateAnimation.setDuration(durationMillis);
		return rotateAnimation;
	}

	public static AnimationSet getAnimationSet(Animation... animations){
		AnimationSet animationSet = new AnimationSet(false);
		for(Animation animation : animations){
			animationSet.addAnimation(animation);
		}
		return animationSet;
	}


}
