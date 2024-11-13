package skylab.skyhoot.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;
import skylab.skyhoot.entities.AnswerOption;
import skylab.skyhoot.entities.Question;

import java.util.List;

public interface AnswerOptionDao extends JpaRepository<AnswerOption, Integer> {

    List<AnswerOption> findAllByQuestion_Id(int questionId);

    AnswerOption findById(int id);






}
