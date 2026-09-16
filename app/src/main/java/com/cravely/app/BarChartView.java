package com.cravely.app;
import android.content.Context; import android.graphics.*; import android.util.AttributeSet; import android.view.View;
public class BarChartView extends View {
    float[] vals={450,720,610,980,1300,840,1120,720,910,1180,1050,1450}; String[] weeks={"W1","W2","W3","W4"};
    final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG); final Paint t=new Paint(Paint.ANTI_ALIAS_FLAG); final RectF r=new RectF(); float anim=0f; float d;
    public BarChartView(Context c){this(c,null);} public BarChartView(Context c,AttributeSet a){super(c,a);d=getResources().getDisplayMetrics().density;t.setColor(0xFF6F675E);t.setTextSize(10*d);}
    @Override protected void onAttachedToWindow(){super.onAttachedToWindow();anim=0f;post(tick);}
    final Runnable tick=new Runnable(){public void run(){anim=Math.min(1f,anim+0.06f);invalidate();if(anim<1f)postDelayed(this,16);}};
    @Override protected void onDraw(Canvas c){float w=getWidth(),h=getHeight();float left=44*d,bottom=h-22*d,top=8*d;float max=2000;
        t.setTextAlign(Paint.Align.RIGHT);p.setStrokeWidth(1);
        for(int i=0;i<=4;i++){float y=bottom-(bottom-top)*i/4f;p.setColor(0xFFEBE2D8);c.drawLine(left,y,w,y,p);String lbl=i==0?"₹0":"₹"+(i*500>=1000?String.format("%d,%03d",i*500/1000,i*500%1000):""+i*500);c.drawText(lbl,left-6*d,y+4*d,t);}
        float e=1-(float)Math.pow(1-anim,3);float n=vals.length;float gap=5*d;float bw=((w-left)-gap*(n+1))/n;
        for(int i=0;i<n;i++){float x=left+gap+i*(bw+gap);float bh=(bottom-top)*vals[i]/max*e;p.setColor(i>=9?0xFF0A4837:0xFF5D9179);r.set(x,bottom-bh,x+bw,bottom);c.drawRoundRect(r,3*d,3*d,p);}
        t.setTextAlign(Paint.Align.CENTER);for(int k=0;k<4;k++){float x=left+gap+(k*3+1.5f)*(bw+gap);c.drawText(weeks[k],x,h-6*d,t);}}
}
