package skylab.skyhoot.Business.abstracts;

import skylab.skyhoot.core.result.DataResult;
import skylab.skyhoot.core.result.Result;
import skylab.skyhoot.entities.DTOs.PlayerAnswer.CreatePlayerAnswerDto;
import skylab.skyhoot.entities.DTOs.PlayerAnswer.GetPlayerAnswerDto;
import skylab.skyhoot.entities.PlayerAnswer;

import java.util.List;

public interface PlayerAnswerService {
    DataResult<PlayerAnswer> addPlayerAnswer(CreatePlayerAnswerDto createPlayerAnswerDto);

    Result deletePlayerAnswer(int playerAnswerId);

    DataResult<GetPlayerAnswerDto> getPlayerAnswerById(int playerAnswerId);

    DataResult<List<GetPlayerAnswerDto>> getPlayerAnswersByPlayerId(int playerId);

    DataResult<List<GetPlayerAnswerDto>> getPlayerAnswersByQuestionId(int questionId);

    Result validatePlayerAnswer(int playerAnswerId);


}
