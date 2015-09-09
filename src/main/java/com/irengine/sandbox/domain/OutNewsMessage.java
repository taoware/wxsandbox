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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A OutNewsMessage.
 */
@Entity
@Table(name = "OUTNEWSMESSAGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OutNewsMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "start_date", nullable = false)
    private DateTime startDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "end_date")
    private DateTime endDate;

    @ManyToMany(fetch=FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "OUTNEWSMESSAGE_OUTNEWSMESSAGEITEM",
               joinColumns = @JoinColumn(name="outnewsmessages_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="outnewsmessageitems_id", referencedColumnName="ID"))
    private List<OutNewsMessageItem> outNewsMessageItems = new ArrayList<OutNewsMessageItem>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public List<OutNewsMessageItem> getOutNewsMessageItems() {
		return outNewsMessageItems;
	}

	public void setOutNewsMessageItems(List<OutNewsMessageItem> outNewsMessageItems) {
		this.outNewsMessageItems = outNewsMessageItems;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OutNewsMessage outNewsMessage = (OutNewsMessage) o;

        if ( ! Objects.equals(id, outNewsMessage.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OutNewsMessage{" +
                "id=" + id +
                ", menuName='" + menuName + "'" +
                ", startDate='" + startDate + "'" +
                ", endDate='" + endDate + "'" +
                '}';
    }
}
