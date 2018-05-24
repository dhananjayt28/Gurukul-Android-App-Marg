package in.jivanmuktas.www.marg.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import in.jivanmuktas.www.marg.R;

/**
 * Created by YAJU on 2/5/2016.
 */
public class DemoPagerAdapter extends PagerAdapter implements View.OnClickListener {

    private Context context;
    private int[] imageArray;
    private View lastView;

    private final CallbackDemoPagerAdapter callbackDemoPagerAdapter;

    @Override
    public void onClick(View v) {
        callbackDemoPagerAdapter.skipDemo();
    }

    public interface CallbackDemoPagerAdapter {
        void skipDemo();
    }


    public DemoPagerAdapter(Context context, int[] imageArray, View lastView, CallbackDemoPagerAdapter callbackDemoPagerAdapter) {
        this.context = context;
        this.imageArray = imageArray;
        this.lastView = lastView;
        this.callbackDemoPagerAdapter = callbackDemoPagerAdapter;
    }

    @Override
    public int getCount() {
        return imageArray.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //if (position != getCount() - 1) {
            View v = inflater.inflate(R.layout.demo_image_container, null, false);
            ImageView ivDemo = (ImageView) v.findViewById(R.id.ivDemo);
            ivDemo.setImageResource(imageArray[position]);
            container.addView(v);
            return v;
        //} /*else {
           // container.addView(lastView);
           // return lastView;
       // }*/
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
