package com.irengine.sandbox.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.irengine.sandbox.domain.util.CustomDateTimeDeserializer;
import com.irengine.sandbox.domain.util.CustomDateTimeSerializer;

/**
 * A Activity.
 */
@Entity
@Table(name = "ACTIVITY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Activity implements Serializable {

	private static final long serialVersionUID = 6387031578498968769L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "disable")
    private Boolean disable;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "index_name", nullable = false)
    private String indexName;

    @NotNull
    @Column(name = "folder_name", nullable = false)
    private String folderName;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Size(max = 1000)
    @Column(name = "url", length = 1000)
    private String url;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "start_date")
    private DateTime startDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "end_date")
    private DateTime endDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "ACTIVITY_WCUSERS",
               joinColumns = @JoinColumn(name="activitys_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="wcuserss_id", referencedColumnName="ID"))
    private List<WCUser> wcUserss = new ArrayList<WCUser>();

	public Activity() {
		super();
	}

	public Activity(boolean disable, String name, String indexName,
			String folderName, String type, String description, String url,
			DateTime startDate, DateTime endDate) {
		super();
		this.disable = disable;
		this.name = name;
		this.indexName = indexName;
		this.folderName = folderName;
		this.type = type;
		this.description = description;
		this.url = url;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public long getCount() {
		if(wcUserss!=null){
			return wcUserss.size();
		}
		return 0L;
	}
	
	public String getStatusText(){
		Date now = new Date();
		Date startDate2=startDate.toDate();
		Date endDate2=endDate.toDate();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(endDate2);
		calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		endDate2 = calendar.getTime();
		if(disable==true){
			if(now.after(endDate2)){
				return "已结束";
			}else{
				return "已关闭";
			}
		}else{
			if(now.before(startDate2)){
				return "未进行";
			}else if(now.after(endDate2)){
				return "已结束";
			}else{
				return "进行中";
			}
		}
	}
	
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public List<WCUser> getWcUserss() {
        return wcUserss;
    }

    public void setWcUserss(List<WCUser> wCUsers) {
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

        Activity activity = (Activity) o;

        if ( ! Objects.equals(id, activity.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", disable='" + disable + "'" +
                ", name='" + name + "'" +
                ", indexName='" + indexName + "'" +
                ", folderName='" + folderName + "'" +
                ", type='" + type + "'" +
                ", description='" + description + "'" +
                ", url='" + url + "'" +
                ", startDate='" + startDate + "'" +
                ", endDate='" + endDate + "'" +
                '}';
    }
}
