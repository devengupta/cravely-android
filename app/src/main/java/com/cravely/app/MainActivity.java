package com.cravely.app;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.Bundle;
import android.view.*;
import android.view.animation.PathInterpolator;
import java.util.*;

public class MainActivity extends Activity {
    CravelyView view;
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.rgb(255,248,240));
        getWindow().setNavigationBarColor(Color.rgb(255,248,240));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        view = new CravelyView(this);
        setContentView(view);
    }
    @Override public void onBackPressed() {
        if (!view.goBack()) super.onBackPressed();
    }

    static class CravelyView extends View {
        static final int HOME=0, DISCOVER=1, TRY=2, DEALS=3, INSIGHTS=4, PROFILE=5, ORDER=6, FOOD_DETAIL=7;
        final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG); final Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        final RectF rr = new RectF(); final Path clip = new Path();
        float sx=1f, sy=1f, d;
        int W,H; int screen=HOME, from=HOME, to=HOME; float transition=1f;
        ValueAnimator navAnimator, pageAnimator, pressAnimator;
        float navPos=0f, pressScale=1f; int pressed=-1; float downX,downY;
        final int bg=Color.rgb(255,248,240), ink=Color.rgb(38,35,31), muted=Color.rgb(111,103,94), green=Color.rgb(10,72,55);
        final int mint=Color.rgb(216,240,228), peach=Color.rgb(255,220,183), pink=Color.rgb(249,222,228), lilac=Color.rgb(233,224,246), blue=Color.rgb(220,237,246), yellow=Color.rgb(252,236,191);
        final int red=Color.rgb(214,63,65), orange=Color.rgb(228,126,46);
        Bitmap paneer,pizza,rice,tacos,dealPizza,dealRamen,dealBiryani,dealFries,splash;
        String[] nav={"Home","Discover","Insights","Profile"};
        int[] navIcons={0,1,2,3};

        CravelyView(Context c){
            super(c); d=getResources().getDisplayMetrics().density;
            p.setTypeface(Typeface.create("sans",Typeface.NORMAL)); stroke.setStyle(Paint.Style.STROKE); stroke.setStrokeWidth(1.5f*d);
            paneer=load(com.cravely.app.R.drawable.food_paneer); pizza=load(R.drawable.food_pizza); rice=load(R.drawable.food_rice); tacos=load(R.drawable.food_tacos);
            dealPizza=load(R.drawable.deal_pizza); dealRamen=load(R.drawable.deal_ramen); dealBiryani=load(R.drawable.deal_biryani); dealFries=load(R.drawable.deal_fries); splash=load(R.drawable.splash);
            setBackgroundColor(bg); setFocusable(true);
        }
        Bitmap load(int id){return BitmapFactory.decodeResource(getResources(),id);}
        float X(float v){return v*d*sx;} float Y(float v){return v*d*sx;}
        float baseW(){return 390f;} float baseH(){return 760f;}
        @Override protected void onSizeChanged(int w,int h,int ow,int oh){W=w;H=h;sx=(w/d)/390f;}
        void fill(Canvas c,int color){p.setStyle(Paint.Style.FILL);p.setColor(color);c.drawRect(0,0,W,H,p);}
        void text(Canvas c,String s,float x,float y,float size,int color,boolean bold){p.setStyle(Paint.Style.FILL);p.setColor(color);p.setTextSize(X(size));p.setTypeface(Typeface.create("sans",bold?Typeface.BOLD:Typeface.NORMAL));c.drawText(s,X(x),Y(y),p);}
        void round(Canvas c,float l,float t,float r,float b,float rad,int color){p.setStyle(Paint.Style.FILL);p.setColor(color);rr.set(X(l),Y(t),X(r),Y(b));c.drawRoundRect(rr,X(rad),X(rad),p);}
        void outline(Canvas c,float l,float t,float r,float b,float rad,int color,float sw){stroke.setColor(color);stroke.setStrokeWidth(X(sw));stroke.setStyle(Paint.Style.STROKE);rr.set(X(l),Y(t),X(r),Y(b));c.drawRoundRect(rr,X(rad),X(rad),stroke);}
        void line(Canvas c,float x1,float y1,float x2,float y2,int color,float sw){stroke.setColor(color);stroke.setStrokeWidth(X(sw));stroke.setStyle(Paint.Style.STROKE);c.drawLine(X(x1),Y(y1),X(x2),Y(y2),stroke);}
        void pill(Canvas c,String s,float x,float y,float w,int color,int tc){round(c,x,y,x+w,y+28,14,color);text(c,s,x+12,y+19,12,tc,true);}

        @Override protected void onDraw(Canvas real){super.onDraw(real); if(W==0)return; Canvas c=real; fill(c,bg);
            if(transition<1f){
                c.save(); float out=(1-transition); float in=transition;
                c.translate(X(12*out),0); c.scale(0.985f+0.015f*transition,0.985f+0.015f*transition,W/2f,H/2f); p.setAlpha((int)(255*out)); drawScreen(c,from); p.setAlpha(255); c.restore();
                c.save(); c.translate(X(22*(1-in)),0); c.scale(0.985f+0.015f*in,0.985f+0.015f*in,W/2f,H/2f); p.setAlpha((int)(255*in)); drawScreen(c,to); p.setAlpha(255); c.restore();
            } else drawScreen(c,screen);
            p.setAlpha(255);
        }
        void drawScreen(Canvas c,int s){
            switch(s){case HOME:home(c);break;case DISCOVER:discover(c);break;case TRY:tryNext(c);break;case DEALS:deals(c);break;case INSIGHTS:insights(c);break;case PROFILE:profile(c);break;case ORDER:order(c);break;default:detail(c);}
            if(s!=ORDER && s!=FOOD_DETAIL) bottom(c);
        }

        void header(Canvas c,String title,String sub){text(c,"‹",18,43,35,ink,false);text(c,title,52,39,23,ink,true);if(sub!=null)text(c,sub,52,61,12,muted,false);}
        void home(Canvas c){
            text(c,"Good evening,",22,35,15,muted,false); text(c,"Deven 👋",22,64,31,ink,true);
            text(c,"I know your food habits.",22,90,16,ink,false); text(c,"What can I do for you today?",22,112,16,ink,false);
            round(c,22,126,368,176,24,Color.WHITE); text(c,"⌕",39,157,25,muted,false); text(c,"Ask me anything about food...",70,157,14,muted,false); text(c,"•••",337,156,15,green,true);
            homeCard(c,22,188,188,300,peach,"ORDER FOR ME","Let AI pick tonight’s dinner","⌁",0,pizza);
            homeCard(c,202,188,368,300,pink,"FIND MY NEXT FAV","5 dishes you’ll probably love","●",1,paneer);
            homeCard(c,22,312,188,424,blue,"WHAT SHOULD I TRY NEXT?","Step into a new cuisine","✦",2,rice);
            homeCard(c,202,312,368,424,yellow,"BEST DISCOUNTS FOR ME","Personalized deals you’ll actually like","%",3,dealPizza);
            homeCard(c,22,436,188,548,mint,"MY FOOD SPENDING","See your trends & insights","▥",4,null);
            homeCard(c,202,436,368,548,lilac,"MY FOOD PROFILE","Your taste DNA + food persona","☺",5,null);
            round(c,22,568,368,620,25,green);text(c,"✦",40,600,21,Color.WHITE,true);text(c,"Your taste in one line",75,591,12,Color.rgb(186,226,207),true);text(c,"“Familiar flavours. New adventures.”",75,609,14,Color.WHITE,false);
        }
        void homeCard(Canvas c,float l,float t,float r,float b,int col,String title,String sub,String icon,int target,Bitmap bm){
            float scale=(pressed==target?pressScale:1f); c.save(); c.scale(scale,scale,X((l+r)/2),Y((t+b)/2)); round(c,l,t,r,b,22,col); text(c,title,l+16,t+28,14,ink,true);
            // two-line subtitle for accurate density
            if(sub.length()>28){String[] a=sub.split(" ",3); String first=a[0]+" "+a[1]; text(c,first,l+16,t+50,11,muted,false); text(c,a[2],l+16,t+66,11,muted,false);} else text(c,sub,l+16,t+50,11,muted,false);
            round(c,l+16,b-38,l+52,b-10,15,Color.WHITE); text(c,"→",l+27,b-19,16,green,true);
            if(bm!=null) image(c,bm,r-70,t+43,r-10,t+103,18);
            else {p.setColor(Color.WHITE);p.setStyle(Paint.Style.FILL);c.drawCircle(X(r-36),Y(t+65),X(22),p);text(c,icon,r-46,t+72,19,green,true);}
            c.restore();
        }
        void image(Canvas c,Bitmap b,float l,float t,float r,float bot,float rad){if(b==null)return; c.save();clip.reset();clip.addRoundRect(new RectF(X(l),Y(t),X(r),Y(bot)),X(rad),X(rad),Path.Direction.CW);c.clipPath(clip);c.drawBitmap(b,null,new RectF(X(l),Y(t),X(r),Y(bot)),p);c.restore();}

        void topTitle(Canvas c,String title,String sub){header(c,title,sub);}
        void discover(Canvas c){
            round(c,0,0,390,112,0,green); text(c,"‹",18,43,35,Color.WHITE,false); text(c,"Your Next Favourite",52,39,23,Color.WHITE,true); text(c,"5 dishes you’ve probably never ordered, but might love",52,61,12,Color.rgb(211,235,225),false);
            pill(c,"All",22,76,43,Color.WHITE,green);pill(c,"Paneer picks",72,76,93,Color.rgb(39,93,77),Color.WHITE);pill(c,"Korean",170,76,67,Color.rgb(39,93,77),Color.WHITE);pill(c,"Asian",243,76,57,Color.rgb(39,93,77),Color.WHITE);
            foodItem(c,22,122,paneer,"Korean Gochujang Paneer Bowl","Nature’s Bowl","89% match","₹399",mint,"Spicy • Cheesy • Paneer",0);
            foodItem(c,22,226,pizza,"Truffle Mushroom Pizza","The Pizzeria Project","87% match","₹499",pink,"Rich • Umami • Comfort",1);
            foodItem(c,22,330,rice,"Thai Basil Paneer Rice Bowl","Bowl Theory","85% match","₹379",blue,"Basil • Spicy • Paneer",2);
            foodItem(c,22,434,tacos,"Peri Peri Cottage Cheese Tacos","The Taco Club","84% match","₹429",yellow,"Smoky • Crispy • New cuisine",3);
        }
        void foodItem(Canvas c,float x,float y,Bitmap bm,String name,String rest,String match,String price,int chip,String tags,int idx){
            float appear=transition<1?Math.min(1,transition*1.25f-idx*.06f):1; if(appear<0)appear=0; p.setAlpha((int)(255*appear));
            round(c,x,y,368,y+94,20,Color.WHITE); image(c,bm,x+9,y+9,x+91,y+85,17); text(c,name,x+103,y+27,14,ink,true); text(c,rest,x+103,y+47,11,muted,false); pill(c,match,x+103,y+57,75,mint,green); text(c,price,315,y+28,13,ink,true); text(c,tags,x+103,y+80,10,muted,false); text(c,"♡",340,y+79,17,muted,false); p.setAlpha(255);
        }
        void tryNext(Canvas c){
            topTitle(c,"What Should I Try Next?","Your next adventure, based on your taste");
            round(c,22,84,368,184,26,Color.WHITE); text(c,"Your next adventure",40,116,16,muted,false); text(c,"is Korean cuisine.",40,145,27,ink,true); text(c,"You love spicy food, paneer and bold flavours.",40,165,11,muted,false);pill(c,"Korean",40,180,67,green,Color.WHITE);pill(c,"Thai",114,180,52,Color.rgb(244,241,234),muted);pill(c,"Japanese",174,180,78,Color.rgb(244,241,234),muted);pill(c,"Mexican",258,180,74,Color.rgb(244,241,234),muted);
            round(c,22,202,368,330,24,Color.WHITE); text(c,"You’re ready for this",40,230,16,ink,true); String[] nodes={"Indian","Spicy","Paneer","Korean"}; for(int i=0;i<4;i++){float xx=50+i*88;p.setColor(i==3?red:green);c.drawCircle(X(xx),Y(266),X(5),p);if(i<3)line(c,xx+5,266,xx+83,266,Color.LTGRAY,2);text(c,nodes[i],xx-13,291,10,i==3?red:muted,i==3);}
            text(c,"“You don’t just eat. You explore.”",40,355,15,green,true); text(c,"Same you. New flavours.",40,378,13,muted,false);
            text(c,"Three bridges into Korean food",22,422,18,ink,true);smallRec(c,22,438,"Gochujang Paneer","Closest to what you already love",mint,"91%");smallRec(c,22,500,"Kimchi Cheese Fries","Spicy + cheesy comfort",pink,"86%");smallRec(c,22,562,"Korean Ramen","Late-night comfort",blue,"83%");
        }
        void smallRec(Canvas c,float x,float y,String a,String b,int col,String pct){round(c,x,y,368,y+52,18,Color.WHITE);round(c,x+10,y+9,x+52,y+43,12,col);text(c,"✦",x+21,y+31,15,green,true);text(c,a,x+64,y+22,13,ink,true);text(c,b,x+64,y+39,10,muted,false);text(c,pct,322,y+30,12,green,true);}
        void deals(Canvas c){
            topTitle(c,"Best Discounts for Me","Deals you’ll actually love. No noise."); round(c,22,82,368,139,22,pink);text(c,"Deals you’ll actually love.",40,109,16,red,true);text(c,"No noise, just the good stuff.",40,128,12,muted,false);pill(c,"All",22,151,43,green,Color.WHITE);pill(c,"Near you",72,151,68,Color.WHITE,ink);pill(c,"High match",146,151,80,Color.WHITE,ink);pill(c,"Under ₹300",234,151,90,Color.WHITE,ink);
            dealItem(c,22,193,dealPizza,"Paneer Tikka Pizza","The Pizzeria Project","₹599","₹399","33% OFF","92% match",mint);
            dealItem(c,22,278,dealRamen,"Korean Veg Ramen","Soulfully Yours","₹399","₹279","30% OFF","91% match",blue);
            dealItem(c,22,363,dealBiryani,"Mushroom Biryani","Biryani Blues","₹450","₹299","34% OFF","89% match",yellow);
            dealItem(c,22,448,dealFries,"Peri Peri Fries","Burger & Beyond","₹249","₹179","28% OFF","87% match",pink);
        }
        void dealItem(Canvas c,float x,float y,Bitmap bm,String name,String rest,String oldp,String newp,String off,String match,int chip){round(c,x,y,368,y+76,18,Color.WHITE);image(c,bm,x+10,y+8,x+78,y+68,15);text(c,name,x+90,y+23,13,ink,true);text(c,rest,x+90,y+41,10,muted,false);text(c,oldp,x+90,y+60,10,muted,false);text(c,newp,x+130,y+60,13,red,true);pill(c,off,278,y+8,74,mint,green);text(c,match,278,y+62,10,green,true);}
        void insights(Canvas c){
            topTitle(c,"My Food Spending","Your habits, patterns and little surprises"); text(c,"You spent",22,94,14,muted,false);text(c,"₹6,842",22,126,34,ink,true);text(c,"on food this month.",22,147,13,muted,false);pill(c,"↑ 12% from last month",22,158,122,mint,green);pill(c,"This Month⌄",286,88,82,Color.WHITE,muted);
            text(c,"Weekly spend",22,215,18,ink,true);float[] vals={45,70,60,92,126,82,111,70,90,118,103,145};for(int i=0;i<12;i++){float h=vals[i]*(transition<1?transition:1);int col=i>9?green:Color.rgb(93,145,125);round(c,28+i*27,348-h,47+i*27,348,6,col);}text(c,"W1",28,370,10,muted,false);text(c,"W2",108,370,10,muted,false);text(c,"W3",189,370,10,muted,false);text(c,"W4",270,370,10,muted,false);
            stat(c,22,395,104,"11","Orders");stat(c,143,395,104,"₹641","Average order");stat(c,264,395,104,"₹1,384","Largest order");text(c,"Where does your money go?",22,494,18,ink,true);round(c,22,510,368,632,24,Color.WHITE); // donut
            p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(X(18));p.setStrokeCap(Paint.Cap.BUTT);RectF d=new RectF(X(55),Y(530),X(165),Y(640));float a=0;int[] cols={green,Color.rgb(244,164,55),Color.rgb(84,115,192),Color.rgb(185,86,134),Color.rgb(125,104,171)};float[] sw={115,65,50,42,38};for(int i=0;i<5;i++){stroke.setColor(cols[i]);c.drawArc(d,a,sw[i],false,stroke);a+=sw[i];}stroke.setStyle(Paint.Style.STROKE);text(c,"Indian",202,546,11,muted,false);text(c,"32%",322,546,11,ink,true);text(c,"Pizza",202,570,11,muted,false);text(c,"18%",322,570,11,ink,true);text(c,"Biryani",202,594,11,muted,false);text(c,"14%",322,594,11,ink,true);text(c,"Fast food",202,618,11,muted,false);text(c,"13%",322,618,11,ink,true);
        }
        void stat(Canvas c,float x,float y,float w,String a,String b){round(c,x,y,x+w,y+66,18,Color.WHITE);text(c,a,x+12,y+29,18,ink,true);text(c,b,x+12,y+49,10,muted,false);}
        void profile(Canvas c){
            topTitle(c,"My Food Profile","Your taste DNA + a little personality");text(c,"Share",338,38,11,muted,true);round(c,22,82,368,194,28,peach);text(c,"👑  The Masala Explorer",40,119,22,ink,true);text(c,"You love taking things you already love —",40,142,11,muted,false);text(c,"paneer, cheese, spice and comfort food —",40,158,11,muted,false);pill(c,"Comfort food",40,170,91,Color.WHITE,ink);pill(c,"Spicy",138,170,55,Color.WHITE,ink);pill(c,"Curious",201,170,65,Color.WHITE,ink);
            text(c,"Your Food DNA",22,229,19,ink,true);dna(c,"Spicy",260,.91f,red);dna(c,"Cheesy",294,.83f,Color.rgb(242,170,12));dna(c,"Paneer",328,.94f,Color.rgb(242,170,12));dna(c,"Indian flavours",362,.90f,green);dna(c,"Smoky",396,.78f,orange);dna(c,"Biryani",430,.87f,Color.rgb(199,103,46));dna(c,"Novelty",464,.64f,Color.rgb(131,95,183));round(c,22,492,368,582,22,Color.WHITE);text(c,"Your food philosophy",40,519,13,muted,true);text(c,"“Give me something new,",40,544,17,ink,true);text(c,"but make sure I recognise at least one thing I love.”",40,564,12,ink,false);text(c,"“Familiar flavours. New adventures.”",40,607,13,green,true);
        }
        void dna(Canvas c,String n,float y,float v,int col){text(c,n,22,y,11,ink,false);round(c,118,y-12,118+204*v,y+2,7,col);text(c,(int)(v*100)+"%",334,y,11,muted,true);}
        void order(Canvas c){
            header(c,"Order for Me",null);text(c,"Dinner, just got smarter",260,40,10,orange,true);text(c,"Tonight, I’d order",22,96,28,ink,true);text(c,"this for you.",22,128,28,ink,true);text(c,"Based on your taste, past orders, current deals and the time of day.",22,151,11,muted,false);round(c,22,166,368,304,24,Color.WHITE);image(c, pizza,30,174,360,294,20);pill(c,"89% match",284,178,76,mint,green);text(c,"Korean Veg Ramen Bowl",22,330,20,ink,true);text(c,"Soulfully Yours",22,351,11,muted,false);text(c,"₹399",22,381,12,muted,false);text(c,"₹279",61,381,17,red,true);pill(c,"30% OFF",118,364,70,mint,green);String[] rs={"Matches your spicy + rich + comfort food taste","You haven’t ordered it before","Great value today","Perfect for a late-evening meal"};for(int i=0;i<4;i++){text(c,"✓",22,409+i*21,13,green,true);text(c,rs[i],42,409+i*21,11,muted,false);}round(c,22,504,368,554,25,green);text(c,"Add to Cart on Zomato  →",78,535,14,Color.WHITE,true);outline(c,22,566,368,616,25,Color.rgb(190,185,178),1.2f);text(c,"⟳  Show Me More Options",84,598,13,ink,true);
        }
        void detail(Canvas c){
            header(c,"Korean Gochujang Paneer Bowl","Nature’s Bowl  •  4.4 ★");image(c,paneer,22,82,368,330,26);pill(c,"89% match",278,96,80,mint,green);text(c,"Korean Gochujang Paneer Bowl",22,365,22,ink,true);text(c,"Spicy • Cheesy • Paneer",22,388,12,muted,false);text(c,"₹399",22,421,13,muted,false);text(c,"₹329",61,421,20,red,true);pill(c,"New for you",120,403,82,mint,green);round(c,22,452,368,514,25,green);text(c,"Add to cart",153,490,15,Color.WHITE,true);round(c,22,530,368,580,25,Color.WHITE);outline(c,22,530,368,580,25,green,1.2f);text(c,"Why this matches you",107,562,14,green,true);
        }
        void bottom(Canvas c){
            float y=690; round(c,0,y,390,760,0,bg);line(c,0,y,390,y,Color.rgb(235,226,216),1);
            float target=screen==HOME?0:screen==DISCOVER||screen==TRY||screen==DEALS?1:screen==INSIGHTS?2:3; if(navAnimator!=null && navAnimator.isRunning()) target=navPos;
            for(int i=0;i<4;i++){float x=48+i*98;boolean active=Math.abs(target-i)<.5f;float bump=active?1.05f:1f; c.save();c.scale(bump,bump,X(x),Y(y+28));text(c,i==0?"⌂":i==1?"✦":i==2?"▥":"☺",x-8,y+28,19,active?green:muted,true);c.restore();text(c,nav[i],x-17,y+50,10,active?green:muted,active);}
        }

        void startTransition(int dest){if(dest==screen && transition>=1)return; from=screen;to=dest;screen=dest;transition=0f; if(pageAnimator!=null)pageAnimator.cancel();pageAnimator=ValueAnimator.ofFloat(0f,1f);pageAnimator.setDuration(dest==FOOD_DETAIL?360:300);pageAnimator.setInterpolator(new PathInterpolator(0.4f,0f,0.2f,1f));pageAnimator.addUpdateListener(a->{transition=(float)a.getAnimatedValue();invalidate();});pageAnimator.start();}
        void animatePress(final int which){pressed=which;pressScale=1f; if(pressAnimator!=null)pressAnimator.cancel();pressAnimator=ValueAnimator.ofFloat(1f,.975f,1f);pressAnimator.setDuration(170);pressAnimator.setInterpolator(new PathInterpolator(0.4f,0f,0.2f,1f));pressAnimator.addUpdateListener(a->{pressScale=(float)a.getAnimatedValue();invalidate();});pressAnimator.start();}
        int hitHome(float x,float y){if(y>=188&&y<300)return x<195?0:1;if(y>=312&&y<424)return x<195?2:3;if(y>=436&&y<548)return x<195?4:5;return -1;}
        @Override public boolean onTouchEvent(MotionEvent e){float x=e.getX()/(d*sx),y=e.getY()/(d*sx); if(e.getAction()==MotionEvent.ACTION_DOWN){downX=x;downY=y;return true;} if(e.getAction()!=MotionEvent.ACTION_UP)return true;
            float dy=y-downY; if(Math.abs(dy)>35){return true;}
            if(screen!=ORDER && screen!=FOOD_DETAIL && y>665){int idx=(int)(x/97.5f);if(idx<0)idx=0;if(idx>3)idx=3;startTransition(idx==0?HOME:idx==1?DISCOVER:idx==2?INSIGHTS:PROFILE);return true;}
            if(screen==HOME){int h=hitHome(x,y);if(h>=0){animatePress(h);startTransition(h==0?ORDER:h==1?DISCOVER:h==2?TRY:h==3?DEALS:h==4?INSIGHTS:PROFILE);return true;}}
            if(screen==DISCOVER && y>=122&&y<528){startTransition(FOOD_DETAIL);return true;}
            if(screen==TRY && y>=438&&y<630){startTransition(DISCOVER);return true;}
            if(screen==DEALS && y>=193&&y<530){startTransition(FOOD_DETAIL);return true;}
            if(screen==PROFILE && y<75){startTransition(HOME);return true;}
            if(screen==ORDER && y>=504&&y<620){startTransition(FOOD_DETAIL);return true;}
            if(screen==FOOD_DETAIL && y<70){startTransition(DISCOVER);return true;}
            return true;
        }
        boolean goBack(){if(screen==HOME)return false;int dest=(screen==DISCOVER||screen==TRY||screen==DEALS||screen==INSIGHTS||screen==PROFILE)?HOME:(screen==FOOD_DETAIL?DISCOVER:HOME);startTransition(dest);return true;}
    }
}
