package skylab.skyhoot.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;
import skylab.skyhoot.entities.Question;

import java.util.List;

public interface QuestionDao extends JpaRepository<Question, Integer> {

    Question findById(int id);

    List<Question> findAllByGame_GameId(String gameId);

    List<Question> findAllByIdIn(List<Integer> ids);

    List<Question> findAllByCreatedBy_Username(String username);

}
