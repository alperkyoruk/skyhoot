package skylab.skyhoot.Business.abstracts;


import skylab.skyhoot.core.result.DataResult;
import skylab.skyhoot.core.result.Result;
import skylab.skyhoot.entities.DTOs.Question.CreateQuestionDto;
import skylab.skyhoot.entities.DTOs.Question.GetQuestionDto;
import skylab.skyhoot.entities.DTOs.Question.GetQuestionForHostDto;
import skylab.skyhoot.entities.Question;

import java.util.List;

public interface QuestionService {
    DataResult<Integer> addQuestion(CreateQuestionDto createQuestionDto);

    Result deleteQuestion(int questionId);

    Result updateQuestion(GetQuestionDto getQuestionDto);

    DataResult<GetQuestionDto> getQuestionById(int questionId);

    DataResult<List<GetQuestionDto>> getQuestionsByGameId(String gameId);

    DataResult<List<GetQuestionForHostDto>> getQuestionsByHost();

    DataResult<Question> getQuestionEntityById(int questionId);

    DataResult<List<Question>> getQuestionsByIds(List<Integer> ids);




}
