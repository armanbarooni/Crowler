/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.model;

import com.atlas.crawler.entity.Role;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Collection;


public class CrmUser {



	@NotNull(message = "User Name is required")
	@Size(min = 4,max = 16, message = "طول نام کاربری حداقل 4 و حداکثر 16 حرف می تواند باشد")
	private String userName;

	@NotNull(message = "passwordRequired")
	@Size(min = 4,max = 32, message = "طول رمز عبور حداقل 4 و حداکثر 32 حرف می تواند باشد")
	private String password;

	@NotNull(message = "First Name is Required")
	@Size(min = 4,max = 32, message = "طول نام حداقل 4 و حداکثر 32 حرف می تواند باشد")
	private String firstName;

	@NotNull(message = "Last Name is Required")
	@Size(min = 4,max = 32, message = "طول نام خانوادگی حداقل 4 و حداکثر 32 حرف می تواند باشد")
	private String lastName;

	@NotNull(message = "Role Name is Required")
	@Size(min = 4,max = 16, message = "نقش وارد شده صحیح نیست")
	private String roleName;

	private Collection<Role> roles;

	public CrmUser() {
		
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "CrmUser{" +
				"userName='" + userName + '\'' +
				", password='" + password + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", roleName='" + roleName + '\'' +
				'}';
	}
}
