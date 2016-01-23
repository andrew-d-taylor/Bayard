package edu.usm.domain;
import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by andrew on 1/23/16.
 */
@Entity(name = "sustainer_period")
public class SustainerPeriod extends BasicEntity implements Comparable<SustainerPeriod> {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private DonorInfo donorInfo;

    @Column
    private int periodYear;

    @Column
    private int monthlyAmount;

    @Column
    private boolean sentIRSLetter;

    @Column
    private boolean active;

    @Column
    private boolean january;

    @Column
    private boolean february;
    @Column
    private boolean march;
    @Column
    private boolean april;
    @Column
    private boolean may;
    @Column
    private boolean june;
    @Column
    private boolean july;
    @Column
    private boolean august;
    @Column
    private boolean september;
    @Column
    private boolean october;
    @Column
    private boolean november;
    @Column
    private boolean december;

    @Override
    public int compareTo(SustainerPeriod o2) {
        if (this.periodYear == 0 && o2.getPeriodYear() == 0) {
            return 0;
        } else if (this.periodYear == 0) {
            return 1;
        } else if (o2.getPeriodYear() == 0) {
            return -1;
        } else {
            LocalDate thisDate = LocalDate.of(this.periodYear, 1, 1);
            LocalDate otherDate = LocalDate.of(o2.getPeriodYear(), 1, 1);

            return otherDate.compareTo(thisDate);
        }
    }

    public DonorInfo getDonorInfo() {
        return donorInfo;
    }

    public void setDonorInfo(DonorInfo donorInfo) {
        this.donorInfo = donorInfo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getPeriodYear() {
        return periodYear;
    }

    public void setPeriodYear(int periodYear) {
        this.periodYear = periodYear;
    }

    public int getMonthlyAmount() {
        return monthlyAmount;
    }

    public void setMonthlyAmount(int monthlyAmount) {
        this.monthlyAmount = monthlyAmount;
    }

    public boolean isSentIRSLetter() {
        return sentIRSLetter;
    }

    public void setSentIRSLetter(boolean sentIRSLetter) {
        this.sentIRSLetter = sentIRSLetter;
    }

    public boolean isJanuary() {
        return january;
    }

    public void setJanuary(boolean january) {
        this.january = january;
    }

    public boolean isFebruary() {
        return february;
    }

    public void setFebruary(boolean february) {
        this.february = february;
    }

    public boolean isMarch() {
        return march;
    }

    public void setMarch(boolean march) {
        this.march = march;
    }

    public boolean isApril() {
        return april;
    }

    public void setApril(boolean april) {
        this.april = april;
    }

    public boolean isMay() {
        return may;
    }

    public void setMay(boolean may) {
        this.may = may;
    }

    public boolean isJune() {
        return june;
    }

    public void setJune(boolean june) {
        this.june = june;
    }

    public boolean isJuly() {
        return july;
    }

    public void setJuly(boolean july) {
        this.july = july;
    }

    public boolean isAugust() {
        return august;
    }

    public void setAugust(boolean august) {
        this.august = august;
    }

    public boolean isSeptember() {
        return september;
    }

    public void setSeptember(boolean september) {
        this.september = september;
    }

    public boolean isOctober() {
        return october;
    }

    public void setOctober(boolean october) {
        this.october = october;
    }

    public boolean isNovember() {
        return november;
    }

    public void setNovember(boolean november) {
        this.november = november;
    }

    public boolean isDecember() {
        return december;
    }

    public void setDecember(boolean december) {
        this.december = december;
    }
}
