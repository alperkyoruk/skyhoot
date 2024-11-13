package skylab.skyhoot.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;
import skylab.skyhoot.entities.User;

public interface UserDao extends JpaRepository<User,Integer> {
    User findByUsername(String username);

    User findById(int id);

    User findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
