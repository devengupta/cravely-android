package com.cravely.app;
import android.content.Context; import android.graphics.*; import android.util.AttributeSet; import android.view.View;
public class BarView extends View {
    float value=0f; int color=0xFF0A4837; final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG); final RectF r=new RectF(); float anim=0f;
    public BarView(Context c){super(c);} public BarView(Context c,AttributeSet a){super(c,a);}
    public void set(float v,int col){value=v;color=col;anim=0f;animate().cancel();post(tick);}
    final Runnable tick=new Runnable(){public void run(){anim=Math.min(1f,anim+0.08f);invalidate();if(anim<1f)postDelayed(this,16);}};
    @Override protected void onDraw(Canvas c){float h=getHeight(),w=getWidth();float rad=h/2;p.setColor(0xFFF1ECE4);r.set(0,0,w,h);c.drawRoundRect(r,rad,rad,p);p.setColor(color);float e=1-(float)Math.pow(1-anim,3);r.set(0,0,Math.max(h,w*value*e),h);c.drawRoundRect(r,rad,rad,p);}
}
