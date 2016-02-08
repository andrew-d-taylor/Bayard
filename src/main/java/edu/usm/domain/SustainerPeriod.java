package edu.usm.domain;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;

/**
 * Created by andrew on 1/23/16.
 */
@Entity(name = "sustainer_period")
public class SustainerPeriod extends BasicEntity implements MonetaryContribution, Comparable<SustainerPeriod>, Serializable {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @NotNull
    private DonorInfo donorInfo;

    @Column
    private int periodYear;

    @Column
    @NotNull
    private LocalDate periodStartDate;

    @Column
    private LocalDate cancelDate;

    @Column
    private int monthlyAmount;

    @Column
    private boolean sentIRSLetter;

    @Override
    public int compareTo(SustainerPeriod o) {
        if (o.getPeriodStartDate().isEqual(this.getPeriodStartDate())) {
            return 0;
        }
        return (this.getPeriodStartDate().isBefore(o.getPeriodStartDate())) ? 1 : -1;
    }

    public int getTotalYearToDate() {
        LocalDate endOfPeriod = this.cancelDate;
        if (endOfPeriod == null) {
            endOfPeriod = LocalDate.now();
        }
        Period periodBetween = Period.between(this.periodStartDate, endOfPeriod);
        int total = 0;
        for (int i = 0; i < periodBetween.getMonths(); i++) {
            total += this.monthlyAmount;
        }
        return total;
    }

    @Override
    public int getAmount() {
        return getTotalYearToDate();
    }

    @Override
    public LocalDate getDateOfReceipt() {
        return this.getPeriodStartDate();
    }

    public DonorInfo getDonorInfo() {
        return donorInfo;
    }

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public LocalDate getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public void setDonorInfo(DonorInfo donorInfo) {
        this.donorInfo = donorInfo;
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


}
