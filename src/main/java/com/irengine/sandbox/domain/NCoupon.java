package com.irengine.sandbox.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.irengine.sandbox.domain.util.CustomDateTimeDeserializer;
import com.irengine.sandbox.domain.util.CustomDateTimeSerializer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A NCoupon.
 */
@Entity
@Table(name = "NCOUPON")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NCoupon implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code")
    private String code;

    public enum COUPONSTATUS{UNUSED,USED,CANCELED,SENT,SENT_SUCCESS,SENT_FAILURE};
    
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status", nullable = false)
    private COUPONSTATUS status;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "created_time", nullable = false)
    private DateTime createdTime;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "modifed_time", nullable = false)
    private DateTime modifedTime;

    @ManyToOne
    private CouponBatch couponBatch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

	public COUPONSTATUS getStatus() {
		return status;
	}

	public void setStatus(COUPONSTATUS status) {
		this.status = status;
	}

	public DateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(DateTime createdTime) {
        this.createdTime = createdTime;
    }

    public DateTime getModifedTime() {
        return modifedTime;
    }

    public void setModifedTime(DateTime modifedTime) {
        this.modifedTime = modifedTime;
    }

    public CouponBatch getCouponBatch() {
        return couponBatch;
    }

    public void setCouponBatch(CouponBatch couponBatch) {
        this.couponBatch = couponBatch;
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NCoupon nCoupon = (NCoupon) o;

        if ( ! Objects.equals(id, nCoupon.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NCoupon{" +
                "id=" + id +
                ", code='" + code + "'" +
                ", status='" + status + "'" +
                ", createdTime='" + createdTime + "'" +
                ", modifedTime='" + modifedTime + "'" +
                '}';
    }
}
