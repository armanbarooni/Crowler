/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user" ,schema = "public")
public class User {

	private final static Long PASSWORD_EXPIRATION_TIME = 30L * 24L * 60L * 60L * 1000L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "username")
	private String userName;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "created_date")
	private String timeStampCreatedDate;
	
	@Column(name = "expire_date")
	private String timeStampExpireDate;
	
	@Column(name = "last_login")
	private String timeStampLastLogin;

	@Column(name = "failed_attempt")
	private Integer failedAttempt;

	@Column(name = "account_non_locked")
	private Boolean accountNonLocked;

	@Column(name = "lock_time")
	private String timeStampLockTime;

	@Column(name = "is_enabled")
	private Boolean isEnabled;


	
	@ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,
													CascadeType.MERGE,
													CascadeType.PERSIST,
													CascadeType.REFRESH})
	@JoinTable(name = "users_roles",
	joinColumns = @JoinColumn(name ="user_id"),
	inverseJoinColumns = @JoinColumn(name="role_id"))
	private Collection<Role> roles;
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	private List<Log> logs;
	
	public User() {
		
	}

	public User(String userName, String password, String firstName, String lastName,
				String timeStampCreatedDate, String timeStampExpireDate, String timeStampLastLogin,
				Integer failedAttempt, Boolean accountNonLocked, String timeStampLockTime,
				Collection<Role> roles, List<Log> logs) {

		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.timeStampCreatedDate = timeStampCreatedDate;
		this.timeStampExpireDate = timeStampExpireDate;
		this.timeStampLastLogin = timeStampLastLogin;
		this.failedAttempt = failedAttempt;
		this.accountNonLocked = accountNonLocked;
		this.timeStampLockTime = timeStampLockTime;
		this.roles = roles;
		this.logs = logs;

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setTimeStampCreatedDate(String timeStampCreatedDate) {
		this.timeStampCreatedDate = timeStampCreatedDate;
	}

	public void setTimeStampExpireDate(String timeStampExpireDate) {
		this.timeStampExpireDate = timeStampExpireDate;
	}

	public void setTimeStampLastLogin(String timeStampLastLogin) {
		this.timeStampLastLogin = timeStampLastLogin;
	}

	public String getTimeStampCreatedDate() {
		return timeStampCreatedDate;
	}

	public String getTimeStampExpireDate() {
		return timeStampExpireDate;
	}

	public String getTimeStampLastLogin() {
		return timeStampLastLogin;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	
	public List<Log> getLogs() {
		return logs;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}

	public void addLog(Log log) {
		if(logs == null) {
			logs = new ArrayList<>();
		}
		logs.add(log);
		log.setUser(this);
	}

	public static Long getPasswordExpirationTime() {
		return PASSWORD_EXPIRATION_TIME;
	}

	public Integer getFailedAttempt() {
		return failedAttempt;
	}

	public void setFailedAttempt(Integer failedAttempt) {
		this.failedAttempt = failedAttempt;
	}

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public String getTimeStampLockTime() {
		return timeStampLockTime;
	}

	public void setTimeStampLockTime(String timeStampLockTime) {
		this.timeStampLockTime = timeStampLockTime;
	}

	public Boolean getEnabled() {
		return isEnabled;
	}

	public void setEnabled(Boolean enabled) {
		isEnabled = enabled;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", createdDate=" + timeStampCreatedDate + ", expireDate=" + timeStampExpireDate
				+ ", lastLogin=" + timeStampLastLogin + ", roles=" + roles + "]";
	}

}
