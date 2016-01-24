package edu.usm.domain;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * Created by andrew on 1/23/16.
 */
@Entity(name = "sustainer_period")
public class SustainerPeriod extends BasicEntity implements Comparable<SustainerPeriod>, Serializable {

    @Transient
    private Map<Integer, Boolean> activeMonths = new HashMap<>();

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


    public int getTotalYearToDate() {
        int total = 0;
        Set<Integer> months = this.activeMonths.keySet();
        for (Integer month: months) {
            if (this.activeMonths.get(month)) {
                total += this.monthlyAmount;
            }
        }
        return total;
    }


    public void setMonth(Month month, boolean value) {
        switch (month.getValue()) {
            case 1:
                this.setJanuary(value);
                break;
            case 2:
                this.setFebruary(value);
                break;
            case 3:
                this.setMarch(value);
                break;
            case 4:
                this.setApril(value);
                break;
            case 5:
                this.setMay(value);
                break;
            case 6:
                this.setJune(value);
                break;
            case 7:
                this.setJuly(value);
                break;
            case 8:
                this.setAugust(value);
                break;
            case 9:
                this.setSeptember(value);
                break;
            case 10:
                this.setOctober(value);
                break;
            case 11:
                this.setNovember(value);
                break;
            case 12:
                this.setDecember(value);
                break;
            default:
                break;
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
        this.activeMonths.put(1, january);
    }

    public boolean isFebruary() {
        return february;
    }

    public void setFebruary(boolean february) {
        this.february = february;
        this.activeMonths.put(2, february);
    }

    public boolean isMarch() {
        return march;
    }

    public void setMarch(boolean march) {
        this.march = march;
        this.activeMonths.put(3, march);
    }

    public boolean isApril() {
        return april;
    }

    public void setApril(boolean april) {
        this.april = april;
        this.activeMonths.put(4, april);
    }

    public boolean isMay() {
        return may;
    }

    public void setMay(boolean may) {
        this.may = may;
        this.activeMonths.put(5, may);
    }

    public boolean isJune() {
        return june;
    }

    public void setJune(boolean june) {
        this.june = june;
        this.activeMonths.put(6, june);
    }

    public boolean isJuly() {
        return july;
    }

    public void setJuly(boolean july) {
        this.july = july;
        this.activeMonths.put(7, july);
    }

    public boolean isAugust() {
        return august;
    }

    public void setAugust(boolean august) {
        this.august = august;
        this.activeMonths.put(8, august);
    }

    public boolean isSeptember() {
        return september;
    }

    public void setSeptember(boolean september) {
        this.september = september;
        this.activeMonths.put(9, september);
    }

    public boolean isOctober() {
        return october;
    }

    public void setOctober(boolean october) {
        this.october = october;
        this.activeMonths.put(10, october);
    }

    public boolean isNovember() {
        return november;
    }

    public void setNovember(boolean november) {
        this.november = november;
        this.activeMonths.put(11, november);
    }

    public boolean isDecember() {
        return december;
    }

    public void setDecember(boolean december) {
        this.december = december;
        this.activeMonths.put(12, december);
    }

}
