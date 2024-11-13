package skylab.skyhoot.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;
import skylab.skyhoot.entities.PlayerAnswer;

import java.util.List;

public interface PlayerAnswerDao extends JpaRepository<PlayerAnswer,Integer> {

    PlayerAnswer findById(int id);

    PlayerAnswer findByPlayerIdAndQuestionId(int playerId, int questionId);

    List<PlayerAnswer> findAllByPlayer_PlayerId(String playerId);

    List<PlayerAnswer> findAllByQuestionId(int questionId);

    List<PlayerAnswer> findAllByAnswerOptionId(int answerOptionId);

    boolean existsByPlayer_PlayerIdAndQuestionId(String playerId, int questionId);

}
