package pl.com.psipoznan.visitorsregistry.visitorsregistry.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.com.psipoznan.visitorsregistry.visitorsregistry.model.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {
	
	List<Visitor> findAll();
	Visitor findByName(String name);
	Visitor findByTicket(long ticket);
	Visitor findByCompany(String company);

}
