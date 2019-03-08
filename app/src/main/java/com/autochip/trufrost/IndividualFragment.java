package com.autochip.trufrost;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import app_utility.DataBaseHelper;
import app_utility.DatabaseHandler;
import app_utility.IndividualProductRVAdapter;
import app_utility.OnFragmentInteractionListener;
import app_utility.ZoomOutPageTransformer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link app_utility.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndividualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndividualFragment extends Fragment implements OnFragmentInteractionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String mParam1;
    private String mParam2;
    private int mParam3;

    int imagePathPosition = 0;

    DatabaseHandler dbh;

    TextView tvProductName, tvProductDescription;
    RecyclerView recyclerViewImages;
    public static OnFragmentInteractionListener mListener;

    String[] saImagePath;

    Dialog dialogViewPager;

    IndividualProductRVAdapter individualProductRVAdapter;

    ArrayList<String> alTechHeading;
    ArrayList<String> alTechValues;
    TableRow tableRowHeading, tableRowValue;
    TextView[] tvTechSpecsHeading;
    TextView[] tvTechSpecsValues;
    TableLayout tlTechnicalSpecs;

    ImageView ivMainImage, ivBlurImage;

    ViewPager mViewPagerSlideShow;
    ImageView ivLeftArrow;
    ImageView ivRightArrow;
    ArrayList<String> alImagesPath;



    public IndividualFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IndividualFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndividualFragment newInstance(String param1, String param2, int param3) {
        IndividualFragment fragment = new IndividualFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getInt(ARG_PARAM3);
        }
        dbh = new DatabaseHandler(getActivity());
        mListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_individual_e, container, false);


        initViews(view);

        getData();

        return view;
    }


    private void initViews(View view){
        tvProductName = view.findViewById(R.id.tv_product_name);
        tvProductName.setText(mParam1);
        tvProductDescription = view.findViewById(R.id.tv_product_description);
        tvProductDescription.setText(mParam2);
        tlTechnicalSpecs = view.findViewById(R.id.tl_technical_specs);
        ivMainImage = view.findViewById(R.id.iv_main_image);

        ivMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initImagePreviewDialog();
                dialogViewPager.show();
            }
        });

        tableRowHeading = new TableRow(getActivity());
        tableRowHeading.setBackgroundColor(getActivity().getResources().getColor(android.R.color.white));
        tableRowValue = new TableRow(getActivity());
        tableRowValue.setBackgroundColor(getActivity().getResources().getColor(android.R.color.white));

        recyclerViewImages = view.findViewById(R.id.rv_individual_product_images);
        LinearLayoutManager mLinearLayoutManager;

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLinearLayoutManager = new GridLayoutManager(getActivity(), 5);
            mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            // In landscape
        } else {
            mLinearLayoutManager = new GridLayoutManager(getActivity(), 3);
            mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            // In portrait
        }
        /*LinearLayoutManager mLinearLayoutManager = new GridLayoutManager(getActivity(), 4);
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);*/

        recyclerViewImages.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        recyclerViewImages.setHasFixedSize(true);
        recyclerViewImages.setLayoutManager(mLinearLayoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        //
        // saImagePath = dbh.getImagePathFromProducts(mParam3).split(",");
        alImagesPath = new ArrayList<>(Arrays.asList(dbh.getImagePathFromProducts(mParam3).split(",")));
        Uri uri = Uri.fromFile(new File(alImagesPath.get(0)));
        ivMainImage.setImageURI(uri);
        individualProductRVAdapter= new IndividualProductRVAdapter(getActivity(), recyclerViewImages, alImagesPath);
        recyclerViewImages.setAdapter(individualProductRVAdapter);
    }

    private void getData(){
        ArrayList<DataBaseHelper> arrayList = new ArrayList<>(dbh.getAllMainProducts());
        ArrayList<DataBaseHelper> arrayList1 = new ArrayList<>(dbh.getAllProductsData1());
        alTechHeading = new ArrayList<>(Arrays.asList(dbh.getProductTechSpecHeading(mParam3).split(",")));
        alTechValues = new ArrayList<>(Arrays.asList(dbh.getProductTechSpecValue(mParam3).split(",")));

        tvTechSpecsHeading = new TextView[alTechHeading.size()];
        tvTechSpecsValues = new TextView[alTechValues.size()];

        tvTechSpecsValues = new TextView[alTechValues.size()];
        for (int i = 0; i < alTechHeading.size(); i++) {
            addDynamicTextViewTechSpecsHeading(i);
            addDynamicTextViewTechSpecsValues(i);
        }

    }

    private void addDynamicTextViewTechSpecsHeading(int i) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(1, 2, 1, 2);
        params.gravity = Gravity.CENTER;


        TextView tv = new TextView(getActivity());
        tv.setText(alTechHeading.get(i));
        tv.setLayoutParams(params);
        tv.setPadding(4, 0, 4, 0);
        tv.setTextColor(getActivity().getResources().getColor(android.R.color.white));
        tv.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
        tvTechSpecsHeading[i] = tv;
        tableRowHeading.addView(tv);
        if (i == alTechHeading.size() - 1) {
            tlTechnicalSpecs.addView(tableRowHeading, 0);
        }
    }

    private void addDynamicTextViewTechSpecsValues(int i) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(1, 2, 1, 2);
        params.gravity = Gravity.CENTER;

        TextView tv = new TextView(getActivity());
        try {
            tv.setText(alTechValues.get(i));
            tv.setLayoutParams(params);
            tv.setTextColor(getActivity().getResources().getColor(R.color.darkBlue));
            tv.setBackgroundColor(getActivity().getResources().getColor(android.R.color.white));
            tvTechSpecsHeading[i] = tv;
            tableRowValue.addView(tv);
            if (i == alTechHeading.size() - 1) {
                tlTechnicalSpecs.addView(tableRowValue);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            if (i == alTechHeading.size() - 1) {
                tlTechnicalSpecs.addView(tableRowValue);
            }
        }
    }

    private void initImagePreviewDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_view_pager, null);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.9f));
        dialogViewPager = new Dialog(getActivity());
        dialogViewPager.setContentView(layout);
        dialogViewPager.setCancelable(true);

        //TextView tvHeading = dialogViewPager.findViewById(R.id.tv_readmore_heading);
        TextView tvClosePreview = dialogViewPager.findViewById(R.id.tv_close_dialog);
        ivBlurImage = dialogViewPager.findViewById(R.id.iv_blur_bg);

        tvClosePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogViewPager.dismiss();
            }
        });
        mViewPagerSlideShow = dialogViewPager.findViewById(R.id.viewpager_image_dialog);
        mViewPagerSlideShow.setOffscreenPageLimit(3);


        ivLeftArrow = dialogViewPager.findViewById(R.id.iv_dialog_left_arrow);
        ivRightArrow = dialogViewPager.findViewById(R.id.iv_dialog_right_arrow);

        ivLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPagerSlideShow.setCurrentItem(mViewPagerSlideShow.getCurrentItem() - 1);
            }
        });

        ivRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPagerSlideShow.setCurrentItem(mViewPagerSlideShow.getCurrentItem() + 1);
            }
        });

        mViewPagerSlideShow.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                handleArrow(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPagerSlideShow.setPageTransformer(false, new ViewPager.PageTransformer()

        {
            @Override
            public void transformPage(View page, float position) {
                ZoomOutPageTransformer zoomOutPageTransformer = new ZoomOutPageTransformer();
                zoomOutPageTransformer.transformPage(page, position);
            }
        });

        final DialogImagePagerAdapter dialogImagePagerAdapter = new DialogImagePagerAdapter(getActivity(), alImagesPath);
        mViewPagerSlideShow.setAdapter(dialogImagePagerAdapter);
        mViewPagerSlideShow.setCurrentItem(imagePathPosition);
        handleArrow(imagePathPosition);
        //takeScreenshot();
        /*Typeface lightFace = Typeface.createFromAsset(getResources().getAssets(), "fonts/myriad_pro_light.ttf");
        Typeface regularFace = Typeface.createFromAsset(getResources().getAssets(), "fonts/myriad_pro_regular.ttf");
        tvHeading.setTypeface(regularFace);*/
        //tvSubHeading.setTypeface(lightFace);
        //tvDescription.setTypeface(lightFace);
    }

    private void handleArrow(int position) {
        if (position == 0 && mViewPagerSlideShow.getAdapter().getCount() == 1) {
            ivLeftArrow.setVisibility(View.GONE);
            ivRightArrow.setVisibility(View.GONE);
        } else if(position == 0 && mViewPagerSlideShow.getAdapter().getCount() > 1){
            ivLeftArrow.setVisibility(View.GONE);
            ivRightArrow.setVisibility(View.VISIBLE);
        }else if (position == alImagesPath.size() - 1) {
            ivRightArrow.setVisibility(View.GONE);
            ivLeftArrow.setVisibility(View.VISIBLE);
        } else {
            ivLeftArrow.setVisibility(View.VISIBLE);
            ivRightArrow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentMessage(String sMsg, int type, String sResults, String sResult) {
        switch (sMsg){
            case "IMAGE_CLICKED":
                Uri uri = Uri.fromFile(new File(sResult));
                imagePathPosition = type;
                //mViewPagerSlideShow.setCurrentItem(imagePathPosition);
                ivMainImage.setImageURI(uri);
                break;
        }
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            ivBlurImage.setImageBitmap(blurBitmap(bitmap));
            v1.setDrawingCacheEnabled(false);

            /*File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();*/

            //openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private Bitmap blurBitmap(Bitmap bitmap){
        RenderScript rs = RenderScript.create(getActivity());
        Allocation input = Allocation.createFromBitmap(rs, bitmap,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(25);
        script.setInput(input);

        script.forEach(output);
        output.copyTo(bitmap);

        return bitmap;
    }
}
