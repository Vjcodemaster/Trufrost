package com.autochip.trufrost;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app_utility.DatabaseHandler;
import app_utility.IndividualProductRVAdapter;
import app_utility.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link app_utility.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndividualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndividualFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String mParam1;
    private String mParam2;
    private int mParam3;

    DatabaseHandler dbh;

    TextView tvProductName, tvProductDescription;
    RecyclerView recyclerViewImages;
    private OnFragmentInteractionListener mListener;

    String[] saImagePath;

    Dialog dialogViewPager;

    IndividualProductRVAdapter individualProductRVAdapter;

    ArrayList<String> alTechHeading;
    ArrayList<String> alTechValues;
    TableRow tableRowHeading, tableRowValue;
    TextView[] tvTechSpecsHeading;
    TextView[] tvTechSpecsValues;
    TableLayout tlTechnicalSpecs;

    ImageView ivMainImage;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_individual, container, false);


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
        ArrayList<String> alImagesPath = new ArrayList<>(Arrays.asList(dbh.getImagePathFromProducts(mParam3).split(",")));
        Uri uri = Uri.fromFile(new File(alImagesPath.get(0)));
        ivMainImage.setImageURI(uri);
        individualProductRVAdapter= new IndividualProductRVAdapter(getActivity(), recyclerViewImages, alImagesPath);
        recyclerViewImages.setAdapter(individualProductRVAdapter);
    }

    private void getData(){
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

}
