package com.irengine.sandbox.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A CUser.
 */
@Entity
@Table(name = "CUSER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "openId")
    private String openId;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "CUSER_COUPON",
               joinColumns = @JoinColumn(name="cusers_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="coupons_id", referencedColumnName="ID"))
    private List<Coupon> coupons = new ArrayList<Coupon>();

	public CUser() {
		
	}
	
	public CUser(String mobile, String openId) {
		this.mobile = mobile;
		this.openId = openId;
		this.coupons = new ArrayList<Coupon>();
	}
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CUser cUser = (CUser) o;

        if ( ! Objects.equals(id, cUser.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CUser{" +
                "id=" + id +
                ", mobile='" + mobile + "'" +
                ", openId='" + openId + "'" +
                '}';
    }
}
