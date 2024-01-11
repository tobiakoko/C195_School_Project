package model;

import java.time.LocalDateTime;

public class Country {
    private int countryId;
    private String country;
    private int monthCount;
    private String month;


    public Country(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    public Country(String month, int monthCount){
        this.month = month;
        this.monthCount = monthCount;
    }
    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(int monthCount) {
        this.monthCount = monthCount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }



    @Override
    public String toString() {
        return ("Country{" +
                "countryId=" + countryId +
                ", country='" + country + '\'' +
                '}');
    }
}
