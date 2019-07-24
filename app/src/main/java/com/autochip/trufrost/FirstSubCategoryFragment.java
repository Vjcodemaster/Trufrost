package com.autochip.trufrost;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import app_utility.DataBaseHelper;
import app_utility.DatabaseHandler;
import app_utility.FirstSubCategoryRVAdapter;
import app_utility.OnFragmentInteractionListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link app_utility.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirstSubCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstSubCategoryFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView tvTitle;

    private RecyclerView recyclerView;

    private DatabaseHandler dbh;

    private ArrayList<DataBaseHelper> alFirstSCData;

    public FirstSubCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstSubCategoryFragment.
     */
    public static FirstSubCategoryFragment newInstance(String param1, String param2) {
        FirstSubCategoryFragment fragment = new FirstSubCategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_first_sub_category, container, false);
        initViews(view);
        return view;
    }


    private void initViews(View view){
        tvTitle = view.findViewById(R.id.tv_title);
        recyclerView = view.findViewById(R.id.rv_first_sub_products);


        tvTitle.setText(mParam1);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        alFirstSCData = new ArrayList<>(dbh.getFirstSubCategoryNameAndImage(mParam1));

        ArrayList<String> alFirstSubCategoryNames = new ArrayList<>();
        ArrayList<String> alFirstSubCategoryImagePath = new ArrayList<>();
        if(alFirstSCData.size()>=1 && alFirstSCData.get(0).get_first_sub_category_names()!=null)
            alFirstSubCategoryNames = new ArrayList<>(Arrays.asList(alFirstSCData.get(0).get_first_sub_category_names().split("##")));

        if(alFirstSCData.size()>=1 && alFirstSCData.get(0).get_first_sub_category_images_path()!=null)
            alFirstSubCategoryImagePath = new ArrayList<>(Arrays.asList(alFirstSCData.get(0).get_first_sub_category_images_path().split("##")));
        /*alFirstSubCategoryNames.add("Trufrost – Professional Refrigeration Products");
        alFirstSubCategoryNames.add("Trufrost – Ice Machines");
        alFirstSubCategoryNames.add("Trufrost – Cold Rooms");
        alFirstSubCategoryNames.add("Butler – Espresso Coffee Machine");
        alFirstSubCategoryNames.add("Butler – Commercial Blenders");
        alFirstSubCategoryNames.add("Butler – Combi Steamers");
        alFirstSubCategoryNames.add("Butler – Bakery Equipment");
        alFirstSubCategoryNames.add("Butler – Catering Products");
        alFirstSubCategoryNames.add("Butler – Induction Systems");*/

        if(alFirstSubCategoryNames.size()>0) {
            FirstSubCategoryRVAdapter firstSubCategoryRVAdapter = new FirstSubCategoryRVAdapter(getActivity(), recyclerView,
                    alFirstSubCategoryNames, alFirstSubCategoryImagePath);
            recyclerView.setAdapter(firstSubCategoryRVAdapter);
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
