package com.cravely.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.content.*;
import java.util.*;

public class MainActivity extends Activity {
  @Override public void onCreate(Bundle b){ super.onCreate(b); getWindow().setStatusBarColor(Color.rgb(255,248,240)); getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); setContentView(new CravelyView(this)); }

  static class CravelyView extends View {
    Paint p=new Paint(3); Paint stroke=new Paint(3); int screen=0; float d; int W,H; RectF r=new RectF();
    int bg=Color.rgb(255,248,240), ink=Color.rgb(35,34,31), muted=Color.rgb(112,105,96), green=Color.rgb(15,69,54), mint=Color.rgb(218,240,229), peach=Color.rgb(255,218,179), pink=Color.rgb(248,221,225), lilac=Color.rgb(232,224,246), blue=Color.rgb(220,237,246), yellow=Color.rgb(251,235,190), red=Color.rgb(213,63,65);
    String[] nav={"Home","Discover","Insights","Profile"};
    CravelyView(Context c){super(c); d=getResources().getDisplayMetrics().density; p.setTypeface(Typeface.create("sans",Typeface.NORMAL)); stroke.setStyle(Paint.Style.STROKE); stroke.setStrokeWidth(2*d); setBackgroundColor(bg);}
    float S(float x){return x*d;} void text(Canvas c,String s,float x,float y,float size,int color,boolean bold){p.setStyle(Paint.Style.FILL);p.setColor(color);p.setTextSize(S(size));p.setTypeface(Typeface.create("sans",bold?Typeface.BOLD:Typeface.NORMAL));c.drawText(s,S(x),S(y),p);}
    void round(Canvas c,float l,float t,float rr,float b,float rad,int color){p.setStyle(Paint.Style.FILL);p.setColor(color);r.set(S(l),S(t),S(rr),S(b));c.drawRoundRect(r,S(rad),S(rad),p);}
    void line(Canvas c,float x1,float y1,float x2,float y2,int color,float sw){stroke.setColor(color);stroke.setStrokeWidth(S(sw));c.drawLine(S(x1),S(y1),S(x2),S(y2),stroke);}
    void pill(Canvas c,String s,float x,float y,float w,int color,int tc){round(c,x,y,x+w,y+28,14,color);text(c,s,x+12,y+19,12,tc,true);}
    @Override protected void onDraw(Canvas c){super.onDraw(c);W=getWidth();H=getHeight();c.drawColor(bg); if(screen==0)home(c); else if(screen==1)next(c); else if(screen==2)tryNext(c); else if(screen==3)deals(c); else if(screen==4)insights(c); else if(screen==5)profile(c); else order(c); if(screen!=6)bottom(c);}
    void header(Canvas c,String title,String sub){text(c,"‹",18,42,34,ink,false);text(c,title,52,38,22,ink,true);if(sub!=null)text(c,sub,52,59,12,muted,false);}
    void home(Canvas c){
      text(c,"Good evening,",22,37,15,muted,false);text(c,"Deven 👋",22,66,30,ink,true);
      text(c,"I know your food habits.",22,92,16,ink,false);text(c,"What can I do for you today?",22,113,16,ink,false);
      round(c,22,128,W/d-22,176,24,Color.WHITE);text(c,"⌕",38,158,24,muted,false);text(c,"Ask me anything about food...",70,158,14,muted,false);text(c,"•••",W/d-62,157,15,green,true);
      card(c,22,190,W/d/2-8,302,peach,"ORDER FOR ME","Let me pick tonight’s dinner","→",7);
      card(c,W/d/2+8,190,W/d-22,302,pink,"FIND MY NEXT FAV","5 dishes you’ll probably love","→",1);
      card(c,22,314,W/d/2-8,426,blue,"WHAT SHOULD I TRY NEXT?","Step into a new cuisine","→",2);
      card(c,W/d/2+8,314,W/d-22,426,yellow,"BEST DISCOUNTS FOR ME","Deals you’ll actually like","→",3);
      card(c,22,438,W/d/2-8,550,mint,"MY FOOD SPENDING","See your trends & insights","→",4);
      card(c,W/d/2+8,438,W/d-22,550,lilac,"MY FOOD PROFILE","Your taste DNA + persona","→",5);
      round(c,22,568,W/d-22,620,25,green);text(c,"✦",40,601,21,Color.WHITE,true);text(c,"Your taste in one line",76,592,12,Color.rgb(185,226,207),true);text(c,"“Familiar flavours. New adventures.”",76,610,14,Color.WHITE,false);
    }
    void card(Canvas c,float l,float t,float rr,float b,int color,String title,String sub,String arrow,int target){round(c,l,t,rr,b,22,color);text(c,title,l+16,t+29,14,ink,true);text(c,sub,l+16,t+51,12,muted,false);round(c,l+16,b-38,l+52,b-10,15,Color.WHITE);text(c,arrow,l+27,b-19,16,green,true); // little food illustration
      p.setColor(Color.WHITE);p.setStyle(Paint.Style.FILL);c.drawCircle(S(rr-30),S(t+58),S(20),p);text(c,target==5?"☺":target==4?"▥":target==3?"%":target==2?"✦":target==1?"●":"⌁",rr-39,t+65,20,green,true);
    }
    void topTitle(Canvas c,String title,String sub){text(c,"‹",18,44,34,ink,false);text(c,title,52,39,23,ink,true);text(c,sub,52,62,13,muted,false);}
    void next(Canvas c){topTitle(c,"Your Next Favourite","5 dishes you’ve probably never ordered, but might love");pill(c,"All",22,78,43,green,Color.WHITE);pill(c,"Paneer picks",72,78,93,Color.WHITE,ink);pill(c,"Korean",170,78,67,Color.WHITE,ink);pill(c,"Asian",243,78,57,Color.WHITE,ink);
      item(c,22,120,"Korean Gochujang Paneer Bowl","Nature’s Bowl","89% match","₹399",mint,"Spicy • Cheesy • Paneer");
      item(c,22,226,"Truffle Mushroom Pizza","The Pizzeria Project","87% match","₹499",pink,"Rich • Umami • Comfort");
      item(c,22,332,"Thai Basil Paneer Rice Bowl","Bowl Theory","85% match","₹379",blue,"Basil • Spicy • Paneer");
      item(c,22,438,"Peri Peri Cottage Cheese Tacos","The Taco Club","84% match","₹429",yellow,"Smoky • Crispy • New cuisine");
    }
    void item(Canvas c,float x,float y,String name,String rest,String match,String price,int chip,String tags){round(c,x,y,W/d-22,y+92,20,Color.WHITE);round(c,x+10,y+10,x+84,y+82,18,chip);text(c,"FOOD",x+22,y+51,11,green,true);text(c,name,x+96,y+29,15,ink,true);text(c,rest,x+96,y+49,11,muted,false);pill(c,match,x+96,y+57,74,mint,green);text(c,price,W/d-73,y+31,13,ink,true);text(c,tags,x+96,y+80,10,muted,false);}
    void tryNext(Canvas c){topTitle(c,"What Should I Try Next?","Your next step, based on your taste graph");round(c,22,84,W/d-22,186,26,green);text(c,"YOUR NEXT ADVENTURE",40,113,11,Color.rgb(170,225,201),true);text(c,"Korean cuisine",40,146,27,Color.WHITE,true);text(c,"You already love spicy + paneer + bold flavours.",40,168,12,Color.WHITE,false);pill(c,"Korean",40,198,67,Color.WHITE,green);pill(c,"Thai",114,198,52,Color.WHITE,green);pill(c,"Japanese",174,198,78,Color.WHITE,green);
      round(c,22,208,W/d-22,350,24,Color.WHITE);text(c,"You’re ready for this",40,240,17,ink,true);String[] nodes={"Indian","Spicy","Paneer","Korean"};for(int i=0;i<4;i++){float xx=40+i*82; p.setColor(i==3?red:green);c.drawCircle(S(xx),S(277),S(5),p);if(i<3)line(c,xx+5,277,xx+77,277,Color.LTGRAY,2);text(c,nodes[i],xx-2,302,10,i==3?red:muted,i==3);}text(c,"“You don’t just eat. You explore.”",40,332,15,green,true);
      text(c,"Three bridges into Korean food",22,382,18,ink,true);smallRec(c,22,402,"Gochujang Paneer","Closest to what you already love",mint,"91%");smallRec(c,22,466,"Kimchi Cheese Fries","Spicy + cheesy comfort",pink,"86%");smallRec(c,22,530,"Korean Ramen","Late-night comfort",blue,"83%");
    }
    void smallRec(Canvas c,float x,float y,String a,String b,int col,String pct){round(c,x,y,W/d-22,y+54,18,Color.WHITE);round(c,x+10,y+9,x+52,y+45,12,col);text(c,"✦",x+21,y+33,15,green,true);text(c,a,x+64,y+23,13,ink,true);text(c,b,x+64,y+41,10,muted,false);text(c,pct,x+W/d-82,y+31,13,green,true);}
    void deals(Canvas c){topTitle(c,"Best Discounts for Me","Deals you’ll actually care about");round(c,22,82,W/d-22,139,22,pink);text(c,"No noise. Just the good stuff.",40,112,16,ink,true);text(c,"Personalized by taste match + discount.",40,130,11,muted,false);pill(c,"All",22,151,43,green,Color.WHITE);pill(c,"Near you",72,151,68,Color.WHITE,ink);pill(c,"Under ₹300",146,151,90,Color.WHITE,ink);deal(c,22,192,"Paneer Tikka Pizza","₹599","₹399","33% OFF","92% match",mint);deal(c,22,272,"Korean Veg Ramen","₹399","₹279","30% OFF","91% match",blue);deal(c,22,352,"Mushroom Biryani","₹450","₹299","34% OFF","89% match",yellow);deal(c,22,432,"Peri Peri Fries","₹249","₹179","28% OFF","87% match",pink);}
    void deal(Canvas c,float x,float y,String n,String oldp,String pnew,String off,String match,int col){round(c,x,y,W/d-22,y+68,18,Color.WHITE);round(c,x+10,y+10,x+58,y+58,14,col);text(c,"FOOD",x+16,y+39,9,green,true);text(c,n,x+70,y+25,14,ink,true);text(c,oldp,x+70,y+46,10,muted,false);text(c,pnew,x+107,y+46,13,red,true);pill(c,off,x+W/d-116,y+10,72,mint,green);text(c,match,x+W/d-116,y+50,11,green,true);}
    void insights(Canvas c){topTitle(c,"My Food Spending","Your habits, patterns and little surprises");text(c,"You spent",22,94,14,muted,false);text(c,"₹6,842",22,126,34,ink,true);text(c,"on food this month",22,147,13,muted,false);pill(c,"↑ 12% from last month",22,158,122,mint,green);text(c,"Weekly spend",22,211,18,ink,true);float[] vals={45,72,61,98,130,84,112,72,91,118,105,145};for(int i=0;i<12;i++){float h=vals[i];round(c,28+i*25,350-h,45+i*25,350,7,i>9?green:Color.rgb(92,145,125));}line(c,22,350,W/d-22,350,Color.LTGRAY,1);text(c,"W1",25,370,10,muted,false);text(c,"W2",103,370,10,muted,false);text(c,"W3",181,370,10,muted,false);text(c,"W4",259,370,10,muted,false);
      stat(c,22,398,112,"11","Orders");stat(c,130,398,112,"₹641","Avg order");stat(c,238,398,112,"₹1,384","Largest");text(c,"Where does your money go?",22,535,18,ink,true);round(c,22,552,W/d-22,630,22,Color.WHITE);text(c,"Indian",44,580,12,ink,false);text(c,"32%",W/d-62,580,12,green,true);text(c,"Pizza",44,603,12,ink,false);text(c,"18%",W/d-62,603,12,green,true);text(c,"Biryani",160,580,12,ink,false);text(c,"14%",W/d-62,603,12,green,true);text(c,"Fast food",160,603,12,ink,false);text(c,"13%",W/d-62,626,12,green,true);
    }
    void stat(Canvas c,float x,float y,float w,String a,String b){round(c,x,y,x+w,y+66,18,Color.WHITE);text(c,a,x+12,y+29,18,ink,true);text(c,b,x+12,y+49,10,muted,false);}
    void profile(Canvas c){topTitle(c,"My Food Profile","Your taste DNA + a little personality");round(c,22,82,W/d-22,192,28,peach);text(c,"👑  The Masala Explorer",40,119,22,ink,true);text(c,"You love familiar flavours in new forms.",40,144,12,muted,false);pill(c,"Comfort food",40,154,91,Color.WHITE,ink);pill(c,"Spicy",138,154,55,Color.WHITE,ink);pill(c,"Curious",201,154,65,Color.WHITE,ink);
      text(c,"Your Food DNA",22,226,19,ink,true);dna(c,"Paneer",94,0.94,red);dna(c,"Spicy",138,0.91,red);dna(c,"Indian flavours",182,0.90,green);dna(c,"Biryani",226,0.87,Color.rgb(204,114,47));dna(c,"Cheesy",270,0.83,Color.rgb(240,165,15));dna(c,"Smoky",314,0.78,Color.rgb(121,93,70));dna(c,"Novelty",358,0.64,Color.rgb(132,95,183));round(c,22,390,W/d-22,470,22,Color.WHITE);text(c,"Your food philosophy",40,418,13,muted,true);text(c,"“Give me something new,",40,442,17,ink,true);text(c,"but make sure I recognise at least one thing I love.”",40,462,13,ink,false);text(c,"Your usual order time",22,510,17,ink,true);text(c,"8:30 PM – 11:30 PM",22,540,24,green,true);text(c,"Late-evening explorer",22,560,11,muted,false);}
    void dna(Canvas c,String n,float y,float v,int col){text(c,n,22,y,12,ink,false);round(c,116,y-13,116+210*v,y+1,7,col);text(c,(int)(v*100)+"%",W/d-58,y,11,muted,true);}
    void order(Canvas c){text(c,"‹",18,44,34,ink,false);text(c,"Order for Me",52,39,23,ink,true);text(c,"Based on your taste, past orders, deals and time of day.",52,62,11,muted,false);text(c,"Tonight, I’d order",22,104,28,ink,true);text(c,"this for you.",22,137,28,ink,true);round(c,22,156,W/d-22,278,26,green);text(c,"89% TASTE MATCH",42,184,11,Color.rgb(190,232,210),true);text(c,"Korean Veg Ramen Bowl",42,221,22,Color.WHITE,true);text(c,"Spicy · Rich · Comfort · New",42,245,12,Color.WHITE,false);pill(c,"₹399 → ₹279",42,256,105,Color.WHITE,green);round(c,22,298,W/d-22,414,24,Color.WHITE);text(c,"Why I picked it",42,327,17,ink,true);String[] reasons={"Matches your spicy + rich taste","You haven’t ordered it before","Great deal today","Perfect for a late-evening meal"};for(int i=0;i<4;i++){text(c,"✓",42,354+i*19,13,green,true);text(c,reasons[i],62,354+i*19,11,muted,false);}round(c,22,432,W/d-22,482,25,green);text(c,"Add to Cart on Zomato  →",60,463,15,Color.WHITE,true);round(c,22,494,W/d-22,544,25,Color.WHITE);stroke.setColor(green);stroke.setStyle(Paint.Style.STROKE);c.drawRoundRect(new RectF(S(22),S(494),S(W/d-22),S(544)),S(25),S(25),stroke);text(c,"Show me more options",70,525,14,green,true);}
    void bottom(Canvas c){float y=H/d-68;round(c,0,y,W/d,H/d,bg);line(c,0,y,W/d,y,Color.rgb(235,226,216),1);for(int i=0;i<4;i++){float x=40+i*(W/d-80)/3;boolean active=(screen==0&&i==0)||(screen==1||screen==2&&i==1)||(screen==4&&i==2)||(screen==5&&i==3);text(c,i==0?"⌂":i==1?"✦":i==2?"▥":"☺",x-7,y+26,19,active?green:muted,true);text(c,nav[i],x-18,y+47,10,active?green:muted,active);}}
    @Override public boolean onTouchEvent(android.view.MotionEvent e){if(e.getAction()!=MotionEvent.ACTION_UP)return true;float x=e.getX()/d,y=e.getY()/d;float h=H/d;
      if(screen!=6 && y>h-80){if(x<W/4)screen=0;else if(x<W/2)screen=1;else if(x<3*W/4)screen=4;else screen=5;invalidate();return true;}
      if(screen==0){if(y>=190&&y<302){screen=(x<W/d/2)?6:1;}else if(y>=314&&y<426){screen=(x<W/d/2)?2:3;}else if(y>=438&&y<550){screen=(x<W/d/2)?4:5;}}
      else if(screen==1 && y>120&&y<620)screen=6;
      else if(screen==2 && y>395)screen=1;
      else if(screen==3 && y>190)screen=6;
      else if(screen==5 && y<80)screen=0;
      else if(screen==6 && y>420&&y<490)screen=0;
      invalidate();return true;
    }
  }
}
