package com.company;

/**
 * Created by zhou on 17-4-20.
 */
public class LocationInfo {
    public int countryId;
   // private int adminId;
   public int subLocalityId;
   public int adminId;
//    private String country;
    public String admin;
    public String subLocality;
    public String locality;
    public String country;
    public int localityId;
    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getSubLocalityId() {
        return subLocalityId;
    }

    public void setSubLocalityId(int subLocalityId) {
        this.subLocalityId = subLocalityId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getSubLocality() {
        return subLocality;
    }

    public void setSubLocality(String subLocality) {
        this.subLocality = subLocality;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
