package app_utility;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.autochip.trufrost.HomeScreenActivity;
import com.autochip.trufrost.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class VolleyTask {

    private Context context;
    private int mStatusCode = 0;
    //private JSONObject jsonObject = new JSONObject();
    private HashMap<String, String> params;
    //private int position;
    private String msg;
    //String sDescription;

    private int ERROR_CODE = 0;

    /*ArrayList<String> alSubCategory;
    ArrayList<String> alMainCategory;
    //ArrayList<Integer> alID;
    ArrayList<Integer> alProductName;
    ArrayList<Integer> alProductSubCategory;

    private HashMap<Integer, String> hmImageAddressWithDBID = new HashMap<>();*/

    private LinkedHashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> lhmMainCategory = new LinkedHashMap<>();
    //private LinkedHashMap<String, HashMap<String, ArrayList<String>>> lhmSubCategory = new LinkedHashMap<>();

    private LinkedHashMap<String, String> lhmTags = new LinkedHashMap<>();
    //int stockFlag;
    private String URL;
    //JSONObject jsonObject = new JSONObject();
    private DatabaseHandler dbh;

    /*public VolleyTask(Context context, JSONObject jsonObject, String sCase, int stockFlag, String URL) {
        this.context = context;
        this.jsonObject = jsonObject;
        this.stockFlag = stockFlag;
        this.URL = URL;
        Volley(sCase);
    }*/

    public VolleyTask(Context context, HashMap<String, String> params, String sCase, String URL) {
        this.context = context;
        this.params = params;
        this.URL = URL;
        Volley(sCase);
        dbh = new DatabaseHandler(context);
    }

    private void Volley(String sCase) {
        switch (sCase) {
            case "REQUEST_PRODUCTS":
                requestProducts(URL);
                break;
            case "ODOO_LOGIN":
                //loginOdoo();
                break;
        }
    }

    private void requestProducts(String URL) {

        StringRequest request = new StringRequest(
                Request.Method.POST, URL, //BASE_URL + Endpoint.USER
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Success
                        onPostProductsReceived(mStatusCode, response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //NetworkResponse networkResponse = error.networkResponse;
                        msg = "No response from Server";
                    }
                }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            public byte[] getBody() { //throws AuthFailureError
                return new JSONObject(params).toString().getBytes();
                //return params.toString().g    etBytes();
                //return params.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));

        /*request.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(request);

    }

    /*private void loginOdoo() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            }
        });

    }*/


    private void onPostProductsReceived(int mStatusCode, String response) {
        if (mStatusCode == 200) {
            JSONObject jsonObject;
            int sResponseCode;
            //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
            try {
                jsonObject = new JSONObject(response);
                String sResult = jsonObject.getString("result");
                jsonObject = new JSONObject(sResult);
                sResponseCode = jsonObject.getInt("response_code");
            } catch (Exception e) {
                e.printStackTrace();
                /*ERROR_CODE = 900;
                msg = "No IDS matched";
                e.printStackTrace();
                sendMsgToActivity();*/
                return;
            }
            if (sResponseCode == 0) {
                msg = "Unable to connect to server, please try again later";
                sendMsgToActivity();
                return;
            }

            switch (sResponseCode) {
                case 201: //success
                    ERROR_CODE = 201;


                    try {
                        //sDescription = jsonObject.getString("description");
                        msg = jsonObject.getString("message");
                        JSONArray jsonArray = new JSONArray(msg);

                        HashMap<String, ArrayList<String>> hm;
                        HashMap<String, HashMap<String, ArrayList<String>>> hmSecondCategory;
                        HashMap<String, String> hmFirstSubCategoryImageURL = new HashMap<>();
                        HashMap<String, String> hmSecondSubCategoryImageURL = new HashMap<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            //String product = jsonArray.getJSONObject(i).getString("product");
                            //String quantity = jsonArray.getJSONObject(i).getString("quantity_received");
                            String sMainCategory = jsonArray.getJSONObject(i).getString("maincategory");
                            String sFirstSubCategoryName = jsonArray.getJSONObject(i).getString("subcategory1");
                            String sFirstSubCategoryImageURL = jsonArray.getJSONObject(i).getString("sub_categ_image2");
                            String sSecondSubCategoryName = jsonArray.getJSONObject(i).get("subcategory").toString();
                            String sSecondSubCategoryImageURL = jsonArray.getJSONObject(i).getString("sub_categ_image1");
                            String sIndividualProductName = jsonArray.getJSONObject(i).getString("name");
                            String sIndividualProductImageURL = jsonArray.getJSONObject(i).getString("image");
                            String sIndividualProductDescription = jsonArray.getJSONObject(i).getString("description");
                            String sIndividualProductTechSpecHeader = jsonArray.getJSONObject(i).getString("techspecheader");
                            String sIndividualProductTechSpecValue = jsonArray.getJSONObject(i).getString("techspecvalue");
                            String sIndividualProductTags = jsonArray.getJSONObject(i).getString("tags");
                            String sProductOdooID = jsonArray.getJSONObject(i).getString("id");


                            if (!sFirstSubCategoryImageURL.equals("null") && !sFirstSubCategoryImageURL.equals("")) {
                                hmFirstSubCategoryImageURL.put(sFirstSubCategoryName, sFirstSubCategoryImageURL);
                            }
                            if (!sSecondSubCategoryImageURL.equals("null") && !sSecondSubCategoryImageURL.equals("")) {
                                hmSecondSubCategoryImageURL.put(sSecondSubCategoryName, sSecondSubCategoryImageURL);
                            }

                            StringBuilder sb = new StringBuilder();
                            sb.append(sMainCategory);
                            sb.append("##");
                            sb.append(sFirstSubCategoryName);
                            sb.append("##");
                            sb.append(sFirstSubCategoryImageURL);
                            sb.append("##");
                            sb.append(sSecondSubCategoryName);
                            sb.append("##");
                            sb.append(sSecondSubCategoryImageURL);
                            sb.append("##");
                            sb.append(sIndividualProductName);
                            sb.append("##");
                            sb.append(sIndividualProductImageURL);
                            sb.append("##");
                            sb.append(sIndividualProductDescription);
                            sb.append("##");
                            sb.append(sIndividualProductTechSpecHeader);
                            sb.append("##");
                            sb.append(sIndividualProductTechSpecValue);
                            sb.append("##");
                            sb.append(sIndividualProductTags);
                            sb.append("##");
                            sb.append(sProductOdooID);

                            ArrayList<String> alProducts = new ArrayList<>();

                            lhmTags.put(sSecondSubCategoryName, sIndividualProductTags);

                            hm = new HashMap<>();
                            if (lhmMainCategory.get(sMainCategory) == null) {
                                hmSecondCategory = new HashMap<>();
                                alProducts.add(sb.toString());
                                hm.put(sSecondSubCategoryName, alProducts);
                                hmSecondCategory.put(sFirstSubCategoryName, hm);
                                lhmMainCategory.put(sMainCategory, hmSecondCategory);
                            } else if (lhmMainCategory.get(sMainCategory) != null) {
                                ArrayList<String> altmp;
                                hmSecondCategory = new HashMap<>(lhmMainCategory.get(sMainCategory));
                                if (hmSecondCategory.get(sFirstSubCategoryName) != null) {
                                    hm = hmSecondCategory.get(sFirstSubCategoryName);
                                    if (hm.get(sSecondSubCategoryName) != null) {
                                        altmp = new ArrayList<>(hm.get(sSecondSubCategoryName));
                                        altmp.add(sb.toString());
                                        hm.put(sSecondSubCategoryName, altmp);
                                        hmSecondCategory.put(sFirstSubCategoryName, hm);
                                        lhmMainCategory.put(sMainCategory, hmSecondCategory);
                                    } else {
                                        alProducts.add(sb.toString());
                                        hm = new HashMap<>(hmSecondCategory.get(sFirstSubCategoryName));
                                        //hm = new HashMap<>();
                                        hm.put(sSecondSubCategoryName, alProducts);
                                        //hmSecondCategory = new HashMap<>(lhmMainCategory.get(sMainCategory));
                                        hmSecondCategory.put(sFirstSubCategoryName, hm);
                                        lhmMainCategory.put(sMainCategory, hmSecondCategory);
                                    }
                                } else {
                                    alProducts.add(sb.toString());
                                    hm = new HashMap<>();
                                    hm.put(sSecondSubCategoryName, alProducts);
                                    hmSecondCategory.put(sFirstSubCategoryName, hm);
                                    lhmMainCategory.put(sMainCategory, hmSecondCategory);
                                }
                            }
                        }
                        ArrayList<String> alMainMenuKey = new ArrayList<>(lhmMainCategory.keySet());

                        for (int i = 0; i < alMainMenuKey.size(); i++) {
                            String sMainCategoryName = alMainMenuKey.get(i);
                            hmSecondCategory = new HashMap<>(lhmMainCategory.get(sMainCategoryName));
                            ArrayList<String> alFirstSubCategoryNames = new ArrayList<>(hmSecondCategory.keySet());
                            ArrayList<String> alFirstSubCategoryImageURL = new ArrayList<>();
                            //ArrayList<String> alFirstSubCategoryImageURL = new ArrayList<>(hmFirstSubCategoryImageURL.keySet());

                            for (int j = 0; j < alFirstSubCategoryNames.size(); j++) {
                                //if(hmFirstSubCategoryImageURL.containsKey(alFirstSubCategoryNames)){
                                alFirstSubCategoryImageURL.add(hmFirstSubCategoryImageURL.get(alFirstSubCategoryNames.get(j)));
                                //}
                            }

                            String sFirstSubCategory = android.text.TextUtils.join("##", alFirstSubCategoryNames);
                            String sFirstSCImagesURL = android.text.TextUtils.join("##", alFirstSubCategoryImageURL);
                            //continue from here
                            dbh.addDataToMainProducts(new DataBaseHelper(sMainCategoryName, switchDescription(alMainMenuKey.get(i)), sFirstSubCategory, sFirstSCImagesURL));
                            int mainID = dbh.getIdForStringTablePermanent(sMainCategoryName);
                            //ArrayList<DataBaseHelper>alFirstSCData = new ArrayList<>(dbh.getAllMainProducts());

                            /*if (DataReceiverService.refOfService != null) {

                                String sData = mainID + "##" + sFirstSCImagesURL + "##" + "1";
                                String[] sSplitData = sData.split("##");
                                ArrayList<String> alMultipleUrl = new ArrayList<>(Arrays.asList(sSplitData[1].split(",")));
                                if (alMultipleUrl.size() > 1) {
                                    for (int l = 0; l < alMultipleUrl.size(); l++) {
                                        String sMultiple = mainID + "##" + alMultipleUrl.get(l) + "##" + "1";
                                        DataReceiverService.refOfService.dataStorage.alDBIDWithAddress.add(sMultiple);
                                        DataReceiverService.refOfService.dataStorage.isDataUpdatedAtleastOnce = true;
                                    }
                                } else {
                                    DataReceiverService.refOfService.dataStorage.alDBIDWithAddress.add(sData);
                                    DataReceiverService.refOfService.dataStorage.isDataUpdatedAtleastOnce = true;
                                }
                            }*/

                            for(int n=0; n<alFirstSubCategoryNames.size();n++) {
                                String sFirstSubCategoryName = alFirstSubCategoryNames.get(n);
                                ArrayList<String> alSecondSubCategoryNames = new ArrayList<>(hmSecondCategory.get(sFirstSubCategoryName).keySet());
                                ArrayList<String> alSecondSubCategoryImageURL = new ArrayList<>();

                                ArrayList<String> alTagKeys = new ArrayList<>(lhmTags.keySet());
                                for (int k = 0; k < alTagKeys.size(); k++) {
                                    String sSubCategory = alTagKeys.get(k);
                                    ArrayList<String> alFirstSubCategory = new ArrayList<>(Arrays.asList(lhmTags.get(sSubCategory).split(",")));
                                    for (int j = 0; j < alFirstSubCategory.size(); j++) {
                                        String sTag = alFirstSubCategory.get(j);
                                        if (!alSecondSubCategoryNames.contains(sTag) && sTag.equals(sFirstSubCategoryName)) {
                                            alSecondSubCategoryNames.add(sTag);
                                        }
                                    }
                                }

                                for (int j = 0; j < alSecondSubCategoryNames.size(); j++) {
                                    alSecondSubCategoryImageURL.add(hmSecondSubCategoryImageURL.get(alSecondSubCategoryNames.get(j)));
                                }

                                String sSecondSubCategoryName = android.text.TextUtils.join("##", alSecondSubCategoryNames);
                                String sSecondSCImagesURL = android.text.TextUtils.join("##", alSecondSubCategoryImageURL);

                                String sFirstImageURL = alFirstSubCategoryImageURL.get(alFirstSubCategoryNames.indexOf(sFirstSubCategoryName));
                                dbh.addDataToSubCategoryTable(new DataBaseHelper(mainID, sMainCategoryName, sFirstSubCategoryName,
                                        sFirstImageURL, sSecondSubCategoryName, sSecondSCImagesURL));
                                int nFirstSCID = dbh.getIdForStringTableSecondSubCategory(sFirstSubCategoryName);


                                if (DataReceiverService.refOfService != null) {

                                    String sData = nFirstSCID + "##" + sSecondSCImagesURL + "##" + "2";
                                    //String[] sSplitData = sData.split("##");
                                    ArrayList<String> alMultipleUrl = new ArrayList<>(Arrays.asList(sSecondSCImagesURL.split("#")));
                                    if (alMultipleUrl.size() > 1) {
                                        for (int l = 0; l < alMultipleUrl.size(); l++) {
                                            String sMultiple = nFirstSCID + "##" + alMultipleUrl.get(l) + "##" + "2";
                                            DataReceiverService.refOfService.dataStorage.alDBIDWithAddress.add(sMultiple);
                                            DataReceiverService.refOfService.dataStorage.isDataUpdatedAtleastOnce = true;
                                        }
                                    } else {
                                        DataReceiverService.refOfService.dataStorage.alDBIDWithAddress.add(sData);
                                        DataReceiverService.refOfService.dataStorage.isDataUpdatedAtleastOnce = true;
                                    }

                                    sData = nFirstSCID + "##" + sFirstImageURL + "##" + "4";
                                    DataReceiverService.refOfService.dataStorage.alDBIDWithAddress.add(sData);
                                    DataReceiverService.refOfService.dataStorage.isDataUpdatedAtleastOnce = true;
                                }

                                for (int l = 0; l < alSecondSubCategoryNames.size(); l++) {
                                    hm = hmSecondCategory.get(sFirstSubCategoryName);
                                    ArrayList<String> alSecondCategoryTemp = new ArrayList<>(hm.get(alSecondSubCategoryNames.get(l)));

                                    for (int p=0; p<alSecondCategoryTemp.size(); p++) {
                                        ArrayList<String> alSecondCategory = new ArrayList<>(Arrays.asList(alSecondCategoryTemp.get(p).split("##")));
                                        dbh.addDataToIndividualProducts(new DataBaseHelper(mainID, Integer.valueOf(alSecondCategory.get(11)), nFirstSCID, sMainCategoryName, sFirstSubCategoryName,
                                                alSecondSubCategoryNames.get(l), alSecondCategory.get(5), alSecondCategory.get(7), alSecondCategory.get(8),
                                                alSecondCategory.get(9), alSecondCategory.get(6), "", lhmTags.get(alSecondSubCategoryNames.get(l))));

                                        int id = dbh.getRecordsCount();
                                        if (DataReceiverService.refOfService != null) {

                                            String sData = id + "##" + alSecondCategory.get(6); //+ alSecondCategory.get(5);
                                            String[] sSplitData = sData.split("##");
                                            ArrayList<String> alMultipleUrl = new ArrayList<>(Arrays.asList(sSplitData[1].split(",")));
                                            if (alMultipleUrl.size() > 1) {
                                                for (int m = 0; m < alMultipleUrl.size(); m++) {
                                                    String sMultiple = id + "##" + alMultipleUrl.get(m);
                                                    DataReceiverService.refOfService.dataStorage.alDBIDWithAddress.add(sMultiple);
                                                    DataReceiverService.refOfService.dataStorage.isDataUpdatedAtleastOnce = true;
                                                }
                                            } else {
                                                DataReceiverService.refOfService.dataStorage.alDBIDWithAddress.add(sData);
                                                DataReceiverService.refOfService.dataStorage.isDataUpdatedAtleastOnce = true;
                                            }
                                        }
                                    }
                                }
                            }


                        }
                        sendMsgToActivity();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ERROR_CODE = 901;
                        msg = "Unable to reach server, please try again";
                        sendMsgToActivity();
                    }
                    break;
                /*case 201:
                    ERROR_CODE = 201;
                    try {
                        msg = jsonObject.getString("message");
                        JSONArray jsonArray = new JSONArray(msg);
                        //JSONObject jsonObject1;
                        alData = new ArrayList<>();
                        alID = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String product = jsonArray.getJSONObject(i).getString("name");
                            String quantity = jsonArray.getJSONObject(i).getString("quantity_done");

                            alData.add(product);
                            alID.add(Integer.valueOf(quantity));
                        }
                        sendMsgToActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 202:
                    try {
                        ERROR_CODE = 202;
                        msg = jsonObject.getString("message");
                        sendMsgToActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 203:
                    try {
                        ERROR_CODE = 203;
                        msg = jsonObject.getString("message");
                        sendMsgToActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 300: //RFID not exist
                    try {
                        ERROR_CODE = 300;
                        msg = jsonObject.getString("message");
                        sendMsgToActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 204: //authentication failed(wrong password)
                    //snackBarToast = new SnackBarToast(aActivity, aActivity.getResources().getString(R.string.wrong_password));
                    break;
                case 402:
                    try {
                        ERROR_CODE = 402;
                        msg = jsonObject.getString("message");
                        sendMsgToActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;*/
            }
        }
        /*if (circularProgressBar != null && circularProgressBar.isShowing()) {
            circularProgressBar.dismiss();
        }*/
    }

    private String switchDescription(String sKey) {
        String sResult = null;
        switch (sKey) {
            case "Commercial Kitchens":
                sResult = context.getResources().getString(R.string.commercial_kitchens);
                break;
            case "Bars & Pubs":
                sResult = context.getResources().getString(R.string.bars_pubs);
                break;
            case "Cake & Sweet Shops":
                sResult = context.getResources().getString(R.string.cake_sweet_shop);
                break;
            case "Food Retail":
                sResult = context.getResources().getString(R.string.food_retail);
                break;
            case "Food Preservation":
                sResult = context.getResources().getString(R.string.food_preservation);
                break;
            case "Biomedical":
                sResult = context.getResources().getString(R.string.bio_medical);
                break;

        }
        return sResult;
    }

    private void sendMsgToActivity() {
        try {
            HomeScreenActivity.onFragmentInteractionListener.onFragmentMessage("UPDATE_HOME_BUTTON", 0, "", "");
            //DataReceiverService.onServiceInterfaceListener.onServiceMessage("START_TECH");
            //onServiceInterface.onServiceCall("RFID", ERROR_CODE, String.valueOf(this.jsonObject.get("rfids")), msg, alID, alData);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
