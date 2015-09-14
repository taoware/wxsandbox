package com.irengine.sandbox.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A UserBasicInfo.
 */
@Entity
@Table(name = "USERBASICINFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserBasicInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "open_id", nullable = false , unique=true)
    private String openId;

    @Column(name = "mobile")
    private String mobile;

    public enum USERSTATUS{unregistered,registered};
    
    public UserBasicInfo() {
		super();
	}

	public UserBasicInfo(Long id, String openId, String mobile,
			USERSTATUS status) {
		super();
		this.id = id;
		this.openId = openId;
		this.mobile = mobile;
		this.status = status;
	}

	@Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status", nullable = false)
    private USERSTATUS status;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public USERSTATUS getStatus() {
		return status;
	}

	public void setStatus(USERSTATUS status) {
		this.status = status;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserBasicInfo userBasicInfo = (UserBasicInfo) o;

        if ( ! Objects.equals(id, userBasicInfo.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserBasicInfo{" +
                "id=" + id +
                ", openId='" + openId + "'" +
                ", mobile='" + mobile + "'" +
                ", status='" + status + "'" +
                '}';
    }
}
