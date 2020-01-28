package app_utility;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.OdooUser;
import oogbox.api.odoo.client.AuthError;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.AuthenticateListener;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

import static app_utility.StaticReferenceClass.DB_NAME;
import static app_utility.StaticReferenceClass.NETWORK_ERROR_CODE;
import static app_utility.StaticReferenceClass.PASSWORD;
import static app_utility.StaticReferenceClass.PORT_NO;
import static app_utility.StaticReferenceClass.SERVER_URL;
import static app_utility.StaticReferenceClass.USER_ID;

public class TrufrostAsyncTask extends AsyncTask<String, Void, String> {

    private LinkedHashMap<String, ArrayList<String>> lhmProductsWithID = new LinkedHashMap<>();
    //private CircularProgressBar circularProgressBar;
    //private Activity aActivity;
    //private OnAsyncTaskInterface onAsyncTaskInterface;
    private ArrayList<Integer> alPosition = new ArrayList<>();
    private Context context;
    private HashMap<String, Object> hmDataList = new HashMap<>();
    private int nOrderID = 191;
    private int odooID = StaticReferenceClass.DEFAULT_ODOO_ID;

    private int nTemporaryDBID;
    private ArrayList<String> alEmpID = new ArrayList<>();
    private ArrayList<String> alAmount = new ArrayList<>();
    private ArrayList<String> alTime = new ArrayList<>();
    //private ArrayList<String> alEmpID = new ArrayList<>();

    ArrayList<DataBaseHelper> alDBTemporaryData;
    DatabaseHandler dbh;
    Integer[] nOdooIDArray;

    ArrayList<Integer> alIDFetched;

    public TrufrostAsyncTask(Context context) {
        this.context = context;
    }

    public TrufrostAsyncTask(Context context, DatabaseHandler dbh, Integer[] nOdooIDArray) {
        this.context = context;
        this.dbh = dbh;
        this.nOdooIDArray = nOdooIDArray;
    }

    public TrufrostAsyncTask(Context context, ArrayList<DataBaseHelper> alDBTemporaryData, DatabaseHandler dbh) {
        this.context = context;
        this.alDBTemporaryData = alDBTemporaryData;
        this.dbh = dbh;
    }
    /*public BELAsyncTask(Activity aActivity, OnAsyncTaskInterface onAsyncTaskInterface,
                        HashMap<String, Object> hmDataList) {
        this.aActivity = aActivity;
        this.hmDataList = hmDataList;
        this.onAsyncTaskInterface = onAsyncTaskInterface;
    }*/

    private boolean isPresent = false;
    private Boolean isConnected = false;
    private int ERROR_CODE = 0;
    private String sMsgResult;
    private int type;
    private String sStallName;
    private int[] IDS = new int[2];
    String sEmpID;
    String sAmount;
    String sTime;
    String sScannedID;

    OdooClient client;
    AuthenticateListener loginCallback;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //setProgressBar();
    }

    @Override
    protected String doInBackground(String... params) {
        type = Integer.parseInt(params[0]);
        switch (type) {
            case 1:
                loginTask();
                break;
            case 2:
               /* ArrayList<Integer> alOdooID = new ArrayList<>(dbh.getProductsOdooID());

                Integer[] nOdooIDArray;
                if(alOdooID.size()>30){
                    nOdooIDArray = new Integer[30];
                    for(int i = 0; i < 30; i++) {
                        nOdooIDArray[i] = alOdooID.get(i);
                        alOdooID.remove(i);
                    }
                } else {
                    nOdooIDArray = new Integer[alOdooID.size()];
                    alOdooID.toArray(nOdooIDArray);
                }*/

                /*Integer[] nOdooIDArray = new Integer[alOdooID.size()];
                alOdooID.toArray(nOdooIDArray);*/


                //String[] namesArr = (String[]) alOdooID.toArray(new String[alOdooID.size()]);

                validateLoginViaLibrary(nOdooIDArray);
                //validateLogin(nOdooIDArray);
                //getData();
                //updateTask();
                break;
            case 3:
                placeOrder();
                break;
            case 4:
                readProductAndImageTask();
                //snapRoadTask(params[1]);
                //readProducts();
                break;
            case 5:
                //sStallName = params[1];
                //validateLogin();
                break;
            case 6:
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (ERROR_CODE != 0) {
            switch (ERROR_CODE) {
                case NETWORK_ERROR_CODE:
                    unableToConnectServer(ERROR_CODE);
                    break;
            }
            ERROR_CODE = 0;
            return;
        }
        switch (type) {
            case 2:
                //TechnicalSpecService.onServiceInterfaceListener.onServiceMessage("TASK_COMPLETE", alIDFetched);
                //AdminRegisterService.onAsyncInterfaceListener.onAsyncComplete("ODOO_ID_RETRIEVED", odooID, "");
                break;
            case 4:
                //onAsyncTaskInterface.onAsyncTaskComplete("READ_PRODUCTS", type, lhmProductsWithID, alPosition);
                break;
            case 5:
                break;
            case 6:
                break;

        }
        /*if (circularProgressBar != null && circularProgressBar.isShowing()) {
            circularProgressBar.dismiss();
        }*/
    }

    /*private void loginTask() {
        //if (isConnected) {
        try {
            isConnected = OdooConnect.testConnection(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            if (isConnected) {
                isConnected = true;
                //return true;
            } else {
                isConnected = false;
                sMsgResult = "Connection error";
            }
        } catch (Exception ex) {
            ERROR_CODE = NETWORK_ERROR_CODE;
            // Any other exception
            sMsgResult = "Error: " + ex;
        }
        // }
        //return isConnected;
    }*/

    private void loginTask() {
        //if (isConnected) {
        try {
            isConnected = OdooConnect.testConnection("https://trufrost.odoo.com", 443,
                    "trufrosts-master-434286", "admin", "2019@autochip");
            if (isConnected) {
                isConnected = true;
                //return true;
            } else {
                isConnected = false;
                sMsgResult = "Connection error";
            }
        } catch (Exception ex) {
            ERROR_CODE = NETWORK_ERROR_CODE;
            // Any other exception
            sMsgResult = "Error: " + ex;
        }
        // }
        //return isConnected;
    }

    private void validateLoginViaLibrary(final Integer[] nOdooIDArray) {

        client = new OdooClient.Builder(context)
                .setHost(StaticReferenceClass.SERVER_URL)
                .setConnectListener(new OdooConnectListener() {
                    @Override
                    public void onConnected(OdooVersion version) {
                        client.authenticate(USER_ID, PASSWORD, DB_NAME, loginCallback);
                    }
                }).build();

        loginCallback = new AuthenticateListener() {
            @Override
            public void onLoginSuccess(OdooUser user) {
                ODomain domain = new ODomain();
                domain.add("id", "=", nOdooIDArray);

                String[] fields = new String[103];
                fields[0] = "blood_bags_id";
                fields[1] = "ambient_id";
                fields[2] = "baking_plate_revolve_id";
                fields[3] = "boiler_capacity_id";
                fields[4] = "bottle_storage_id";
                fields[5] = "bowl_capacity_id";
                fields[6] = "bowl_speed_id";
                fields[7] = "cabinet_case_id";
                fields[8] = "capacity_in_cft_id";
                fields[9] = "capacity_in_gallons_id";
                fields[10] = "capacity_in_litre_id";
                fields[11] = "cavity_dimension_id";
                fields[12] = "cavity_voulme_id";
                fields[13] = "compatible_storage_bin_id";
                fields[14] = "consecutive_dispensing_id";
                fields[15] = "cooking_surface_id";
                fields[16] = "cooking_time_id";
                fields[17] = "cooling_system_id";
                fields[18] = "cube_shape_id";
                fields[19] = "current_id";
                fields[20] = "defrost_id";
                fields[21] = "digital_temp_indicator_id";
                fields[22] = "dimensions_id";
                fields[23] = "door_handle_id";
                fields[24] = "drive_motor_id";
                fields[25] = "electricals_id";
                fields[26] = "external_dimension_id";
                fields[27] = "fans_id";
                fields[28] = "first_dispensing_id";
                fields[29] = "flavours_id";
                fields[30] = "freezing_cylinder_id";
                fields[31] = "freezing_cylinder_capacity_id";
                fields[32] = "fry_basket_id";
                fields[33] = "frypot_oil_id";
                fields[34] = "gas_input_id";
                fields[35] = "glass_door_id";
                fields[36] = "gn_en_id";
                fields[37] = "griddle_dimension_id";
                fields[38] = "grinding_burrs_id";
                fields[39] = "gross_volumels_id";

                fields[40] = "heat_load_id";
                fields[41] = "hopper_capacity_id";
                fields[42] = "ice_storage_id";
                fields[43] = "input_power_id";
                fields[44] = "interior_dimensions_id";
                fields[45] = "interior_light_id";
                fields[46] = "jar_capacity_id";
                fields[47] = "lighting_under_id";
                fields[48] = "loading_temp_id";
                fields[49] = "lock_id";
                fields[50] = "max_kneading_id";
                fields[51] = "max_loading_id";
                fields[52] = "max_production_id";
                fields[53] = "max_room_area_id";
                fields[54] = "max_room_volume_id";
                fields[55] = "mix_hopper_id";
                fields[56] = "mixing_speed_id";
                fields[57] = "motor_power_id";
                fields[58] = "net_weight_id";
                fields[59] = "no_of_baskets_id";
                fields[60] = "no_of_beech_id";
                fields[61] = "no_of_compressor_id";
                fields[62] = "no_of_doors_id";
                fields[63] = "no_of_layers_id";
                fields[64] = "no_of_lids_id";
                fields[65] = "no_of_shelves_id";
                fields[66] = "power_id";
                fields[67] = "power_supply_id";
                fields[68] = "power_consumption_id";
                fields[69] = "product_dimension_id";
                fields[70] = "product_weight_id";
                fields[71] = "rated_capacity_id";
                fields[72] = "rated_input_ower_id";
                fields[73] = "refrigerant_id";
                fields[74] = "remarks_id";
                fields[75] = "stabilizer_id";
                fields[76] = "temp_display_id";
                fields[77] = "temp_range_id";
                fields[78] = "temperature_range_id";
                fields[79] = "time_control_id";
                fields[80] = "volts_id";
                fields[81] = "volume_id";
                fields[82] = "wdh_inchs_id";
                fields[83] = "wdh_mm_id";
                fields[84] = "wheels_castors_id";
                fields[85] = "output_id";
                fields[86] = "quantity_id";
                fields[87] = "ventilations_id";
                fields[88] = "temp_con_panel_id";
                fields[89] = "ref_cap_id";
                fields[90] = "defrosting_id";
                fields[91] = "front_glass_id";
                fields[92] = "doors_id";
                fields[93] = "compressor_id";
                fields[94] = "ref_freezer_id";
                fields[95] = "certificate_id";
                fields[96] = "temp_control_id";

                fields[97] = "temp_con_pos_num_id";
                fields[98] = "ice_maker_cham_id";
                fields[99] = "adjustable_leg_id";
                fields[100] = "handle_id";
                fields[101] = "reversible_door_id";
                fields[102] = "colour_id";

                OdooFields odooFields = new OdooFields();
                odooFields.addAll(fields);

                int offset = 0;
                int limit = 80;

                String sorting = "";
                client.searchRead("res.company", domain, odooFields, offset, limit, sorting, new IOdooResponse() {
                    @Override
                    public void onResult(OdooResult result) {
                        OdooRecord[] records = result.getRecords();

                        try {
                            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());

                            alIDFetched = new ArrayList<>();

                            for (OdooRecord record : records) {
                                int id = Integer.valueOf(record.get("id").toString());
                                alIDFetched.add(id);
                                record.remove("id");
                                ArrayList<Object> alTechSpecsValue = new ArrayList<>(record.values());
                                ArrayList<String> alTechSpecsKey = new ArrayList<>(record.keySet());
                                ArrayList<String> alKeys = new ArrayList<>();
                                ArrayList<String> alValues = new ArrayList<>();

                                for (int j = 0; j < alTechSpecsValue.size(); j++) {

                                    if (!alTechSpecsValue.get(j).toString().equals("false")) {
                                        String sTechSpecValue = String.valueOf(alTechSpecsValue.get(j));
                                        String sTechSpecKeyID = alTechSpecsKey.get(j);

                                        String sTechSpecKey = jsonObject.getString(sTechSpecKeyID);

                                        alKeys.add(sTechSpecKey);
                                        alValues.add(sTechSpecValue);
                                    }
                                }
                                if (alKeys.size() >= 1 && alValues.size() >= 1) {
                                    String sKey = TextUtils.join(",", alKeys);
                                    String sValue = TextUtils.join(",", alValues);
                                    dbh.update(new DataBaseHelper(sKey, sValue), id);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                TechnicalSpecService.onServiceInterfaceListener.onServiceMessage("TASK_COMPLETE", alIDFetched);
            }

            @Override
            public void onLoginFail(AuthError error) {
                String failed = error.toString();
            }
        };
    }

    private void validateLogin(Integer[] nOdooIDArray) {
        //240
        try {
            OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);

            Integer[] ids = new Integer[3];
            ids[0] = 371;
            ids[1] = 304;
            ids[2] = 303;

            //ids
            Object[] conditions1 = new Object[1];
            //conditions[0] = new Object[]{"id", "!=", "0099009"};
            conditions1[0] = new Object[]{"id", "=", nOdooIDArray};
            //conditions1[1] = new Object[]{"id", "=", 304};

            String[] fields = new String[103];
            fields[0] = "blood_bags_id";
            fields[1] = "ambient_id";
            fields[2] = "baking_plate_revolve_id";
            fields[3] = "boiler_capacity_id";
            fields[4] = "bottle_storage_id";
            fields[5] = "bowl_capacity_id";
            fields[6] = "bowl_speed_id";
            fields[7] = "cabinet_case_id";
            fields[8] = "capacity_in_cft_id";
            fields[9] = "capacity_in_gallons_id";
            fields[10] = "capacity_in_litre_id";
            fields[11] = "cavity_dimension_id";
            fields[12] = "cavity_voulme_id";
            fields[13] = "compatible_storage_bin_id";
            fields[14] = "consecutive_dispensing_id";
            fields[15] = "cooking_surface_id";
            fields[16] = "cooking_time_id";
            fields[17] = "cooling_system_id";
            fields[18] = "cube_shape_id";
            fields[19] = "current_id";
            fields[20] = "defrost_id";
            fields[21] = "digital_temp_indicator_id";
            fields[22] = "dimensions_id";
            fields[23] = "door_handle_id";
            fields[24] = "drive_motor_id";
            fields[25] = "electricals_id";
            fields[26] = "external_dimension_id";
            fields[27] = "fans_id";
            fields[28] = "first_dispensing_id";
            fields[29] = "flavours_id";
            fields[30] = "freezing_cylinder_id";
            fields[31] = "freezing_cylinder_capacity_id";
            fields[32] = "fry_basket_id";
            fields[33] = "frypot_oil_id";
            fields[34] = "gas_input_id";
            fields[35] = "glass_door_id";
            fields[36] = "gn_en_id";
            fields[37] = "griddle_dimension_id";
            fields[38] = "grinding_burrs_id";
            fields[39] = "gross_volumels_id";

            fields[40] = "heat_load_id";
            fields[41] = "hopper_capacity_id";
            fields[42] = "ice_storage_id";
            fields[43] = "input_power_id";
            fields[44] = "interior_dimensions_id";
            fields[45] = "interior_light_id";
            fields[46] = "jar_capacity_id";
            fields[47] = "lighting_under_id";
            fields[48] = "loading_temp_id";
            fields[49] = "lock_id";
            fields[50] = "max_kneading_id";
            fields[51] = "max_loading_id";
            fields[52] = "max_production_id";
            fields[53] = "max_room_area_id";
            fields[54] = "max_room_volume_id";
            fields[55] = "mix_hopper_id";
            fields[56] = "mixing_speed_id";
            fields[57] = "motor_power_id";
            fields[58] = "net_weight_id";
            fields[59] = "no_of_baskets_id";
            fields[60] = "no_of_beech_id";
            fields[61] = "no_of_compressor_id";
            fields[62] = "no_of_doors_id";
            fields[63] = "no_of_layers_id";
            fields[64] = "no_of_lids_id";
            fields[65] = "no_of_shelves_id";
            fields[66] = "power_id";
            fields[67] = "power_supply_id";
            fields[68] = "power_consumption_id";
            fields[69] = "product_dimension_id";
            fields[70] = "product_weight_id";
            fields[71] = "rated_capacity_id";
            fields[72] = "rated_input_ower_id";
            fields[73] = "refrigerant_id";
            fields[74] = "remarks_id";
            fields[75] = "stabilizer_id";
            fields[76] = "temp_display_id";
            fields[77] = "temp_range_id";
            fields[78] = "temperature_range_id";
            fields[79] = "time_control_id";
            fields[80] = "volts_id";
            fields[81] = "volume_id";
            fields[82] = "wdh_inchs_id";
            fields[83] = "wdh_mm_id";
            fields[84] = "wheels_castors_id";
            fields[85] = "output_id";
            fields[86] = "quantity_id";
            fields[87] = "ventilations_id";
            fields[88] = "temp_con_panel_id";
            fields[89] = "ref_cap_id";
            fields[90] = "defrosting_id";
            fields[91] = "front_glass_id";
            fields[92] = "doors_id";
            fields[93] = "compressor_id";
            fields[94] = "ref_freezer_id";
            fields[95] = "certificate_id";
            fields[96] = "temp_control_id";

            fields[97] = "temp_con_pos_num_id";
            fields[98] = "ice_maker_cham_id";
            fields[99] = "adjustable_leg_id";
            fields[100] = "handle_id";
            fields[101] = "reversible_door_id";
            fields[102] = "colour_id";


            //conditions1[1] = new Object[]{"bowl_capacity_id", "!=", 0};

            //"technical.specification"

            //List<HashMap<String, Object>> stallData = oc.search_read("product.template", new Object[]{conditions1}, "name", "tech_spec_name");
            //List<HashMap<String, Object>> stallData = oc.search_read("product.template", new Object[]{conditions1}, "id", "bowl_capacity_id", "ambient_id");
            List<HashMap<String, Object>> techSpecsData = oc.search_read("product.template", new Object[]{conditions1}, fields);
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());

            alIDFetched = new ArrayList<>();

            for (int i = 0; i < techSpecsData.size(); i++) {
                int id = Integer.valueOf(techSpecsData.get(i).get("id").toString());
                alIDFetched.add(id);
                techSpecsData.get(i).remove("id");
                ArrayList<Object> alTechSpecsValue = new ArrayList<>(techSpecsData.get(i).values());
                ArrayList<String> alTechSpecsKey = new ArrayList<>(techSpecsData.get(i).keySet());
                ArrayList<String> alKeys = new ArrayList<>();
                ArrayList<String> alValues = new ArrayList<>();

                for (int j = 0; j < alTechSpecsValue.size(); j++) {

                    if (!alTechSpecsValue.get(j).toString().equals("false")) {
                        String sTechSpecValue = String.valueOf(alTechSpecsValue.get(j));
                        String sTechSpecKeyID = alTechSpecsKey.get(j);

                        String sTechSpecKey = jsonObject.getString(sTechSpecKeyID);

                        alKeys.add(sTechSpecKey);
                        alValues.add(sTechSpecValue);


                    }
                }
                if (alKeys.size() >= 1 && alValues.size() >= 1) {
                    String sKey = android.text.TextUtils.join(",", alKeys);
                    String sValue = android.text.TextUtils.join(",", alValues);
                    dbh.update(new DataBaseHelper(sKey, sValue), id);
                }
            }
            /*try {
                JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
                //for(int i=0; i<jsonObject.length();i++){
                String json = jsonObject.getString("bowl_speed_id");
                String ss = json + 123;
                //}
            }catch (Exception e){

            }*/
            //getData();
            /*if (stallData.size() == 1) {
                isPresent = true;
                odooID = Integer.valueOf(stallData.get(0).get("id").toString());
            }*/
            /*for (int i = 0; i < stallData.size(); ++i) {
                //int id = Integer.valueOf(data.get(i).get("id").toString());
                String sName = String.valueOf(stallData.get(i).get("name").toString());

                *//*if(sStallName.equals(sName)){
                    isPresent = true;

                    return;
                }*//*
                if (sStallName.equals(sName)) {
                    isPresent = true;
                    odooID = Integer.valueOf(stallData.get(i).get("id").toString());
                    return;
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        //TechnicalSpecService.onServiceInterfaceListener.onServiceMessage("TASK_OVER", null);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("specs.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void getData() {
        //240
        try {
            OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            Object[] conditions1 = new Object[1];
            //conditions[0] = new Object[]{"id", "!=", "0099009"};
            conditions1[0] = new Object[]{"id", "=", 371};
            List<HashMap<String, Object>> stallData = oc.search_read("technical.specification", new Object[]{conditions1}, "id", "name", "tech_spec_name");
            //List<HashMap<String, Object>> data = oc.search_read("technical.specification", new Object[]{conditions1}, "name");
            if (stallData.size() == 1) {
                isPresent = true;
                odooID = Integer.valueOf(stallData.get(0).get("id").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void createOrder() {
        //240
        try {
            OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            Integer createCustomer = oc.create("sale.order", new HashMap() {{
                put("partner_id", 562);
                //put("state", ORDER_STATE[0]);
            }});
            IDS[0] = createCustomer;
            //createOne2Many(createCustomer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    private void placeOrder() {
        //240
        try {
            OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            Boolean idC = oc.write("sale.order", new Object[]{nOrderID}, hmDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readImageTask() {
        OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
        Object[] conditions = new Object[2];
        conditions[0] = new Object[]{"res_model", "=", "product.template"};
        conditions[1] = new Object[]{"res_field", "=", "image_medium"};
        List<HashMap<String, Object>> data = oc.search_read("ir.attachment", new Object[]{conditions}, "id", "store_fname", "res_name");

        for (int i = 0; i < data.size(); ++i) {

        }
    }

    /*private void readProducts() {
        OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
        List<HashMap<String, Object>> data = oc.search_read("product.template", new Object[]{
                new Object[]{new Object[]{"type", "=", "product"}}}, "id", "name", "list_price");

        for (int i = 0; i < data.size(); ++i) {
            //int id = Integer.valueOf(data.get(i).get("id").toString());
            String sName = String.valueOf(data.get(i).get("name").toString());
            //String sUnitPrice = String.valueOf(data.get(i).get("list_price").toString());
            ArrayList<String> alData = new ArrayList<>();
            alData.add(data.get(i).get("id").toString());
            //alData.add(data.get(i).get("name").toString());
            alData.add(data.get(i).get("list_price").toString());
            lhmProductsWithID.put(sName, alData);
        }
    }*/
    private void readProductAndImageTask() {
        OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
        List<HashMap<String, Object>> productsData = oc.search_read("product.template", new Object[]{
                new Object[]{new Object[]{"type", "=", "product"}}}, "id", "name", "list_price");

        Object[] conditions = new Object[2];
        conditions[0] = new Object[]{"res_model", "=", "product.template"};
        conditions[1] = new Object[]{"res_field", "=", "image_medium"};
        List<HashMap<String, Object>> imageData = oc.search_read("ir.attachment", new Object[]{conditions},
                "id", "store_fname", "res_name");

        for (int i = 0; i < productsData.size(); ++i) {
            //int id = Integer.valueOf(data.get(i).get("id").toString());
            String sName = String.valueOf(productsData.get(i).get("name").toString());
            //String sUnitPrice = String.valueOf(data.get(i).get("list_price").toString());
            ArrayList<String> alData = new ArrayList<>();
            alData.add(productsData.get(i).get("id").toString());
            //alData.add(data.get(i).get("name").toString());
            alData.add(productsData.get(i).get("list_price").toString());
            for (int j = 0; j < imageData.size(); j++) {
                String base64 = imageData.get(j).get("store_fname").toString();
                if (imageData.get(j).get("res_name").toString().equals(sName)) {
                    alData.add(base64);
                    alPosition.add(i);
                    break;
                }
            }
            lhmProductsWithID.put(sName, alData);
        }
    }

    private void unableToConnectServer(int errorCode) {
        //OfflineTransferService.onAsyncInterfaceListener.onAsyncComplete("SERVER_ERROR", errorCode, "");
    }

    /*private void setProgressBar() {
        circularProgressBar = new CircularProgressBar(context);
        circularProgressBar.setCanceledOnTouchOutside(false);
        circularProgressBar.setCancelable(false);
        circularProgressBar.show();
    }*/

}
