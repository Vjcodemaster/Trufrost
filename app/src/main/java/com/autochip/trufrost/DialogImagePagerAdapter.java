package com.autochip.trufrost;

/*
 * Created by Vj on 30-Mar-17.
 */

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;

class DialogImagePagerAdapter extends PagerAdapter {

    private Activity aActivity;
    private LayoutInflater mLayoutInflater;
    //private int[] mResources;
    //private OnFragmentInteractionListener mListener;
    //private String[] saImagePath;
    ArrayList<String> alImagesPath = new ArrayList<>();
    //private CircularProgressBar circularProgressBar;

    /*private String[] sImageURL = {"https://s3.amazonaws.com/sohamsaabucket/01-min.jpg", "https://s3.amazonaws.com/sohamsaabucket/02-min.jpg",
            "https://s3.amazonaws.com/sohamsaabucket/03-min.jpg"};*/

    DialogImagePagerAdapter(Activity aActivity, ArrayList<String> alImagesPath) {
        this.aActivity = aActivity;
        this.alImagesPath = alImagesPath;
        mLayoutInflater = (LayoutInflater) aActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return alImagesPath.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.dialog_image_pager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        Uri uri = Uri.fromFile(new File(alImagesPath.get(position)));
        imageView.setImageURI(uri);



        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
