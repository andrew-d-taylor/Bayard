package edu.usm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "donor_info")
public class DonorInfo extends BasicEntity implements Serializable {

    @Column
    private boolean sustainer;

    @Column
    private LocalDate date;

    @Column
    private boolean irsLetterSent;

    @Column
    private boolean thankYouLetterSent;

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name="donor_info_id")
    private Set<Donation> donations;

    public DonorInfo (String id) {
        setId(id);
    }

    public DonorInfo() {
        super();
    }

    public boolean isSustainer() {
        return sustainer;
    }

    public void setSustainer(boolean isSustainer) {
        this.sustainer = isSustainer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<Donation> getDonations() {
        return donations;
    }

    public void addDonation(Donation donation) {
        if (null == this.donations) {
            this.donations = new HashSet<>();
        }
        this.donations.add(donation);
    }

    public boolean isIrsLetterSent() {
        return irsLetterSent;
    }

    public void setIrsLetterSent(boolean irsLetterSent) {
        this.irsLetterSent = irsLetterSent;
    }

    public boolean isThankYouLetterSent() {
        return thankYouLetterSent;
    }

    public void setThankYouLetterSent(boolean thankYouLetterSent) {
        this.thankYouLetterSent = thankYouLetterSent;
    }
}
