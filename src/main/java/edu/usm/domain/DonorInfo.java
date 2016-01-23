package edu.usm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

@Entity(name = "donor_info")
public class DonorInfo extends BasicEntity implements Serializable {

    @Column
    private boolean currentSustainer;

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name="donor_info_id")
    private Set<Donation> donations;

    @OneToMany(mappedBy = "donorInfo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private SortedSet<SustainerPeriod> sustainerPeriods;

    public DonorInfo (String id) {
        setId(id);
    }

    public DonorInfo() {
        super();
    }

    public boolean isCurrentSustainer() {
        return currentSustainer;
    }

    public void setCurrentSustainer(boolean isSustainer) {
        this.currentSustainer = isSustainer;
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

    public void setDonations(Set<Donation> donations) {
        this.donations = donations;
    }

    public Set<SustainerPeriod> getSustainerPeriods() {
        return sustainerPeriods;
    }

    public void setSustainerPeriods(SortedSet<SustainerPeriod> sustainerPeriods) {
        this.sustainerPeriods = sustainerPeriods;
    }
}
