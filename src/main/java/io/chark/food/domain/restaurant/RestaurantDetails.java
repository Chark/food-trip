package io.chark.food.domain.restaurant;

import io.chark.food.domain.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
public class RestaurantDetails extends BaseEntity {

    // Imones kodas
    @Size(max = DEFAULT_LENGTH)
    @Column(unique = true, nullable = false, length = DEFAULT_LENGTH)
    private String registrationCode;


    //moketojo kodas
    @Size(max = DEFAULT_LENGTH)
    @Column(unique = true, nullable = false, length = DEFAULT_LENGTH)
    private String vat;


    @Size(max = DEFAULT_LENGTH)
    @Column(nullable = false, length = DEFAULT_LENGTH)
    private String manager;


    private String phoneNumber;

    private String fax;

    private String website;

    private int employees;

    private String mobileNumber;

    @Size(max = DEFAULT_LENGTH)
    @Column(unique = true, nullable = false, length = DEFAULT_LENGTH)
    private String bankAccountNumber;


    @ManyToOne
    private Bank bank;

    @OneToOne()
    private Restaurant restauant;


    public RestaurantDetails() {
    }

    public RestaurantDetails(String mobileNumber, String bankAccountNumber, String vat, String registrationCode, String manager) {
        this.mobileNumber = mobileNumber;
        this.bankAccountNumber = bankAccountNumber;
        this.vat = vat;
        this.registrationCode = registrationCode;
        this.manager = manager;
    }

    public void setRestauant(Restaurant restauant) {
        this.restauant = restauant;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getEmployees() {
        return employees;
    }

    public void setEmployees(int employees) {
        this.employees = employees;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Bank getBank() {
        return bank;
    }

    public Restaurant getRestauant() {
        return restauant;
    }
}