// PackageDetailsDTO.java
package com.g4t2project.g4t2project.DTO;

public class PackageDetailsDTO {
    private String packageType;
    private String propertyType;
    private int price;
    private double hours;
    private int hourlyRate;
    private String propertyDetails;
    private int pax;
    private boolean manualBookingRequired;

    public PackageDetailsDTO(String packageType, String propertyType, int price, double hours, int hourlyRate, String propertyDetails, int pax, boolean manualBookingRequired) {
        this.packageType = packageType;
        this.propertyType = propertyType;
        this.price = price;
        this.hours = hours;
        this.hourlyRate = hourlyRate;
        this.propertyDetails = propertyDetails;
        this.pax = pax;
        this.manualBookingRequired = manualBookingRequired;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public int getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getPropertyDetails() {
        return propertyDetails;
    }

    public void setPropertyDetails(String propertyDetails) {
        this.propertyDetails = propertyDetails;
    }

    public int getPax() {
        return pax;
    }

    public void setPax(int pax) {
        this.pax = pax;
    }

    public boolean isManualBookingRequired() {
        return manualBookingRequired;
    }

    public void setManualBookingRequired(boolean manualBookingRequired) {
        this.manualBookingRequired = manualBookingRequired;
    }
    
}
