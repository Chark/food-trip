package io.chark.food.domain.restaurant;

import io.chark.food.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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

    @Size(max = DEFAULT_LENGTH)
    private String phoneNumber;
    @Size(max = DEFAULT_LENGTH)
    private String fax;
    @Size(max = DEFAULT_LENGTH)
    private String webSite;
    @Size(max = DEFAULT_LENGTH)
    private int employees;
    @Size(max = DEFAULT_LENGTH)
    private String mobileNumber;
    @Size(max = DEFAULT_LENGTH)
    @Column(unique = true, nullable = false, length = DEFAULT_LENGTH)
    private String bankAccountNumber;


    @ManyToOne
    private Bank bank;


    public RestaurantDetails(String mobileNumber, String bankAccountNumber, String vat, String registrationCode, String manager) {
        this.mobileNumber = mobileNumber;
        this.bankAccountNumber = bankAccountNumber;
        this.vat = vat;
        this.registrationCode = registrationCode;
        this.manager = manager;
    }





}