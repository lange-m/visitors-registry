package pl.com.psipoznan.visitorsregistry.visitorsregistry.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="visitors")
public class Visitor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	private String company;
	private LocalDateTime enter;
	private LocalDateTime exit;
	private String ticket;
	
	public Visitor() {}
	
	public Visitor(String name, String company) {
		this.name = name;
		this.company = company;
		this.enter = LocalDateTime.now();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public LocalDateTime getEnter() {
		return enter;
	}
	public void setEnter(LocalDateTime enter) {
		this.enter = enter;
	}
	public LocalDateTime getExit() {
		return exit;
	}
	public void setExit(LocalDateTime exit) {
		this.exit = exit;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
}
