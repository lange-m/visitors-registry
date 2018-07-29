package pl.com.psipoznan.visitorsregistry.visitorsregistry.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.com.psipoznan.visitorsregistry.visitorsregistry.model.RememberedVisitor;

@Repository
public interface RememberedVisitorRepository extends JpaRepository<RememberedVisitor, Long> {

	List<RememberedVisitor> findAll();
	RememberedVisitor findByLogin(String login);
	RememberedVisitor findByName(String name);
	RememberedVisitor findByCompany(String company);
	
}
