package com.example.congresssearch;

import java.io.Serializable;
/**
 * Created by ZHEJIAO on 11/27/16.
 */

public class BillsData implements Serializable {
    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getShort_title() {
        return short_title;
    }

    public void setShort_title(String short_title) {
        this.short_title = short_title;
    }

    public String getLong_title() {
        return long_title;
    }

    public void setLong_title(String long_title) {
        this.long_title = long_title;
    }

    public String getIntroduced_on() {
        return introduced_on;
    }

    public void setIntroduced_on(String introduced_on) {
        this.introduced_on = introduced_on;
    }

    public String getBill_type() {
        return bill_type;
    }

    public void setBill_type(String bill_type) {
        this.bill_type = bill_type;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCongress_url() {
        return congress_url;
    }

    public void setCongress_url(String congress_url) {
        this.congress_url = congress_url;
    }

    public String getVersion_status() {
        return version_status;
    }

    public void setVersion_status(String version_status) {
        this.version_status = version_status;
    }

    public String getBill_url() {
        return bill_url;
    }

    public void setBill_url(String bill_url) {
        this.bill_url = bill_url;
    }

    private String bill_id;
    private String short_title;
    private String long_title;
    private String introduced_on;
    private String bill_type;
    private String sponsor;
    private String chamber;
    private String status;
    private String congress_url;
    private String version_status;
    private String bill_url;

}
