package skylab.skyhoot.Business.concretes;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import skylab.skyhoot.Business.abstracts.AnswerOptionService;
import skylab.skyhoot.Business.abstracts.QuestionService;
import skylab.skyhoot.Business.constants.Messages;
import skylab.skyhoot.core.result.*;
import skylab.skyhoot.dataAccess.AnswerOptionDao;
import skylab.skyhoot.entities.AnswerOption;
import skylab.skyhoot.entities.DTOs.AnswerOption.CreateAnswerOptionDto;
import skylab.skyhoot.entities.DTOs.AnswerOption.GetAnswerOptionDto;

import java.util.List;

@Service
public class AnswerOptionManager implements AnswerOptionService {


    private AnswerOptionDao answerOptionDao;
    private QuestionService questionService;

    public AnswerOptionManager(AnswerOptionDao answerOptionDao, QuestionService questionService) {
        this.answerOptionDao = answerOptionDao;
        this.questionService = questionService;
    }

    @Override
    public Result addAnswerOption(CreateAnswerOptionDto createAnswerOptionDto) {


        var question = questionService.getQuestionEntityById(createAnswerOptionDto.getQuestionId()).getData();
        if(question == null) {
            return new ErrorResult(Messages.questionNotFound);
        }

        if(createAnswerOptionDto.getIsCorrect() != 0 && createAnswerOptionDto.getIsCorrect() != 1) {
            return new ErrorResult(Messages.isCorrectValueError);
        }


        AnswerOption answerOption = AnswerOption.builder()
                .option(createAnswerOptionDto.getOption())
                .isCorrect(createAnswerOptionDto.getIsCorrect()==1 ? true : false)
                .question(question)
                .build();

        answerOptionDao.save(answerOption);
        return new SuccessResult(Messages.answerOptionAdded);
    }

    @Override
    public Result deleteAnswerOption(int answerOptionId) {
        var answerOption = answerOptionDao.findById(answerOptionId);
        if(answerOption == null) {
            return new ErrorResult(Messages.answerOptionNotFound);
        }
        answerOptionDao.delete(answerOption);
        return new SuccessResult(Messages.answerOptionDeleted);
    }

    @Override
    public Result updateAnswerOption(GetAnswerOptionDto getAnswerOptionDto) {
        var answerOption = answerOptionDao.findById(getAnswerOptionDto.getId());
        if(answerOption == null) {
            return new ErrorResult(Messages.answerOptionNotFound);
        }

        answerOption.setOption(getAnswerOptionDto.getOption() != null ? getAnswerOptionDto.getOption() : answerOption.getOption());
        answerOption.setCorrect(getAnswerOptionDto.isCorrect());
        answerOptionDao.save(answerOption);
        return new SuccessResult(Messages.answerOptionUpdated);
    }

    @Override
    public DataResult<GetAnswerOptionDto> getAnswerOptionById(int answerOptionId) {
        var answerOption = answerOptionDao.findById(answerOptionId);
        if(answerOption == null) {
            return new ErrorDataResult<>(Messages.answerOptionNotFound);
        }

        var returnAnswerOption = new GetAnswerOptionDto(answerOption);
        return new SuccessDataResult<>(returnAnswerOption, Messages.answerOptionFound);
    }

    @Override
    public DataResult<List<GetAnswerOptionDto>> getAnswerOptionsByQuestionId(int questionId) {
        var answerOptions = answerOptionDao.findAllByQuestion_Id(questionId);
        if(answerOptions.isEmpty()) {
            return new ErrorDataResult<>(Messages.answerOptionNotFound);
        }

        List<GetAnswerOptionDto> returnList = new GetAnswerOptionDto().buildListGetAnswerOptionDto(answerOptions);
        return new SuccessDataResult<>(returnList, Messages.answerOptionFound);
    }

    @Override
    public DataResult<AnswerOption> getAnswerOptionEntityById(int answerOptionId) {
        var answerOption = answerOptionDao.findById(answerOptionId);
        if(answerOption == null) {
            return new ErrorDataResult<>(Messages.answerOptionNotFound);
        }
        return new SuccessDataResult<>(answerOption, Messages.answerOptionFound);
    }
}
