package com.irengine.sandbox.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A OutNewsMessageItem.
 */
@Entity
@Table(name = "OUTNEWSMESSAGEITEM")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OutNewsMessageItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 1000)
    @Column(name = "pic_url", length = 1000)
    private String picUrl;

    @Size(max = 1000)
    @Column(name = "url", length = 1000)
    private String url;

    @Size(max = 1000)
    @Column(name = "content", length = 1000)
    private String content;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "OUTNEWSMESSAGEITEM_USERINFO",
               joinColumns = @JoinColumn(name="outnewsmessageitems_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="userinfos_id", referencedColumnName="ID"))
    private Set<UserInfo> userInfos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(Set<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OutNewsMessageItem outNewsMessageItem = (OutNewsMessageItem) o;

        if ( ! Objects.equals(id, outNewsMessageItem.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OutNewsMessageItem{" +
                "id=" + id +
                ", picUrl='" + picUrl + "'" +
                ", url='" + url + "'" +
                ", content='" + content + "'" +
                '}';
    }
}
