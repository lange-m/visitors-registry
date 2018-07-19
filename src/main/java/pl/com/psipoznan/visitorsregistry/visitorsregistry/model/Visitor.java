package pl.com.psipoznan.visitorsregistry.visitorsregistry.model;

import java.time.LocalDate;

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
	private LocalDate enter;
	private LocalDate exit;
	private long ticket;
	
	public Visitor() {}
	
	public Visitor(String name, String company) {
		this.name = name;
		this.company = company;
		this.enter = LocalDate.now();
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
	public LocalDate getEnter() {
		return enter;
	}
	public void setEnter(LocalDate enter) {
		this.enter = enter;
	}
	public LocalDate getExit() {
		return exit;
	}
	public void setExit(LocalDate exit) {
		this.exit = exit;
	}
	public long getTicket() {
		return ticket;
	}
	public void setTicket(long ticket) {
		this.ticket = ticket;
	}
}
