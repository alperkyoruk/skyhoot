package skylab.skyhoot.Business.abstracts;

import skylab.skyhoot.core.result.DataResult;
import skylab.skyhoot.core.result.Result;
import skylab.skyhoot.entities.AnswerOption;
import skylab.skyhoot.entities.DTOs.AnswerOption.CreateAnswerOptionDto;
import skylab.skyhoot.entities.DTOs.AnswerOption.GetAnswerOptionDto;

import java.util.List;

public interface AnswerOptionService {

    Result addAnswerOption(CreateAnswerOptionDto createAnswerOptionDto);

    Result deleteAnswerOption(int answerOptionId);

    Result updateAnswerOption(GetAnswerOptionDto getAnswerOptionDto);

    DataResult<GetAnswerOptionDto> getAnswerOptionById(int answerOptionId);

    DataResult<List<GetAnswerOptionDto>> getAnswerOptionsByQuestionId(int questionId);

    DataResult<AnswerOption> getAnswerOptionEntityById(int answerOptionId);

    Result updatePlayerCount(int answerOptionId, int playerCount);

    Result clearPlayerCounts(List<Integer> questionIds);





}
