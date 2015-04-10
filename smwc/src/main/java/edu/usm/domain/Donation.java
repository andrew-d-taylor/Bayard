package edu.usm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class Donation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private int amount;
    @Column
    private String type;
    @Column
    private LocalDate date;
    @Column
    private String comment;


    @ManyToOne(fetch = FetchType.LAZY)
    private DonorInfo donor;

    public DonorInfo getDonorInfo() {
        return donor;
    }

    public void setDonorInfo(DonorInfo donorInfo) {
        this.donor = donorInfo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }




}