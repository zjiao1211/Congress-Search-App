package com.example.congresssearch;

import java.io.Serializable;

/**
 * Created by ZHEJIAO on 11/25/16.
 */

public class LegislatorsData implements Serializable {

    public void setBioguide_id(String bioguide_id) {
        this.bioguide_id = bioguide_id;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStart_term(String start_term) {
        this.start_term = start_term;
    }

    public void setEnd_term(String end_term) {
        this.end_term = end_term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    public int getDistrict() {
        return district;
    }

    public String getTitle() {
        return title;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getEmail() {
        return email;
    }

    public String getChamber() {
        return chamber;
    }

    public String getPhone() {
        return phone;
    }

    public String getStart_term() {
        return start_term;
    }

    public String getEnd_term() {
        return end_term;
    }

    public String getTerm() {
        return term;
    }

    public String getOffice() {
        return office;
    }

    public String getFax() {
        return fax;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getWebsite() {
        return website;
    }

    public String getBioguide_id() {
        return bioguide_id;
    }
    public String getParty() {
        return party;
    }

    public String getState_name() {
        return state_name;
    }
    public String getLast_name() {
        return last_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String bioguide_id;
    private String party;
    private int district;
    private String title;
    private String last_name;
    private String first_name;
    private String email;
    private String chamber;
    private String phone;
    private String start_term;
    private String end_term;
    private String term;
    private String office;
    private String state_name;
    private String state;



    private String fax;
    private String birthday;
    private String facebook;
    private String twitter;
    private String website;

}
