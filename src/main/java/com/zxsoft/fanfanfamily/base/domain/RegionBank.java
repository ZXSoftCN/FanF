package com.zxsoft.fanfanfamily.base.domain;

import com.zxsoft.fanfanfamily.base.domain.mort.Bank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "sys_region_bank")
public class RegionBank implements Serializable {

    private Region region;
    private Bank bank;

    @Id
    @ManyToOne
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Id
    @ManyToOne
    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(region,bank);
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        RegionBank that = (RegionBank) obj;
        return Objects.equals( region, that.region ) &&
                Objects.equals( bank, that.bank );
    }
}
