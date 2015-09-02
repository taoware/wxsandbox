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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A OutMessage.
 */
@Entity
@Table(name = "OUTMESSAGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OutMessage implements Serializable {

	private static final long serialVersionUID = 2971642595523378313L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @Size(max = 1000)
    @Column(name = "url", length = 1000)
    private String url;

    @Size(max = 1000)
    @Column(name = "pic_url", length = 1000)
    private String picUrl;

    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "start_date", nullable = false)
    private DateTime startDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "end_date", nullable = false)
    private DateTime endDate;

    @Column(name = "disable")
    private Boolean disable;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "OUTMESSAGE_WCUSERS",
               joinColumns = @JoinColumn(name="outmessages_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="wcuserss_id", referencedColumnName="ID"))
    private Set<WCUser> wcUserss = new HashSet<>();

	public long getCount() {
		return wcUserss.size();
	}

	public String getReturnContent(){
		return getsubstring(content, 60);
	}
	
	public String getStatusText(){
		Date now = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(endDate.toDate());
		calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		Date endDate2 = calendar.getTime();
		if(disable==true){
			if(now.after(endDate2)){
				return "已结束";
			}else{
				return "已关闭";
			}
		}else{
			if(now.before(startDate.toDate())){
				return "未进行";
			}else if(now.after(endDate2)){
				return "已结束";
			}else{
				return "进行中";
			}
		}
	}
	
	public OutMessage() {
		super();
	}

	public OutMessage(String type, String content, String url, String picUrl,
			String title, String menuName, DateTime startDate, DateTime endDate,
			boolean disable) {
		super();
		this.type = type;
		this.content = content;
		this.url = url;
		this.picUrl = picUrl;
		this.title = title;
		this.menuName = menuName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.disable = disable;
	}
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public Set<WCUser> getWcUserss() {
        return wcUserss;
    }

    public void setWcUserss(Set<WCUser> wCUsers) {
        this.wcUserss = wCUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OutMessage outMessage = (OutMessage) o;

        if ( ! Objects.equals(id, outMessage.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OutMessage{" +
                "id=" + id +
                ", type='" + type + "'" +
                ", content='" + content + "'" +
                ", url='" + url + "'" +
                ", picUrl='" + picUrl + "'" +
                ", title='" + title + "'" +
                ", menuName='" + menuName + "'" +
                ", startDate='" + startDate + "'" +
                ", endDate='" + endDate + "'" +
                ", disable='" + disable + "'" +
                '}';
    }
    
	/**处理文本消息字符串过长显示不美观问题*/
	private String getsubstring(String content, int sublength) {
		int length = content.getBytes().length;
		if (sublength >= length) {
			return content;
		} else {
				int i = 0;
				int j = 0;
				for (; i < length; i++) {
					if (content.substring(0, i + 1).getBytes().length >= sublength) {
						j = content.substring(0, i + 1).getBytes().length;
						break;
					}

				}
				String substring = j > sublength ? content.substring(0, i)
						: content.substring(0, i + 1);
				return substring+"...";
		}
	}
	
}
