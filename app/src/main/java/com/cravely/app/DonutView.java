package com.cravely.app;
import android.content.Context; import android.graphics.*; import android.util.AttributeSet; import android.view.View;
public class DonutView extends View {
    public float[] values={32,18,14,13,9,14}; public int[] colors={0xFF0A4837,0xFFE47E2E,0xFFF2AA0C,0xFFD63F41,0xFF7E68AC,0xFFC98F7A};
    final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG); final RectF r=new RectF(); float anim=0f;
    public DonutView(Context c){super(c);} public DonutView(Context c,AttributeSet a){super(c,a);}
    @Override protected void onAttachedToWindow(){super.onAttachedToWindow();anim=0f;post(tick);}
    final Runnable tick=new Runnable(){public void run(){anim=Math.min(1f,anim+0.05f);invalidate();if(anim<1f)postDelayed(this,16);}};
    @Override protected void onDraw(Canvas c){float w=getWidth(),h=getHeight();float d=Math.min(w,h);float ring=d*0.3f;r.set((w-d)/2+ring/2,(h-d)/2+ring/2,(w+d)/2-ring/2,(h+d)/2-ring/2);
        p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(ring);float total=0;for(float v:values)total+=v;float e=1-(float)Math.pow(1-anim,3);float start=-90,sweepTotal=360*e;
        for(int i=0;i<values.length;i++){float s=360*values[i]/total;float draw=Math.max(0,Math.min(s,sweepTotal));p.setColor(colors[i]);c.drawArc(r,start,draw-1.5f,false,p);start+=s;sweepTotal-=s;if(sweepTotal<=0)break;}}
}
