package com.irengine.sandbox.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WCUser.
 */
@Entity
@Table(name = "WCUSER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WCUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "open_id")
    private String openId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "sex")
    private String sex;

    @Column(name = "city")
    private String city;

    @Column(name = "province")
    private String province;

    @Column(name = "country")
    private String country;

    @Column(name = "union_id")
    private String unionId;

    @Column(name = "mobile")
    private String mobile;

	public WCUser() {
		super();
	}

	public WCUser(String openId, String nickname, String sex, String city,
			String province, String country, String unionId) {
		super();
		this.openId = openId;
		this.nickname = nickname;
		this.sex = sex;
		this.city = city;
		this.province = province;
		this.country = country;
		this.unionId = unionId;
	}
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WCUser wCUser = (WCUser) o;

        if ( ! Objects.equals(id, wCUser.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WCUser{" +
                "id=" + id +
                ", openId='" + openId + "'" +
                ", nickname='" + nickname + "'" +
                ", sex='" + sex + "'" +
                ", city='" + city + "'" +
                ", province='" + province + "'" +
                ", country='" + country + "'" +
                ", unionId='" + unionId + "'" +
                ", mobile='" + mobile + "'" +
                '}';
    }
}
