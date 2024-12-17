package com.example.taxstatement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TaxStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String taxId;
    private double employmentIncome;
    private double taxPaid;
    private double taxRefundDue;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public double getEmploymentIncome() {
        return employmentIncome;
    }

    public void setEmploymentIncome(double employmentIncome) {
        this.employmentIncome = employmentIncome;
    }

    public double getTaxPaid() {
        return taxPaid;
    }

    public void setTaxPaid(double taxPaid) {
        this.taxPaid = taxPaid;
    }

    public double getTaxRefundDue() {
        return taxRefundDue;
    }

    public void setTaxRefundDue(double taxRefundDue) {
        this.taxRefundDue = taxRefundDue;
    }

    @Override
    public String toString() {
        return "TaxStatement{" +
                "name='" + name + '\'' +
                ", taxId='" + taxId + '\'' +
                ", employmentIncome=" + employmentIncome +
                ", taxPaid=" + taxPaid +
                ", taxRefundDue=" + taxRefundDue +
                '}';
    }
}
