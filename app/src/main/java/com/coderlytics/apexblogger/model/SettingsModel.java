package com.coderlytics.apexblogger.model;

public class SettingsModel {


    public String version_code;
    public String version_name;
    public String maintenance_message;
    public String update_msg;
    public String first_time_start_app_message;
    public String term_and_condition_url;

    public String getAbout_us_url() {
        return about_us_url;
    }

    public void setAbout_us_url(String about_us_url) {
        this.about_us_url = about_us_url;
    }

    public String about_us_url;
    public String privacy_policy_url;
    public boolean maintance_mode;



    public boolean first_time_enabled;

    public boolean isFirst_time_enabled() {
        return first_time_enabled;
    }

    public void setFirst_time_enabled(boolean first_time_enabled) {
        this.first_time_enabled = first_time_enabled;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getMaintenance_message() {
        return maintenance_message;
    }

    public void setMaintenance_message(String maintenance_message) {
        this.maintenance_message = maintenance_message;
    }

    public String getUpdate_msg() {
        return update_msg;
    }

    public void setUpdate_msg(String update_msg) {
        this.update_msg = update_msg;
    }

    public String getFirst_time_start_app_message() {
        return first_time_start_app_message;
    }

    public void setFirst_time_start_app_message(String first_time_start_app_message) {
        this.first_time_start_app_message = first_time_start_app_message;
    }

    public String getTerm_and_condition_url() {
        return term_and_condition_url;
    }

    public void setTerm_and_condition_url(String term_and_condition_url) {
        this.term_and_condition_url = term_and_condition_url;
    }

    public String getPrivacy_policy_url() {
        return privacy_policy_url;
    }

    public void setPrivacy_policy_url(String privacy_policy_url) {
        this.privacy_policy_url = privacy_policy_url;
    }

    public boolean isMaintance_mode() {
        return maintance_mode;
    }

    public void setMaintance_mode(boolean maintance_mode) {
        this.maintance_mode = maintance_mode;
    }
}
