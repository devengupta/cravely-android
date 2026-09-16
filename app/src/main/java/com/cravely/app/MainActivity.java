package com.cravely.app;

import android.animation.*;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.*;
import android.view.animation.PathInterpolator;
import android.widget.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class MainActivity extends Activity {
    static final int HOME=0, DISCOVER=1, TRY=2, DEALS=3, INSIGHTS=4, PROFILE=5, ORDER=6;
    FrameLayout container, splashHolder; View current; int screen=-1;
    final Deque<Integer> back=new ArrayDeque<>();
    final PathInterpolator ease=new PathInterpolator(0.4f,0f,0.2f,1f);
    int[] navIds={R.id.nav_home,R.id.nav_discover,R.id.nav_insights,R.id.nav_profile};
    int[] navIcons={R.id.nav_home_i,R.id.nav_discover_i,R.id.nav_insights_i,R.id.nav_profile_i};
    int[] navTexts={R.id.nav_home_t,R.id.nav_discover_t,R.id.nav_insights_t,R.id.nav_profile_t};
    int[] navTargets={HOME,DISCOVER,INSIGHTS,PROFILE};

    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        container=findViewById(R.id.container); splashHolder=findViewById(R.id.splash_holder);
        for(int i=0;i<4;i++){final int t=navTargets[i];findViewById(navIds[i]).setOnClickListener(v->{back.clear();show(t,false);});}
        show(HOME,false);
        showSplash();
    }

    void showSplash(){
        View s=getLayoutInflater().inflate(R.layout.screen_splash,splashHolder,false);
        splashHolder.addView(s);
        s.findViewById(R.id.start).setOnClickListener(v->s.animate().alpha(0f).setDuration(400).setInterpolator(ease).withEndAction(()->{splashHolder.removeAllViews();splashHolder.setVisibility(View.GONE);}).start());
        getWindow().getDecorView().setSystemUiVisibility(0);
    }

    void show(int which,boolean pushBack){
        if(which==screen) return;
        if(pushBack && screen>=0) back.push(screen);
        View next=build(which);
        final View old=current; current=next; screen=which;
        container.addView(next);
        next.setAlpha(0f); next.setTranslationX(pushBack?dp(24):0); next.setScaleX(0.985f); next.setScaleY(0.985f);
        next.animate().alpha(1f).translationX(0).scaleX(1f).scaleY(1f).setDuration(320).setInterpolator(ease).start();
        if(old!=null) old.animate().alpha(0f).translationX(pushBack?-dp(12):0).setDuration(220).withEndAction(()->container.removeView(old)).start();
        updateNav(which); updateStatusBar(which);
    }

    void updateNav(int which){
        boolean hide=(which==ORDER);
        View nav=findViewById(R.id.nav); nav.setVisibility(hide?View.GONE:View.VISIBLE);
        int active=which==HOME?0:(which==DISCOVER||which==TRY||which==DEALS)?1:which==INSIGHTS?2:which==PROFILE?3:-1;
        for(int i=0;i<4;i++){int col=i==active?getColor(R.color.green):getColor(R.color.muted);
            ImageView ic=findViewById(navIcons[i]); ic.setColorFilter(col); ic.animate().scaleX(i==active?1.12f:1f).scaleY(i==active?1.12f:1f).setDuration(200).start();
            TextView tv=findViewById(navTexts[i]); tv.setTextColor(col); tv.setTypeface(null,i==active?android.graphics.Typeface.BOLD:android.graphics.Typeface.NORMAL);}
    }

    void updateStatusBar(int which){
        View d=getWindow().getDecorView();
        if(which==DISCOVER){getWindow().setStatusBarColor(getColor(R.color.green));d.setSystemUiVisibility(0);}
        else{getWindow().setStatusBarColor(Color.TRANSPARENT);d.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);}
    }

    @Override public void onBackPressed(){
        if(splashHolder.getVisibility()==View.VISIBLE){super.onBackPressed();return;}
        if(!back.isEmpty()){show(back.pop(),false);return;}
        if(screen!=HOME){show(HOME,false);return;}
        super.onBackPressed();
    }

    View build(int which){
        LayoutInflater in=getLayoutInflater();
        switch(which){
            case HOME: return buildHome(in);
            case DISCOVER: return buildDiscover(in);
            case TRY: return buildTry(in);
            case DEALS: return buildDeals(in);
            case INSIGHTS: return buildInsights(in);
            case PROFILE: return buildProfile(in);
            default: return buildOrder(in);
        }
    }

    void topBar(View v,String title,boolean share){
        TextView t=v.findViewById(R.id.title); if(t!=null) t.setText(title);
        View b=v.findViewById(R.id.back); if(b!=null) b.setOnClickListener(x->onBackPressed());
        if(share){v.findViewById(R.id.action_icon).setVisibility(View.VISIBLE);v.findViewById(R.id.action_text).setVisibility(View.VISIBLE);}
    }

    void card(View home,int id,int bg,String title,String sub,int img,final int target){
        View c=home.findViewById(id); c.setBackgroundResource(bg);
        ((TextView)c.findViewById(R.id.c_title)).setText(title); ((TextView)c.findViewById(R.id.c_sub)).setText(sub);
        ((ImageView)c.findViewById(R.id.c_img)).setImageResource(img);
        c.setOnClickListener(v->{v.animate().scaleX(0.97f).scaleY(0.97f).setDuration(90).withEndAction(()->{v.animate().scaleX(1f).scaleY(1f).setDuration(120).start();show(target,true);}).start();});
    }

    View buildHome(LayoutInflater in){
        View v=in.inflate(R.layout.screen_home,container,false);
        card(v,R.id.card_order,R.drawable.bg_card_peach,"Order\nfor Me","Let AI pick tonight’s dinner",R.drawable.ill_order,ORDER);
        card(v,R.id.card_fav,R.drawable.bg_card_pink,"Find My\nNext Favourite","5 dishes you’ll probably love",R.drawable.ill_trybowl,DISCOVER);
        card(v,R.id.card_try,R.drawable.bg_card_sky,"What Should\nI Try Next?","Step into a new cuisine",R.drawable.ill_discover,TRY);
        card(v,R.id.card_deals,R.drawable.bg_card_butter,"Best\nDiscounts for Me","Personalized deals you’ll actually like",R.drawable.ill_dealtag,DEALS);
        card(v,R.id.card_insights,R.drawable.bg_card_mint,"My\nFood Spending","See your trends and insights",R.drawable.ill_insights,INSIGHTS);
        card(v,R.id.card_profile,R.drawable.bg_card_lilac,"My\nFood Profile","Your taste DNA and food persona",R.drawable.ill_profile,PROFILE);
        return v;
    }

    View buildDiscover(LayoutInflater in){
        View v=in.inflate(R.layout.screen_discover,container,false);
        v.findViewById(R.id.back).setOnClickListener(x->onBackPressed());
        LinearLayout list=v.findViewById(R.id.list);
        Object[][] rows={
            {R.drawable.dish_paneer,"Korean Gochujang Paneer Bowl","Nature’s Bowl","4.4  (1.2k)","₹399","89% match","New for you"},
            {R.drawable.dish_pizza,"Truffle Mushroom Pizza","The Pizzeria Project","4.3  (3.1k)","₹499","87% match","New for you"},
            {R.drawable.dish_thai,"Thai Basil Paneer Rice Bowl","Bowl Theory","4.5  (417)","₹379","85% match","New for you"},
            {R.drawable.dish_tacos,"Peri Peri Cottage Cheese Tacos","The Taco Club","4.2  (892)","₹429","84% match","New cuisine"},
            {R.drawable.dish_ramen,"Korean Veg Ramen Bowl","Soulfully Yours","4.4  (2.1k)","₹399","83% match","New cuisine"}};
        int i=0;
        for(Object[] r:rows){View it=in.inflate(R.layout.item_dish,list,false);
            ((ImageView)it.findViewById(R.id.img)).setImageResource((Integer)r[0]);
            ((TextView)it.findViewById(R.id.name)).setText((String)r[1]); ((TextView)it.findViewById(R.id.rest)).setText((String)r[2]);
            ((TextView)it.findViewById(R.id.rating)).setText((String)r[3]); ((TextView)it.findViewById(R.id.price)).setText((String)r[4]);
            ((TextView)it.findViewById(R.id.match)).setText((String)r[5]); ((TextView)it.findViewById(R.id.newtag)).setText((String)r[6]);
            it.setOnClickListener(x->show(ORDER,true));
            it.setAlpha(0f); it.setTranslationY(dp(18)); it.animate().alpha(1f).translationY(0).setStartDelay(80+i*70).setDuration(360).setInterpolator(ease).start(); i++;
            list.addView(it);}
        return v;
    }

    View buildOrder(LayoutInflater in){
        View v=in.inflate(R.layout.screen_order,container,false);
        topBar(v,"Order for Me",false);
        TextView old=v.findViewById(R.id.old_price); old.setPaintFlags(old.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
        LinearLayout rs=v.findViewById(R.id.reasons);
        String[] reasons={"Matches your spicy + rich + comfort food taste","You haven’t ordered it before","Great value today","Perfect for a late-evening meal"};
        for(String s:reasons){View r=in.inflate(R.layout.row_reason,rs,false);((TextView)r.findViewById(R.id.t)).setText(s);rs.addView(r);}
        v.findViewById(R.id.btn_zomato).setOnClickListener(x->Toast.makeText(this,"Zomato hand-off coming soon",Toast.LENGTH_SHORT).show());
        v.findViewById(R.id.btn_more).setOnClickListener(x->show(DISCOVER,true));
        return v;
    }

    View buildTry(LayoutInflater in){
        View v=in.inflate(R.layout.screen_try,container,false);
        topBar(v,"What Should I Try Next?",false);
        v.findViewById(R.id.btn_bridges).setOnClickListener(x->show(DISCOVER,true));
        return v;
    }

    View buildDeals(LayoutInflater in){
        View v=in.inflate(R.layout.screen_deals,container,false);
        topBar(v,"Best Discounts for Me",false);
        LinearLayout list=v.findViewById(R.id.list);
        Object[][] rows={
            {R.drawable.deal_pizza,"Paneer Tikka Pizza","The Pizzeria Project","4.3  (3.1k)","₹599","₹399","33% OFF","92% match"},
            {R.drawable.dish_ramen,"Korean Veg Ramen","Soulfully Yours","4.4  (2.1k)","₹399","₹279","30% OFF","91% match"},
            {R.drawable.deal_biryani,"Mushroom Biryani","Biryani Blues","4.2  (4.4k)","₹450","₹299","34% OFF","89% match"},
            {R.drawable.deal_fries,"Peri Peri Fries","Burger & Beyond","4.1  (1.1k)","₹249","₹179","28% OFF","87% match"}};
        int i=0;
        for(Object[] r:rows){View it=in.inflate(R.layout.item_deal,list,false);
            ((ImageView)it.findViewById(R.id.img)).setImageResource((Integer)r[0]);
            ((TextView)it.findViewById(R.id.name)).setText((String)r[1]); ((TextView)it.findViewById(R.id.rest)).setText((String)r[2]); ((TextView)it.findViewById(R.id.rating)).setText((String)r[3]);
            TextView op=it.findViewById(R.id.oldp); op.setText((String)r[4]); op.setPaintFlags(op.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
            ((TextView)it.findViewById(R.id.newp)).setText((String)r[5]); ((TextView)it.findViewById(R.id.off)).setText((String)r[6]); ((TextView)it.findViewById(R.id.match)).setText((String)r[7]);
            it.setOnClickListener(x->show(ORDER,true));
            it.setAlpha(0f); it.setTranslationY(dp(18)); it.animate().alpha(1f).translationY(0).setStartDelay(80+i*70).setDuration(360).setInterpolator(ease).start(); i++;
            list.addView(it);}
        return v;
    }

    View buildInsights(LayoutInflater in){
        View v=in.inflate(R.layout.screen_insights,container,false);
        topBar(v,"My Food Spending",false);
        LinearLayout legend=v.findViewById(R.id.legend); DonutView donut=v.findViewById(R.id.donut);
        String[] names={"Indian","Pizza","Biryani","Fast Food","Asian","Others"}; float[] vals={32,18,14,13,9,14};
        donut.values=vals;
        for(int i=0;i<names.length;i++){View r=in.inflate(R.layout.row_legend,legend,false);
            ((TextView)r.findViewById(R.id.label)).setText(names[i]); ((TextView)r.findViewById(R.id.pct)).setText((int)vals[i]+"%");
            android.graphics.drawable.GradientDrawable d=new android.graphics.drawable.GradientDrawable(); d.setShape(android.graphics.drawable.GradientDrawable.OVAL); d.setColor(donut.colors[i]); r.findViewById(R.id.dot).setBackground(d);
            legend.addView(r);}
        return v;
    }

    View buildProfile(LayoutInflater in){
        View v=in.inflate(R.layout.screen_profile,container,false);
        topBar(v,"My Food Profile",true);
        LinearLayout dna=v.findViewById(R.id.dna);
        Object[][] rows={{"🌶️","Spicy",0.91f,0xFFD63F41},{"🧀","Cheesy",0.83f,0xFFF2AA0C},{"🍲","Paneer",0.94f,0xFFF2AA0C},{"🍛","Indian Flavours",0.90f,0xFF0A4837},{"🔥","Smoky",0.78f,0xFF7A4B2A},{"🍚","Biryani",0.87f,0xFFE47E2E},{"✨","Novelty",0.64f,0xFF8062B8}};
        for(Object[] r:rows){View row=in.inflate(R.layout.row_dna,dna,false);
            ((TextView)row.findViewById(R.id.emoji)).setText((String)r[0]); ((TextView)row.findViewById(R.id.label)).setText((String)r[1]);
            ((TextView)row.findViewById(R.id.pct)).setText((int)((Float)r[2]*100)+"%");
            ((BarView)row.findViewById(R.id.bar)).set((Float)r[2],(Integer)r[3]);
            dna.addView(row);}
        return v;
    }

    float dp(float v){return v*getResources().getDisplayMetrics().density;}
}
