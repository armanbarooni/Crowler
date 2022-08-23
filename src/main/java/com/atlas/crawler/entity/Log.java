/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.entity;

import javax.persistence.*;


@Entity
@Table(name = "log",schema = "public")
public class Log {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "event_result")
	private Integer eventResult;
	
	@Column(name = "event_type")
	private String eventType;
	

	@Column(name = "start_date")
	private Long timeStampStartDate;

	@Column(name = "finish_date")
	private Long timeStampFinishDate;
	
	@Column(name = "form_name")
	private String formName;
	
	@Column(name = "browser_name")
	private String browserName;
	
	@Column(name = "ip")
	private String ip;
	
	@Column(name = "port")
	private Integer port;
		
	@ManyToOne(cascade = {
			CascadeType.DETACH,
			CascadeType.MERGE,
			CascadeType.PERSIST,
			CascadeType.REFRESH
	})
	@JoinColumn(name = "user_id")
	private User user;
	
	public Log() {
		
	}

	public Log(Integer eventResult, String eventType, Long timeStampStartDate, Long timeStampFinishDate, String formName,
			String browserName, String ip, Integer port, User user) {
		this.eventResult = eventResult;
		this.eventType = eventType;
		this.timeStampStartDate = timeStampStartDate;
		this.timeStampFinishDate = timeStampFinishDate;
		this.formName = formName;
		this.browserName = browserName;
		this.ip = ip;
		this.port = port;
		this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEventResult() {
		return eventResult;
	}

	public void setEventResult(Integer eventResult) {
		this.eventResult = eventResult;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}



	public Long getTimeStampStartDate() {
		return timeStampStartDate;
	}

	public void setTimeStampStartDate(Long timeStampStartDate) {
		this.timeStampStartDate = timeStampStartDate;
	}

	public Long getTimeStampFinishDate() {
		return timeStampFinishDate;
	}

	public void setTimeStampFinishDate(Long timeStampFinishDate) {
		this.timeStampFinishDate = timeStampFinishDate;
	}

	

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Log [id=" + id + ", eventResult=" + eventResult + ", eventType=" + eventType + 
				", browserName=" + browserName + ", ip=" + ip + ", port=" + port + ", user=" + user + "]";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
