package pl.com.psipoznan.visitorsregistry.visitorsregistry.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.com.psipoznan.visitorsregistry.visitorsregistry.model.Identyficator;

@Repository
public interface IdentyficatorRepository extends JpaRepository<Identyficator, Long> {

	Identyficator findByKey(String key);
	Identyficator findFirstByActiveAndDeleted(boolean active, boolean deleted);
	Identyficator findFirstByActiveAndDeletedAndKeyLike(boolean active, boolean deleted, String key);
	List<Identyficator> findAll();
	List<Identyficator> findByActive(boolean active);
	List<Identyficator> findByDeleted(boolean deleted);
}
