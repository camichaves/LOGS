package logis.repository;
import logis.domain.Loggss;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Loggss entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoggssRepository extends JpaRepository<Loggss, Long> {

}
