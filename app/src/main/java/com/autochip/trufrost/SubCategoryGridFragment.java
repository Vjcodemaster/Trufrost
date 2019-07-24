package com.autochip.trufrost;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app_utility.DataBaseHelper;
import app_utility.DatabaseHandler;
import app_utility.OnFragmentInteractionListener;
import app_utility.SubCategoryImageRVAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link app_utility.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubCategoryGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubCategoryGridFragment extends Fragment  {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String sSubCategory;
    String sSubCategoryImagesPath;

    private ArrayList<String> alSubCategoryNames;
    private ArrayList<String> alSubCategoryImagePath;

    DatabaseHandler dbh;
    ArrayList<DataBaseHelper> alSubCategoryData;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;

    public SubCategoryGridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubCategoryGridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubCategoryGridFragment newInstance(String param1, String param2) {
        SubCategoryGridFragment fragment = new SubCategoryGridFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dbh = new DatabaseHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_category_grid, container, false);

        initViews(view);
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_sub_products);
        LinearLayoutManager mLinearLayoutManager;
        //flContainer = findViewById(R.id.fl_menu);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLinearLayoutManager = new GridLayoutManager(getActivity(), 4);
            mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            // In landscape
        } else {
            mLinearLayoutManager = new GridLayoutManager(getActivity(), 3);
            mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            // In portrait
        }

        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        alSubCategoryData = new ArrayList<>(dbh.getSubCategoryUsingFirstSC(mParam1));

        ArrayList<String> alSubCategoryNames = new ArrayList<>();
        ArrayList<String> alSubCategoryImagePath = new ArrayList<>();
        if(alSubCategoryData.get(0).get_product_category_names()!=null)
            alSubCategoryNames = new ArrayList<>(Arrays.asList(alSubCategoryData.get(0).get_product_category_names().split("##")));

        if(alSubCategoryData.get(0).get_sub_category_images_path()!=null)
            alSubCategoryImagePath = new ArrayList<>(Arrays.asList(alSubCategoryData.get(0).get_sub_category_images_path().split("##")));
        /*alFirstSubCategoryNames.add("Trufrost – Professional Refrigeration Products");
        alFirstSubCategoryNames.add("Trufrost – Ice Machines");
        alFirstSubCategoryNames.add("Trufrost – Cold Rooms");
        alFirstSubCategoryNames.add("Butler – Espresso Coffee Machine");
        alFirstSubCategoryNames.add("Butler – Commercial Blenders");
        alFirstSubCategoryNames.add("Butler – Combi Steamers");
        alFirstSubCategoryNames.add("Butler – Bakery Equipment");
        alFirstSubCategoryNames.add("Butler – Catering Products");
        alFirstSubCategoryNames.add("Butler – Induction Systems");*/


        //ArrayList<String> alSubCategoryNames = new ArrayList<>(Arrays.asList(alSubCategoryData.get(0).get_product_category_names().split("##")));
        //ArrayList<String> alSubCategoryImagePath = new ArrayList<>(Arrays.asList(alSubCategoryData.get(0).get_sub_category_images_path().split("##")));
        if(alSubCategoryNames.size()>0) {
            SubCategoryImageRVAdapter subCategoryImageRVAdapter = new SubCategoryImageRVAdapter(getActivity(), recyclerView,
                    alSubCategoryNames, alSubCategoryImagePath);
            recyclerView.setAdapter(subCategoryImageRVAdapter);
        }
        /*for (int i = 0; i < alDBData.size(); i++) {
            if (alDBData.get(i).get_main_product_names().equals(mParam1)) {
                sSubCategory = alDBData.get(i).get_product_category_names();
                sSubCategoryImagesPath = alDBData.get(i).get_sub_category_images_path();
                break;
            }
        }*/

        /*if(sSubCategory!=null) {
            final String[] sSubCategoryArray = sSubCategory.split("##");
            alSubCategoryNames = new ArrayList<>(Arrays.asList(sSubCategoryArray));

            if(sSubCategoryImagesPath!=null)
            alSubCategoryImagePath = new ArrayList<>(Arrays.asList(sSubCategoryImagesPath.split(",")));

            SubCategoryImageRVAdapter subCategoryImageRVAdapter = new SubCategoryImageRVAdapter(getActivity(), recyclerView,
                    alSubCategoryNames, alSubCategoryImagePath);
            recyclerView.setAdapter(subCategoryImageRVAdapter);
        }*/
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
