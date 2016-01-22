package edu.usm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="donation_id")
    private List<Donation> donations;

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

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
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
