package com.zxsoft.fanfanfamily.base.domain.vo;

import java.util.ArrayList;
import java.util.List;

public class Dashboard {
    private List<HeadNumber> headNumbers = new ArrayList<>();
    private List<MortAmount> mortAmounts = new ArrayList<>();
    private BestEmployeeMonth bestEmployeeMonth = new BestEmployeeMonth();
    private List<CompleteRateByDept> completeRateByDepts = new ArrayList<>();
    private List<CompleteRateByBank> completeRateByBanks = new ArrayList<>();

    public List<HeadNumber> getHeadNumbers() {
        return headNumbers;
    }

    public void setHeadNumbers(List<HeadNumber> headNumbers) {
        this.headNumbers = headNumbers;
    }

    public List<MortAmount> getMortAmounts() {
        return mortAmounts;
    }

    public void setMortAmounts(List<MortAmount> mortAmounts) {
        this.mortAmounts = mortAmounts;
    }

    public BestEmployeeMonth getBestEmployeeMonth() {
        return bestEmployeeMonth;
    }

    public void setBestEmployeeMonth(BestEmployeeMonth bestEmployeeMonth) {
        this.bestEmployeeMonth = bestEmployeeMonth;
    }

    public List<CompleteRateByDept> getCompleteRateByDepts() {
        return completeRateByDepts;
    }

    public void setCompleteRateByDepts(List<CompleteRateByDept> completeRateByDepts) {
        this.completeRateByDepts = completeRateByDepts;
    }

    public List<CompleteRateByBank> getCompleteRateByBanks() {
        return completeRateByBanks;
    }

    public void setCompleteRateByBanks(List<CompleteRateByBank> completeRateByBanks) {
        this.completeRateByBanks = completeRateByBanks;
    }
}
