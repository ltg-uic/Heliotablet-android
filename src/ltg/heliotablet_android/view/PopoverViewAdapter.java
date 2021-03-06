package ltg.heliotablet_android.view;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;

public class PopoverViewAdapter extends PagerAdapter {

    private ArrayList<View> pages = null;

    public PopoverViewAdapter(ArrayList<View> pages) {
        this.pages = pages;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    public View getView(int position) {
        return pages.get(position);
    }

    public View findViewById(int position, int id) {
        return pages.get(position).findViewById(id);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = pages.get(position);
        container.addView(v, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //container.removeViewAt(position);
    }
}
