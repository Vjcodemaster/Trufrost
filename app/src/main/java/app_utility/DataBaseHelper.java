package app_utility;


public class DataBaseHelper {

    //private variables
    private int _id;
    //private String _phone_number;
    private String _main_product_names;
    private String _main_product_images_path;
    private String _main_product_description;
    private String _product_category_names;
    private String _product_category_description;
    private String _sub_category_images_link;
    private String _sub_category_images_path;

    private String _first_sub_category_names;
    private String _first_sub_category_images_path;
    private String _first_sub_category_images_link;

    private int _main_product_id;
    private int _odoo_product_id;
    private int _first_category_id;
    private String _sub_category_ids;
    private String _individual_product_names;
    private String _individual_product_description;
    private String _individual_product_images_path;
    private String _individual_product_variant_names;
    private String _individual_product_variant_images_path;
    private String _individual_product_address;
    private String _individual_product_tech_specs_header;
    private String _individual_product_tech_specs_value;
    private String _individual_product_tags;

    // Empty constructor
    public DataBaseHelper(int _main_product_id, String _main_product_names, String _first_sub_category_names,
                          String _first_sub_category_images_link,String _product_category_names,
                          String _sub_category_images_link) {
        this._main_product_id = _main_product_id;
        this._main_product_names = _main_product_names;
        this._first_sub_category_names = _first_sub_category_names;
        this._first_sub_category_images_link = _first_sub_category_images_link;
        this._product_category_names = _product_category_names;
        this._sub_category_images_link = _sub_category_images_link;
    }

    public DataBaseHelper(String _product_category_names, int nCase) {
        this._product_category_names = _product_category_names;
    }

    public DataBaseHelper(String _individual_product_images_path) {
        this._individual_product_images_path = _individual_product_images_path;
    }

    public DataBaseHelper(String _individual_product_tech_specs_header, String _individual_product_tech_specs_value) {
        this._individual_product_tech_specs_header = _individual_product_tech_specs_header;
        this._individual_product_tech_specs_value = _individual_product_tech_specs_value;
    }


    public DataBaseHelper(String _main_product_names, String _main_product_description, String _product_category_names) {
        this._main_product_names = _main_product_names;
        this._main_product_description = _main_product_description;
        this._product_category_names = _product_category_names;
        //this._product_category_description = _product_category_description;
        //this._product_category_images_path = _product_category_images_path;
    }

    public DataBaseHelper(String _main_product_names, String _main_product_description, String _first_sub_category_names,
                          String _first_sub_category_images_link) {
        this._main_product_names = _main_product_names;
        this._main_product_description = _main_product_description;
        this._first_sub_category_names = _first_sub_category_names;
        this._first_sub_category_images_link = _first_sub_category_images_link;
    }

    public DataBaseHelper(){

    }

    public DataBaseHelper(String _general, int nCase, boolean b) {
        switch (nCase){
            case 1:
                this._first_sub_category_images_path = _general;
                break;
            case 2:
                this._sub_category_images_path = _general;
                break;
        }
    }

    /*public DataBaseHelper(String _main_product_names, String _main_product_images_path, String _product_category_names,
                          String _product_category_description, String _product_category_images_path){
        this._main_product_names = _main_product_names;
        this._main_product_images_path = _main_product_images_path;
        this._product_category_names = _product_category_names;
        this._product_category_description = _product_category_description;
        this._product_category_images_path = _product_category_images_path;
    }*/

    public DataBaseHelper(int _main_product_id, int _odoo_product_id, int _first_category_id, String _main_product_names, String _first_sub_category_names, String _product_category_names,
                          String _individual_product_names, String _individual_product_description, String _individual_product_tech_specs_header,
                          String _individual_product_tech_specs_value, String _individual_product_address, String _individual_product_images_path, String _individual_product_tags) {
        this._main_product_id = _main_product_id;
        this._odoo_product_id = _odoo_product_id;
        this._first_category_id = _first_category_id;
        this._main_product_names = _main_product_names;
        this._first_sub_category_names = _first_sub_category_names;
        this._product_category_names = _product_category_names;

        //this._sub_category_ids = _sub_category_ids;
        this._individual_product_names = _individual_product_names;
        this._individual_product_description = _individual_product_description;
        this._individual_product_tech_specs_header = _individual_product_tech_specs_header;
        this._individual_product_tech_specs_value = _individual_product_tech_specs_value;
        this._individual_product_address = _individual_product_address;
        this._individual_product_images_path = _individual_product_images_path;
        this._individual_product_tags = _individual_product_tags;
        //this._individual_product_variant_names = _individual_product_variant_names;
        //this._individual_product_variant_images_path = _individual_product_variant_images_path;
    }

    /*public DataBaseHelper(int _main_product_id, int _odoo_product_id, String , String _product_category_names, String _individual_product_names, String _individual_product_description,
                          String _individual_product_address, String _individual_product_images_path, String _individual_product_tech_specs_header,
                          String _individual_product_tech_specs_value, String _individual_product_tags) {
        this._main_product_id = _main_product_id;
        this._odoo_product_id = _odoo_product_id;
        this._main_product_names = _main_product_names;
        this._product_category_names = _product_category_names;
        //this._sub_category_ids = _sub_category_ids;
        this._individual_product_names = _individual_product_names;
        this._individual_product_description = _individual_product_description;
        this._individual_product_address = _individual_product_address;
        this._individual_product_images_path = _individual_product_images_path;
        this._individual_product_tech_specs_header = _individual_product_tech_specs_header;
        this._individual_product_tech_specs_value = _individual_product_tech_specs_value;
        this._individual_product_tags = _individual_product_tags;
        //this._individual_product_variant_names = _individual_product_variant_names;
        //this._individual_product_variant_images_path = _individual_product_variant_images_path;
    }*/

    // constructor
    /*public DataBaseHelper(String name, String _phone_number, String _email_id, String _designation){
        this._name = name;
        this._phone_number = _phone_number;
        this._email_id = _email_id;
        this._designation = _designation;
    }*/

    // constructor
    /*public DataBaseHelper(String _tag_id, String _major, String _minor, String _uuid, String _rssi, String _time,
                          String _date){
        this._tag_id = _tag_id;
        this._major = _major;
        this._minor = _minor;
        this._uuid = _uuid;
        this._rssi = _rssi;
        this._time = _time;
        this._date = _date;
    }

    // constructor
    public DataBaseHelper(String _last_seen_time, int _id){
        this._last_seen_time = _last_seen_time;
        this._id = _id;
    }*/

    // getting ID
    public int get_id() {
        return this._id;
    }

    // setting id
    public void set_id(int id) {
        this._id = id;
    }

    // getting tagID
    public String get_main_product_names() {
        return this._main_product_names;
    }

    // setting tagID
    public void set_main_product_names(String main_product_names) {
        this._main_product_names = main_product_names;
    }

    // getting name
    public String get_main_product_images_path() {
        return this._main_product_images_path;
    }

    // setting name
    public void set_main_product_description(String main_product_description) {
        this._main_product_description = main_product_description;
    }

    public String get_main_product_description() {
        return this._main_product_description;
    }

    // setting name
    public void set_main_product_images_path(String main_product_images_path) {
        this._main_product_images_path = main_product_images_path;
    }

    // getting phone number
    public String get_product_category_names() {
        return this._product_category_names;
    }

    // setting phone number
    public void set_product_category_names(String product_category_names) {
        this._product_category_names = product_category_names;
    }

    // getting emailID
    public String get_product_category_description() {
        return this._product_category_description;
    }

    // setting emailID
    public void set_product_category_description(String product_category_description) {
        this._product_category_description = product_category_description;
    }

    public String get_sub_category_images_link() {
        return this._sub_category_images_link;
    }

    public void set_sub_category_images_link(String _sub_category_images_link) {
        this._sub_category_images_link = _sub_category_images_link;
    }

    public String get_sub_category_images_path() {
        return this._sub_category_images_path;
    }

    public void set_sub_category_images_path(String sub_category_images_path) {
        this._sub_category_images_path = sub_category_images_path;
    }

    public int get_main_product_id() {
        return this._main_product_id;
    }

    // setting emailID
    public void set_main_product_id(int main_product_id) {
        this._main_product_id = main_product_id;
    }

    public String get_sub_category_ids() {
        return this._sub_category_ids;
    }

    // setting emailID
    public void set_sub_category_ids(String sub_category_ids) {
        this._sub_category_ids = sub_category_ids;
    }

    public String get_individual_product_names() {
        return this._individual_product_names;
    }

    // setting emailID
    public void set_individual_product_names(String individual_product_names) {
        this._individual_product_names = individual_product_names;
    }

    public String get_individual_product_description() {
        return this._individual_product_description;
    }

    // setting emailID
    public void set_individual_product_description(String individual_product_description) {
        this._individual_product_description = individual_product_description;
    }

    public String get_individual_product_images_path() {
        return this._individual_product_images_path;
    }

    // setting emailID
    public void set_individual_product_images_path(String individual_product_images_path) {
        this._individual_product_images_path = individual_product_images_path;
    }

    public String get_individual_product_variant_names() {
        return this._individual_product_variant_names;
    }

    // setting emailID
    public void set_individual_product_variant_names(String individual_product_variant_names) {
        this._individual_product_variant_names = individual_product_variant_names;
    }

    public String get_individual_product_variant_images_path() {
        return this._individual_product_variant_images_path;
    }

    // setting emailID
    public void set_individual_product_variant_images_path(String individual_product_variant_images_path) {
        this._individual_product_variant_images_path = individual_product_variant_images_path;
    }


    public String get_individual_product_address() {
        return this._individual_product_address;
    }

    // setting emailID
    public void set_individual_product_address(String individual_product_address) {
        this._individual_product_address = individual_product_address;
    }

    public String get_individual_product_tech_specs_header() {
        return this._individual_product_tech_specs_header;
    }

    // setting emailID
    public void set_individual_product_tech_specs_header(String individual_product_tech_specs_header) {
        this._individual_product_tech_specs_header = individual_product_tech_specs_header;
    }

    public String get_individual_product_tech_specs_value() {
        return this._individual_product_tech_specs_value;
    }

    // setting emailID
    public void set_individual_product_tech_specs_value(String individual_product_tech_specs_value) {
        this._individual_product_tech_specs_value = individual_product_tech_specs_value;
    }

    public int get_odoo_product_id() {
        return this._odoo_product_id;
    }

    // setting emailID
    public void set_odoo_product_id(int odoo_product_id) {
        this._odoo_product_id = odoo_product_id;
    }

    public String get_individual_product_tags() {
        return this._individual_product_tags;
    }

    // setting emailID
    public void set_individual_product_tags(String individual_product_tags) {
        this._individual_product_tags = individual_product_tags;
    }

    public int get_first_category_id(){
        return  this._first_category_id;
    }

    public void set_first_category_id(int first_category_id) {
        this._first_category_id = first_category_id;

    }

    public String get_first_sub_category_names() {
        return this._first_sub_category_names;
    }

    // setting emailID
    public void set_first_sub_category_names(String first_sub_category_names) {
        this._first_sub_category_names = first_sub_category_names;
    }

    public String get_first_sub_category_images_path() {
        return this._first_sub_category_images_path;
    }

    // setting emailID
    public void set_first_sub_category_images_path(String first_sub_category_images_path) {
        this._first_sub_category_images_path = first_sub_category_images_path;
    }

    public String get_first_sub_category_images_link() {
        return this._first_sub_category_images_link;
    }

    // setting emailID
    public void set_first_sub_category_images_link(String first_sub_category_images_link) {
        this._first_sub_category_images_link = first_sub_category_images_link;
    }
}
