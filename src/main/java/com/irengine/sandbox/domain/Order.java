package com.irengine.sandbox.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.irengine.sandbox.domain.util.CustomLocalDateSerializer;
import com.irengine.sandbox.domain.util.ISO8601LocalDateDeserializer;
import com.irengine.sandbox.domain.util.CustomDateTimeDeserializer;
import com.irengine.sandbox.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.irengine.sandbox.domain.enumeration.DataType;

/**
 * A Order.
 */
@Entity
@Table(name = "SS_ORDER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;



    @Column(name = "my_string")
    private String myString;



    @Column(name = "my_integer")
    private Integer myInteger;



    @Column(name = "my_long")
    private Long myLong;



    @Column(name = "my_float")
    private Float myFloat;



    @Column(name = "my_double")
    private Double myDouble;



    @Column(name = "my_decimal", precision=10, scale=2)
    private BigDecimal myDecimal;



    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "my_date")
    private LocalDate myDate;



    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "my_date_time")
    private DateTime myDateTime;



    @Column(name = "my_boolean")
    private Boolean myBoolean;



    @Enumerated(EnumType.STRING)
    @Column(name = "my_enumeration")
    private DataType myEnumeration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    public Integer getMyInteger() {
        return myInteger;
    }

    public void setMyInteger(Integer myInteger) {
        this.myInteger = myInteger;
    }

    public Long getMyLong() {
        return myLong;
    }

    public void setMyLong(Long myLong) {
        this.myLong = myLong;
    }

    public Float getMyFloat() {
        return myFloat;
    }

    public void setMyFloat(Float myFloat) {
        this.myFloat = myFloat;
    }

    public Double getMyDouble() {
        return myDouble;
    }

    public void setMyDouble(Double myDouble) {
        this.myDouble = myDouble;
    }

    public BigDecimal getMyDecimal() {
        return myDecimal;
    }

    public void setMyDecimal(BigDecimal myDecimal) {
        this.myDecimal = myDecimal;
    }

    public LocalDate getMyDate() {
        return myDate;
    }

    public void setMyDate(LocalDate myDate) {
        this.myDate = myDate;
    }

    public DateTime getMyDateTime() {
        return myDateTime;
    }

    public void setMyDateTime(DateTime myDateTime) {
        this.myDateTime = myDateTime;
    }

    public Boolean getMyBoolean() {
        return myBoolean;
    }

    public void setMyBoolean(Boolean myBoolean) {
        this.myBoolean = myBoolean;
    }

    public DataType getMyEnumeration() {
        return myEnumeration;
    }

    public void setMyEnumeration(DataType myEnumeration) {
        this.myEnumeration = myEnumeration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Order order = (Order) o;

        if ( ! Objects.equals(id, order.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", myString='" + myString + "'" +
                ", myInteger='" + myInteger + "'" +
                ", myLong='" + myLong + "'" +
                ", myFloat='" + myFloat + "'" +
                ", myDouble='" + myDouble + "'" +
                ", myDecimal='" + myDecimal + "'" +
                ", myDate='" + myDate + "'" +
                ", myDateTime='" + myDateTime + "'" +
                ", myBoolean='" + myBoolean + "'" +
                ", myEnumeration='" + myEnumeration + "'" +
                '}';
    }
}
