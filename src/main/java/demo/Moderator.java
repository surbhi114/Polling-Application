package demo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class Moderator {
	
	
	private int moderator_id;
	
	//@NotEmpty(message="name can not be empty")
	private String name;
	
	@Email
	private String email;
	
	@NotEmpty(message="password can not be empty")
	private String password;
	
	private String created_at;
	
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public int getId() {
		return moderator_id;
	}
	public void setId(int moderator_id) {
		this.moderator_id = moderator_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
