package pl.com.psipoznan.visitorsregistry.visitorsregistry.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="identyficators")
public class Identyficator {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String key;
	private String lastUserId;
	private boolean active;
	private boolean deleted;
	
	public Identyficator() {}
	
	public Identyficator(String key) {
		this.key = key;
		this.deleted = false;
		this.active = false;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getLastUserId() {
		return lastUserId;
	}
	public void setLastUserId(String lastUserId) {
		this.lastUserId = lastUserId;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
