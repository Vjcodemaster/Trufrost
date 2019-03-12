package com.autochip.trufrost;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app_utility.DataBaseHelper;
import app_utility.DataReceiverService;
import app_utility.DatabaseHandler;
import app_utility.ImageViewRVAdapter;
import app_utility.OnFragmentInteractionListener;
import app_utility.PermissionHandler;
import app_utility.SharedPreferencesClass;
import app_utility.StaticReferenceClass;
import app_utility.VolleyTask;

import static app_utility.PermissionHandler.WRITE_PERMISSION;
import static app_utility.PermissionHandler.hasPermissions;
import static app_utility.StaticReferenceClass.PRODUCT_URL;
import static app_utility.StaticReferenceClass.WRITE_PERMISSION_CODE;

public class HomeScreenActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    RecyclerView recyclerView;
    FrameLayout flContainer;
    ViewStub stub;
    View inflated;

    private int nPermissionFlag = 0;
    Button btnRefridge;

    Toolbar toolbar;

    ViewStub stubSubMenu;
    View inflatedSubMenu;

    ViewStub stubSubMenu2;
    View inflatedSubMenu2;
    public static OnFragmentInteractionListener onFragmentInteractionListener;

    Button[] btnMenuOne, btnSubMenu;
    LinearLayout llMenuOneParent, llSubMenuParent;
    TextView tvMenuName;
    DatabaseHandler dbh;
    int[] attrs;
    TypedArray ta;
    Drawable drawableFromTheme;
    TypedValue typedValue;
    SharedPreferencesClass sharedPreferencesClass;

    ImageButton ibSearch;
    AutoCompleteTextView actvSearch;
    String itemAtPosition = "";
    ArrayList<String> alMainProducts;

    private FilterAdapter filterAdapter;
    ArrayList<DataBaseHelper> alDb;
    ArrayList<String> alProductNames;
    ArrayList<Integer> alProductsDBID;
    ArrayList<String> alSubCategoryNames;
    ArrayList<String> alDescription;

    private boolean isDataPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        onFragmentInteractionListener = this;
        dbh = new DatabaseHandler(HomeScreenActivity.this);
        sharedPreferencesClass = new SharedPreferencesClass(HomeScreenActivity.this);
        typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);


        /*attrs = new int[] { android.R.attr.selectableItemBackground *//* index 0 *//*};

        // Obtain the styled attributes. 'themedContext' is a context with a
        // theme, typically the current Activity (i.e. 'this')
        ta = obtainStyledAttributes(attrs);
        ta.recycle();
        drawableFromTheme = ta.getDrawable(0 *//* index *//*);
         */
        /*HashMap<String, String> params = new HashMap<>();
        params.put("db", StaticReferenceClass.DB_NAME); //Trufrost-Testing
        params.put("user", StaticReferenceClass.USER_ID);
        params.put("password", StaticReferenceClass.PASSWORD);*/
        Intent in = new Intent(HomeScreenActivity.this, DataReceiverService.class);
        startService(in);


        //VolleyTask volleyTask = new VolleyTask(getApplicationContext(), params, "REQUEST_PRODUCTS", PRODUCT_URL);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ibSearch = toolbar.findViewById(R.id.ib_search);
        actvSearch = toolbar.findViewById(R.id.actv_search);
        actvSearch.setThreshold(1);

        actvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        actvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemAtPosition = parent.getItemAtPosition(position).toString().trim();
                TextView tvID = view.findViewById(R.id.tv_actv_id);
                int nProductID = Integer.valueOf(tvID.getText().toString().trim());
                //int n = alProductNames.indexOf(itemAtPosition);
                //openDisplayIndividualFragment(itemAtPosition, dbh.getDescriptionFromProductName(itemAtPosition), n);
                if(nProductID==-1){
                    nProductID=1;
                }
                openIndividualFragment(itemAtPosition, alDescription.get(nProductID), nProductID);
                ibSearch.setVisibility(View.VISIBLE);
                actvSearch.setText("");
                hideKeyboardFrom(actvSearch);
                actvSearch.setVisibility(View.GONE);
                actvSearch.dismissDropDown();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                //textView.setEnabled(false);
            }
        });

        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alDb = new ArrayList<>(dbh.getDataForSearch());
                if(!isDataPresent) {
                    alProductsDBID = new ArrayList<>();
                    alSubCategoryNames = new ArrayList<>();
                    alProductNames = new ArrayList<>();
                    alDescription = new ArrayList<>();
                    for (int i = 0; i < alDb.size(); i++) {
                        alProductsDBID.add(alDb.get(i).get_id());
                        alSubCategoryNames.add(alDb.get(i).get_product_category_names());
                        alProductNames.add(alDb.get(i).get_individual_product_names());
                        alDescription.add(alDb.get(i).get_individual_product_description());
                    }

                    //alProductNames = new ArrayList<>(dbh.getProductNamesOnly());
                    //alProductsDBID = new ArrayList<>(dbh.getProductDBIDOnly());
                    //filterAdapter = new FilterAdapter(HomeScreenActivity.this, android.R.layout.simple_list_item_1, alProductNames);
                    filterAdapter = new FilterAdapter(HomeScreenActivity.this, R.layout.actv_custom_view, alProductNames);
                /*adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line, alDeliveryOrderNumber);*/

                    actvSearch.setAdapter(filterAdapter);
                    isDataPresent = true;
                }
                actvSearch.setVisibility(View.VISIBLE);
                ibSearch.setVisibility(View.GONE);
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /*stub = findViewById(R.id.fragment_menu);
        stub.setLayoutResource(R.layout.menu_layout);
        inflated = stub.inflate();
        llMenuOneParent = inflated.findViewById(R.id.ll_menu_layout_parent);


        stubSubMenu = findViewById(R.id.fragment_sub_menu);
        stubSubMenu.setLayoutResource(R.layout.sub_menu_layout);
        inflatedSubMenu = stubSubMenu.inflate();
        llSubMenuParent = inflatedSubMenu.findViewById(R.id.ll_sub_menu_parent);
        tvMenuName = inflatedSubMenu.findViewById(R.id.tv_menu_name);

        stubSubMenu2 = findViewById(R.id.fragment_sub_menu_2);
        stubSubMenu2.setLayoutResource(R.layout.sub_menu_layout_2);
        inflatedSubMenu2 = stubSubMenu2.inflate();*/

        //btnRefridge = inflatedSubMenu.findViewById(R.id.btn_refrigde);

        /*btnRefridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductsFragment();
                stubSubMenu2.setVisibility(View.VISIBLE);
                stubSubMenu.setVisibility(View.GONE);
                stub.setVisibility(View.GONE);
            }
        });*/

        recyclerView = findViewById(R.id.rv_products);
        LinearLayoutManager mLinearLayoutManager;
        //flContainer = findViewById(R.id.fl_menu);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLinearLayoutManager = new GridLayoutManager(HomeScreenActivity.this, 3);
            mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            // In landscape
        } else {
            mLinearLayoutManager = new GridLayoutManager(HomeScreenActivity.this, 2);
            mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            // In portrait
        }




       /* int spanCount = 4; // 3 columns
        int spacing = 5; // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));*/

        /*recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position
                int spanCount = 3;
                int spacing = 10;//spacing between views in grid

                if (position >= 0) {
                    int column = position % spanCount; // item column

                    outRect.left = 0;
                    outRect.right = 0;
                    //outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                    //outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount) { // top edge
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing; // item bottom
                } else {
                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.top = 0;
                    outRect.bottom = 0;
                }
            }
        });*/
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(HomeScreenActivity.this, DividerItemDecoration.VERTICAL));

        ImageViewRVAdapter imageViewRVAdapter = new ImageViewRVAdapter(HomeScreenActivity.this, recyclerView, getSupportFragmentManager());
        recyclerView.setAdapter(imageViewRVAdapter);

        /*if (sharedPreferencesClass.getUserLogStatus()) {
            ArrayList<DataBaseHelper> dbData = new ArrayList<>(dbh.getAllMainProducts());
            alMainProducts = new ArrayList<>();
            btnMenuOne = new Button[dbData.size()];
            for (int i = 0; i < btnMenuOne.length; i++) {
                alMainProducts.add(dbData.get(i).get_main_product_names());
                //alMainProducts = new ArrayList<>(dbData.get(i).get_main_product_names());
                //addDynamicContentsForMainMenu(i, alMainProducts);
                final int finalI = i;
                btnMenuOne[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //llMenuOneParent.removeAllViews();
                        String sBtnTag = btnMenuOne[finalI].getTag().toString();
                        ArrayList<DataBaseHelper> al = new ArrayList<>(dbh.getAllMainProducts());
                        ArrayList<String> alSubCategory;
                        String[] subCategory;
                        String sss = null;
                        for (int i = 0; i < al.size(); i++) {
                            if (al.get(i).get_main_product_names().equals(sBtnTag)) {
                                //alSubCategory = new ArrayList<>(Arrays.asList(al.get(i).get_product_category_names().split(",,")));
                                sss = al.get(i).get_product_category_names();
                                break;
                            }
                        }
                        final String[] sCategory = sss.split("##");
                        alSubCategory = new ArrayList<>(Arrays.asList(sCategory));
                        llSubMenuParent.removeAllViews();
                        btnSubMenu = new Button[alSubCategory.size()];
                        for (int i = 0; i < sCategory.length; i++) {
                            //addDynamicContentsForSubMenu(i, alSubCategory);
                            final int finalI = i;
                            btnSubMenu[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //openProductsFragment(sCategory[finalI]);
                                }
                            });
                        }
                        //openMenuFragment(finalI);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        stub.setVisibility(View.GONE);
                        stubSubMenu.setVisibility(View.VISIBLE);
                        tvMenuName.setText(alMainProducts.get(finalI));
                    }
                });
            }
        }*/

        /*TrufrostAsyncTask trufrostAsyncTask = new TrufrostAsyncTask(getApplicationContext());
        trufrostAsyncTask.execute(String.valueOf(2), "");*/
        //openMenuFragment();
    }



    /*public void addDynamicContentsForMainMenu(int i, ArrayList<String> alMainProductName) {
        //Button btnDynamic = new Button(HomeScreenActivity.this);

        btnMenuOne[i] = new Button(HomeScreenActivity.this);

        btnMenuOne[i].setTag(alMainProductName.get(i));

        if (Build.VERSION.SDK_INT < 23) {
            //noinspection deprecation
            btnMenuOne[i].setTextAppearance(HomeScreenActivity.this, R.style.TextAppearance_AppCompat_Medium);
        } else {
            btnMenuOne[i].setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        }
        btnMenuOne[i].setText(alMainProductName.get(i));
        btnMenuOne[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        //btnMenuOne[i].setBackground(drawableFromTheme);
        btnMenuOne[i].setClickable(true);
        btnMenuOne[i].setBackgroundResource(typedValue.resourceId);
        btnMenuOne[i].setAllCaps(false);
        llMenuOneParent.addView(btnMenuOne[i]);
    }

    public void addDynamicContentsForSubMenu(int i, ArrayList<String> alSubCategory) {
        //Button btnDynamic = new Button(HomeScreenActivity.this);

        btnSubMenu[i] = new Button(HomeScreenActivity.this);

        btnSubMenu[i].setTag(alSubCategory.get(i));

        if (Build.VERSION.SDK_INT < 23) {
            //noinspection deprecation
            btnSubMenu[i].setTextAppearance(HomeScreenActivity.this, R.style.TextAppearance_AppCompat_Medium);
        } else {
            btnSubMenu[i].setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        }
        btnSubMenu[i].setText(alSubCategory.get(i));
        btnSubMenu[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        //btnMenuOne[i].setBackground(drawableFromTheme);
        btnSubMenu[i].setClickable(true);
        btnSubMenu[i].setBackgroundResource(typedValue.resourceId);
        btnSubMenu[i].setAllCaps(false);
        llSubMenuParent.addView(btnSubMenu[i]);
    }*/

    @Override
    public void onStart() {
        super.onStart();
        if (!hasPermissions(HomeScreenActivity.this, WRITE_PERMISSION)) {
            ActivityCompat.requestPermissions(HomeScreenActivity.this, WRITE_PERMISSION, WRITE_PERMISSION_CODE);
        } else {
           /* HashMap<String, String> params = new HashMap<>();
            params.put("db", StaticReferenceClass.DB_NAME); //Trufrost-Testing
            params.put("user", StaticReferenceClass.USER_ID);
            params.put("password", StaticReferenceClass.PASSWORD);
            VolleyTask volleyTask = new VolleyTask(getApplicationContext(), params, "REQUEST_PRODUCTS", PRODUCT_URL);*/
            downloadData();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int PERMISSION_ALL, String permissions[], int[] grantResults) {
        StringBuilder sMSG = new StringBuilder();
        if (PERMISSION_ALL == WRITE_PERMISSION_CODE) {
            for (String sPermission : permissions) {
                switch (sPermission) {
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                //Show permission explanation dialog...
                                //showPermissionExplanation(SignInActivity.this.getResources().getString(R.string.phone_explanation));
                                //Toast.makeText(SignInActivity.this, "not given", Toast.LENGTH_SHORT).show();
                                sMSG.append("WRITE_EXTERNAL_STORAGE, ");
                                nPermissionFlag = 0;
                            } else {
                                //Never ask again selected, or device policy prohibits the app from having that permission.
                                //So, disable that feature, or fall back to another situation...
                                //@SuppressWarnings("unused") AlertDialogs alertDialogs = new AlertDialogs(HomeScreen.this, 1, mListener);
                                //Toast.makeText(SignInActivity.this, "permission never ask", Toast.LENGTH_SHORT).show();
                                //showPermissionExplanation(HomeScreenActivity.this.getResources().getString(R.string.phone_explanation));
                                sMSG.append("WRITE_EXTERNAL_STORAGE, ");
                                nPermissionFlag = 0;
                            }
                        } else {
                            downloadData();
                        }
                        break;
                    /*case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                //Show permission explanation dialog...
                                //showPermissionExplanation(SignInActivity.this.getResources().getString(R.string.phone_explanation));
                                //Toast.makeText(SignInActivity.this, "not given", Toast.LENGTH_SHORT).show();
                                sMSG.append("STORAGE, ");
                                nPermissionFlag = 0;
                            } else {
                                //Never ask again selected, or device policy prohibits the app from having that permission.
                                //So, disable that feature, or fall back to another situation...
                                //@SuppressWarnings("unused") AlertDialogs alertDialogs = new AlertDialogs(HomeScreen.this, 1, mListener);
                                //Toast.makeText(SignInActivity.this, "permission never ask", Toast.LENGTH_SHORT).show();
                                //showPermissionExplanation(HomeScreenActivity.this.getResources().getString(R.string.phone_explanation));
                                sMSG.append("STORAGE, ");
                                nPermissionFlag = 0;
                            }
                        }
                        break;*/
                }
            }
            if (!sMSG.toString().equals("") && !sMSG.toString().equals(" ")) {
                PermissionHandler permissionHandler = new PermissionHandler(HomeScreenActivity.this, 0, sMSG.toString(), nPermissionFlag);
            }
        }
    }

    private void downloadData() {
        if (!sharedPreferencesClass.getUserLogStatus()) {
            HashMap<String, String> params = new HashMap<>();
            params.put("db", StaticReferenceClass.DB_NAME); //Trufrost-Testing
            params.put("user", StaticReferenceClass.USER_ID);
            params.put("password", StaticReferenceClass.PASSWORD);
            VolleyTask volleyTask = new VolleyTask(getApplicationContext(), params, "REQUEST_PRODUCTS", PRODUCT_URL);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case WRITE_PERMISSION_CODE:
                downloadData();
                break;
        }
    }

    public void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        int size = getSupportFragmentManager().getBackStackEntryCount();
        if(size==1)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.onBackPressed();
    }

    /*@Override
    public void onBackPressed() {
        int size = getSupportFragmentManager().getBackStackEntryCount();
        if (size >= 1) {
            hideKeyboardFrom(actvSearch);
            actvSearch.dismissDropDown();
        }
        if (size == 2) {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            stub.setVisibility(View.GONE);
            stubSubMenu.setVisibility(View.VISIBLE);
            stubSubMenu2.setVisibility(View.GONE);
        } else if (size == 1) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            stub.setVisibility(View.VISIBLE);
            stubSubMenu.setVisibility(View.GONE);
            stubSubMenu2.setVisibility(View.GONE);
        }
        if (ibSearch.getVisibility() == View.GONE) {
            actvSearch.setVisibility(View.GONE);
            ibSearch.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }*/

    /*private void openMenuFragment(int pos) {
        Fragment newFragment;
        FragmentTransaction transaction;
        //Bundle bundle = new Bundle();
        //bundle.putInt("index", 0);
        newFragment = AboutProductFragment.newInstance(String.valueOf(pos), "");
        //newFragment.setArguments(bundle);

        //String sBackStackParent = newFragment.getClass().getName();
        transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
        transaction.replace(R.id.fl_menu, newFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openProductsFragment(String sTag) {
        Fragment newFragment;
        FragmentTransaction transaction;
        //Bundle bundle = new Bundle();
        //bundle.putString("tag", sTag);
        newFragment = ProductsFragment.newInstance(sTag, "");
        //newFragment.setArguments(bundle);

        //String sBackStackParent = newFragment.getClass().getName();
        transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
        transaction.replace(R.id.fl_menu, newFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openDisplayIndividualFragment(String sName, String sDescription, int dbID) {
        Fragment newFragment;
        FragmentTransaction transaction;
        //Bundle bundle = new Bundle();
        //bundle.putInt("index", 0);

        newFragment = DisplayIndividualFragment.newInstance(sName, sDescription, dbID);
        //newFragment.setArguments(bundle);

        //String sBackStackParent = newFragment.getClass().getName();
        transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
        transaction.replace(R.id.fl_menu, newFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openDisplayEnlargeProductFragment(String sImagesPath) {
        Fragment newFragment;
        FragmentTransaction transaction;
        //Bundle bundle = new Bundle();
        //bundle.putInt("index", 0);
        newFragment = EnlargeProductImage.newInstance(sImagesPath, "");
        //newFragment.setArguments(bundle);

        //String sBackStackParent = newFragment.getClass().getName();
        transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
        transaction.replace(R.id.fl_menu, newFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openDisplayTechnicalFragment() {
        Fragment newFragment;
        FragmentTransaction transaction;
        //Bundle bundle = new Bundle();
        //bundle.putInt("index", 0);
        newFragment = DisplayTechnicalImage.newInstance("", "");
        //newFragment.setArguments(bundle);

        //String sBackStackParent = newFragment.getClass().getName();
        transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
        transaction.replace(R.id.fl_menu, newFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }*/

    private void openSubCategoryFragment(String sMainMenuName) {
        Fragment newFragment;
        FragmentTransaction transaction;
        //Bundle bundle = new Bundle();
        //bundle.putInt("index", 0);
        newFragment = SubCategoryFragment.newInstance(sMainMenuName, "");
        //newFragment.setArguments(bundle);

        //String sBackStackParent = newFragment.getClass().getName();
        transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
        transaction.replace(R.id.fl_container, newFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openIndividualFragment(String sName, String sDescription, int dbID) {
        Fragment newFragment;
        FragmentTransaction transaction;
        //Bundle bundle = new Bundle();
        //bundle.putInt("index", 0);

        newFragment = IndividualFragment.newInstance(sName, sDescription, dbID);
        //newFragment.setArguments(bundle);

        //String sBackStackParent = newFragment.getClass().getName();
        transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
        transaction.add(R.id.fl_container, newFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentMessage(String sMsg, int type, String sResults, String sResult) {

        switch (sMsg) {
            case "SHOW_BACK_BUTTON":
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case "OPEN_SUB_CATEGORY_FRAGMENT":
                //openMenuFragment(type);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                /*stub.setVisibility(View.GONE);
                stubSubMenu.setVisibility(View.VISIBLE);*/
                loadMenuDataAndListener(sResult);
                openSubCategoryFragment(sResult);
                //stub = findViewById(R.id.fragment_menu);
                /*stub.setLayoutResource(R.layout.sub_menu_layout);
                inflated = stub.inflate();*/
                //View inflated = stub.inflate();
                break;
            case "OPEN_DISPLAY_FRAGMENT":
                //openDisplayIndividualFragment(sResults, sResult, type);
                break;
            case "OPEN_DISPLAY_ENLARGE_PRODUCT_IMAGE":
                //openDisplayEnlargeProductFragment(sResults);
                break;
            case "HIDE_BACK_BUTTON":
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case "OPEN_TECHNICAL_FRAGMENT":
                //openDisplayTechnicalFragment();
                break;
            case "OPEN_INDIVIDUAL_FRAGMENT":
                openIndividualFragment(sResults, sResult, type);
                break;
            case "UPDATE_HOME_BUTTON":
                /*ArrayList<DataBaseHelper> dbData = new ArrayList<>(dbh.getAllMainProducts());
                alMainProducts = new ArrayList<>();
                btnMenuOne = new Button[dbData.size()];
                for (int i = 0; i < btnMenuOne.length; i++) {
                    alMainProducts.add(dbData.get(i).get_main_product_names());
                    //alMainProducts = new ArrayList<>(dbData.get(i).get_main_product_names());
                    //addDynamicContentsForMainMenu(i, alMainProducts);
                    final int finalI = i;
                    btnMenuOne[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //llMenuOneParent.removeAllViews();
                            String sBtnTag = btnMenuOne[finalI].getTag().toString();
                            ArrayList<DataBaseHelper> al = new ArrayList<>(dbh.getAllMainProducts());
                            ArrayList<String> alSubCategory;
                            //String[] subCategory;
                            String sss = null;
                            for (int i = 0; i < al.size(); i++) {
                                if (al.get(i).get_main_product_names().equals(sBtnTag)) {
                                    //alSubCategory = new ArrayList<>(Arrays.asList(al.get(i).get_product_category_names().split(",,")));
                                    sss = al.get(i).get_product_category_names();
                                    break;
                                }
                            }
                            final String[] sCategory = sss.split("##");
                            alSubCategory = new ArrayList<>(Arrays.asList(sCategory));
                            llSubMenuParent.removeAllViews();
                            btnSubMenu = new Button[alSubCategory.size()];
                            for (int i = 0; i < sCategory.length; i++) {
                                //addDynamicContentsForSubMenu(i, alSubCategory);
                                final int finalI = i;
                                btnSubMenu[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //openProductsFragment(sCategory[finalI]);
                                    }
                                });
                            }
                            //openMenuFragment(finalI);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            stub.setVisibility(View.GONE);
                            stubSubMenu.setVisibility(View.VISIBLE);
                            tvMenuName.setText(alMainProducts.get(finalI));
                            *//*switch (sBtnTag) {
                                case "Commercial Kitchen":
                                    ArrayList<DataBaseHelper> al = new ArrayList<>(dbh.getAllMainProducts());
                                    ArrayList<String> alSubCategory;
                                    String[] subCategory;
                                    String sss = null;
                                    for(int i=0; i<al.size(); i++){
                                        if(al.get(i).get_main_product_names().equals("Commercial Kitchen")){
                                            //alSubCategory = new ArrayList<>(Arrays.asList(al.get(i).get_product_category_names().split(",,")));
                                            sss = al.get(i).get_product_category_names();
                                            break;
                                        }
                                    }
                                    String[] sCategory = sss.split(",,");
                                    alSubCategory = new ArrayList<>(Arrays.asList(sCategory));
                                    btnSubMenu = new Button[alSubCategory.size()];
                                    for (int i=0; i<sCategory.length; i++){
                                        addDynamicContentsForSubMenu(i, alSubCategory);
                                        final int finalI = i;
                                        btnSubMenu[i].setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                openDisplayIndividualFragment();
                                            }
                                        });
                                    }
                                    openMenuFragment();
                                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                    stub.setVisibility(View.GONE);
                                    stubSubMenu.setVisibility(View.VISIBLE);
                                    break;
                                case "Bars & Pubs":
                                    break;
                                case "Cakes & Sweets Shop":
                                    break;
                                case "Food Retails":
                                    break;
                                case "Food Preservation":
                                    break;
                                case "Biomedical":
                                    break;
                            }*//*
                        }
                    });
                }*/

                sharedPreferencesClass.setUserLogStatus(true);
                break;
            case "UPDATE_SUB_MENU_BUTTONS":
                /*ArrayList<DataBaseHelper> dbData1 = new ArrayList<>(dbh.getAllMainProducts());
                ArrayList<String> alSubProducts = new ArrayList<>();
                btnMenuOne = new Button[dbData1.size()];
                for (int i = 0; i < btnMenuOne.length; i++) {
                    alSubProducts.add(dbData1.get(i).get_product_category_names());
                    addDynamicContentsForSubMenu(i, alSubProducts);
                }*/
                break;
        }
    }

    private void loadMenuDataAndListener(String sResult) {
        ArrayList<DataBaseHelper> al = new ArrayList<>(dbh.getAllMainProducts());
        ArrayList<String> alSubCategory;
        //String[] subCategory;
        String sss = null;
        for (int i = 0; i < al.size(); i++) {
            if (al.get(i).get_main_product_names().equals(sResult)) {
                //alSubCategory = new ArrayList<>(Arrays.asList(al.get(i).get_product_category_names().split(",,")));
                sss = al.get(i).get_product_category_names();
                break;
            }
        }
        /*if (sss != null && !sss.equals("")) {
            final String[] sCategory = sss.split("##");
            alSubCategory = new ArrayList<>(Arrays.asList(sCategory));
            llSubMenuParent.removeAllViews();
            btnSubMenu = new Button[alSubCategory.size()];
            for (int i = 0; i < sCategory.length; i++) {
                //addDynamicContentsForSubMenu(i, alSubCategory);
                final int finalI = i;
                btnSubMenu[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //openProductsFragment(sCategory[finalI]);
                    }
                });
            }
        }*/
        /*stub.setVisibility(View.GONE);
        tvMenuName.setText(sResult);
        stubSubMenu.setVisibility(View.VISIBLE);*/
    }

    public class FilterAdapter extends ArrayAdapter<String> implements Filterable {

        ArrayList<String> originalList;
        ArrayList<String> filteredList;

        ArrayList<Integer> alProductsDBID = new ArrayList<>();

        public FilterAdapter(Context context, int textViewResourceId, ArrayList<String> item) {
            super(context, textViewResourceId, item);
            filteredList = item;
            originalList = new ArrayList<>(filteredList);
        }

        @Override
        public int getCount() {
            return filteredList.size();
        }

        @Override
        public String getItem(int position) {
            return filteredList.get(position);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.actv_custom_view, parent, false);
            }
            if(filteredList.size()>=1 && position < filteredList.size() && alProductsDBID.size()>=1 && position < alProductsDBID.size()) {
                TextView tvName = convertView.findViewById(R.id.tv_actv_name);
                tvName.setText(filteredList.get(position));
                TextView tvID = convertView.findViewById(R.id.tv_actv_id);
                if (alProductsDBID != null)
                    tvID.setText(String.valueOf(alProductsDBID.get(position) + 1));
            }
            return convertView;
        }
        /*@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            super.getView(position, convertView, parent);

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(itemLayout, parent, false);
            }

            return convertView;
        }*/
        /*@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            super.getView(position, convertView, parent);
            TextView tv;

            if(convertView!= null)
                tv = (TextView)convertView;
            else
                tv = new TextView(HomeScreenActivity.this);

            //changing text size and adding icons to sightseer and destination heading
            *//*if(position == 0)
            {
                tv.setText(filteredList.get(position));
                tv.setTextSize(autoCompleteTextView.getTextSize() - 1);
                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.cognito);
                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
                tv.setTextColor(Color.parseColor("#999999"));
            }
            else if(filteredList.get(position).contains("Destination"))
            {
                tv.setText(filteredList.get(position));
                tv.setTextSize(autoCompleteTextView.getTextSize() - 1);
                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.favicon);
                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
                tv.setTextColor(Color.parseColor("#999999"));
            }
            else{
                tv.setText(filteredList.get(position));
                tv.setTextSize(autoCompleteTextView.getTextSize() - 5);
            }*//*
            return tv;
        }*/
        @Override
        public Filter getFilter() {
            return nameFilter;
        }

        Filter nameFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                String str = (String) resultValue;
                return str;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    filteredList.clear();
                    alProductsDBID = new ArrayList<>();
                    /*for (String item : originalList) {
                        if (item.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(item);
                        }
                    }*/
                    for (int i=0; i<originalList.size(); i++) {
                        String item = originalList.get(i);
                        if (item.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(item);
                            alProductsDBID.add(i);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredList;
                    filterResults.count = filteredList.size();
                    return filterResults;
                } else {
                    //filteredList = originalList;
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                /*List<String> filterList = (ArrayList<String>) results.values;
                if (results.count > 0) {
                    //clear();
                    for (String item : filterList) {
                        add(item);

                    }
                    notifyDataSetChanged();
                }*/
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    //filterAdapter = new FilterAdapter(HomeScreenActivity.this, android.R.layout.simple_dropdown_item_1line, originalList);
                    filterAdapter = new FilterAdapter(HomeScreenActivity.this, R.layout.actv_custom_view, originalList);
                /*adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line, alDeliveryOrderNumber);*/

                    actvSearch.setAdapter(filterAdapter);
                    if (actvSearch.getText().length() >= 1)
                        actvSearch.showDropDown();
                }
            }
        };
    }
}
