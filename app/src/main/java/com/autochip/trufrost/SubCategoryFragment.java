package com.autochip.trufrost;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app_utility.DataBaseHelper;
import app_utility.DatabaseHandler;
import app_utility.OnFragmentInteractionListener;
import app_utility.ProductsRVAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link app_utility.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubCategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button[] btnSubCategoryArray;
    ArrayList<DataBaseHelper> dbData;
    LinearLayout llDynamicParent;
    DatabaseHandler dbh;
    String sSubCategory;
    ArrayList<String> alSubCategory;
    private TypedValue typedValue;
    RecyclerView recyclerViewProducts;
    private ExpandableLayout expandableLayout;

    Button btnPreviousClicked = null;

    String sPreviousCategoryname = "";

    public SubCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubCategoryFragment.
     */
    public static SubCategoryFragment newInstance(String param1, String param2) {
        SubCategoryFragment fragment = new SubCategoryFragment();
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
        /*if (btnSubCategoryArray[0] != null)
            btnSubCategoryArray[0].callOnClick();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_category, container, false);
        initViews(view);

        getData();
        return view;
    }

    private void initViews(View view) {
        TextView tvMainMenuName = view.findViewById(R.id.tv_main_menu_name);
        llDynamicParent = view.findViewById(R.id.ll_dynamic_parent);
        expandableLayout = view.findViewById(R.id.expandable_layout);
        recyclerViewProducts = expandableLayout.findViewById(R.id.rv_products_list);
        LinearLayoutManager mLinearLayoutManager;

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
        /*LinearLayoutManager mLinearLayoutManager = new GridLayoutManager(getActivity(), 4);
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);*/

        recyclerViewProducts.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        recyclerViewProducts.setHasFixedSize(true);
        recyclerViewProducts.setLayoutManager(mLinearLayoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));

        ProductsRVAdapter productsRVAdapter = new ProductsRVAdapter(getActivity(), recyclerViewProducts, mParam1);
        recyclerViewProducts.setAdapter(productsRVAdapter);

        tvMainMenuName.setText(mParam1);

        typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
    }

    private void getData() {
        dbData = new ArrayList<>(dbh.getAllMainProducts());
        for (int i = 0; i < dbData.size(); i++) {
            if (dbData.get(i).get_main_product_names().equals(mParam1)) {
                //alSubCategory = new ArrayList<>(Arrays.asList(al.get(i).get_product_category_names().split(",,")));
                sSubCategory = dbData.get(i).get_product_category_names();
                break;
            }
        }

        if (sSubCategory != null && !sSubCategory.equals("")) {
            initializeData();
        }
    }

    private void initializeData() {
        final String[] sSubCategoryArray = sSubCategory.split("##");
        alSubCategory = new ArrayList<>(Arrays.asList(sSubCategoryArray));
        btnSubCategoryArray = new Button[alSubCategory.size()];
        for (int i = 0; i < btnSubCategoryArray.length; i++) {
            addDynamicContentsForSubCategory(i, alSubCategory);
            final int finalI = i;
            btnSubCategoryArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sSubCategory = btnSubCategoryArray[finalI].getText().toString().trim();

                    /*if (!expandableLayout.isExpanded() && sSubCategory.equals(sPreviousCategoryname) && btnSubCategoryArray[finalI] == btnPreviousClicked) {
                        Drawable img = getContext().getResources().getDrawable(R.drawable.up_arrow, null);
                        img.setBounds(0, 0, 30, 30);
                        btnSubCategoryArray[finalI].setCompoundDrawables(null, null, img, null);
                        btnSubCategoryArray[finalI].setCompoundDrawablePadding(6);
                    } else*/ if (btnSubCategoryArray[finalI] == btnPreviousClicked) {
                        expandableLayout.setExpanded(false);
                        //expandableLayout.setVisibility(View.VISIBLE);
                        expandableLayout.collapse();
                        btnPreviousClicked = null;
                        sPreviousCategoryname = sSubCategory;
                        Drawable img = getContext().getResources().getDrawable(R.drawable.down_arrow, null);
                        img.setBounds(0, 0, 30, 30);
                        btnSubCategoryArray[finalI].setCompoundDrawables(null, null, img, null);
                        btnSubCategoryArray[finalI].setCompoundDrawablePadding(6);
                    } else {
                        if (btnPreviousClicked != null) {
                            expandableLayout.collapse();
                        }
                        ProductsRVAdapter productsRVAdapter = new ProductsRVAdapter(getActivity(), recyclerViewProducts, sSubCategory);
                        recyclerViewProducts.setAdapter(productsRVAdapter);

                        Drawable img = getContext().getResources().getDrawable(R.drawable.up_arrow, null);
                        img.setBounds(0, 0, 30, 30);
                        btnSubCategoryArray[finalI].setCompoundDrawables(null, null, img, null);
                        btnSubCategoryArray[finalI].setCompoundDrawablePadding(6);

                        expandableLayout.expand();
                        btnPreviousClicked = btnSubCategoryArray[finalI];
                        sPreviousCategoryname = sSubCategory;
                    }
                    /*ProductsRVAdapter productsRVAdapter= new ProductsRVAdapter(getActivity(), recyclerViewProducts, sSubCategory);
                    recyclerViewProducts.setAdapter(productsRVAdapter);
                    expandableLayout.setVisibility(View.VISIBLE);
                    expandableLayout.expand();

                    Drawable img = getContext().getResources().getDrawable( R.drawable.up_arrow, null);
                    img.setBounds( 0, 0, 30, 30);
                    btnSubCategoryArray[finalI].setCompoundDrawables( null, null, img, null );
                    btnSubCategoryArray[finalI].setCompoundDrawablePadding(6);
                    //openProductsFragment(sCategory[finalI]);

                    btnPreviousClicked = btnSubCategoryArray[finalI];*/
                }
            });
        }

        btnSubCategoryArray[0].callOnClick();
    }


    private void addDynamicContentsForSubCategory(int i, ArrayList<String> alSubCategory) {
        //Button btnDynamic = new Button(HomeScreenActivity.this);

        btnSubCategoryArray[i] = new Button(getActivity());

        btnSubCategoryArray[i].setTag(alSubCategory.get(i));

        if (Build.VERSION.SDK_INT < 23) {
            //noinspection deprecation
            btnSubCategoryArray[i].setTextAppearance(getActivity(), R.style.TextAppearance_AppCompat_Medium);
        } else {
            btnSubCategoryArray[i].setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(5);
        params.setMarginEnd(5);
        btnSubCategoryArray[i].setLayoutParams(params);
        btnSubCategoryArray[i].setText(alSubCategory.get(i));
        btnSubCategoryArray[i].setTextColor(getResources().getColor(R.color.darkBlue));
        //btnMenuOne[i].setBackground(drawableFromTheme);
        btnSubCategoryArray[i].setClickable(true);

        Drawable img = getContext().getResources().getDrawable(R.drawable.down_arrow, null);
        img.setBounds(0, 0, 30, 30);
        btnSubCategoryArray[i].setCompoundDrawables(null, null, img, null);
        btnSubCategoryArray[i].setCompoundDrawablePadding(6);
        btnSubCategoryArray[i].setBackgroundResource(typedValue.resourceId);
        btnSubCategoryArray[i].setAllCaps(false);
        llDynamicParent.addView(btnSubCategoryArray[i]);
    }

    /*private void handleExpandableLayout() {
        if (expandableLayout.isExpanded()) {
            expandableLayout.setVisibility(View.GONE);
            expandableLayout.collapse();
            tvExpand.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle_plus, 0, 0, 0);
        } else {
            expandableLayout.expand();
            expandableLayout.setVisibility(View.VISIBLE);
            tvExpand.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle_minus, 0, 0, 0);
        }
    }*/

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
    public void onStop(){
        super.onStop();
        expandableLayout.setExpanded(true);
    }
    @Override
    public void onResume(){
        super.onResume();
        //expandableLayout.setExpanded(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
