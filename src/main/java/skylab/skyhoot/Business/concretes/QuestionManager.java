package skylab.skyhoot.Business.concretes;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import skylab.skyhoot.Business.abstracts.AnswerOptionService;
import skylab.skyhoot.Business.abstracts.GameService;
import skylab.skyhoot.Business.abstracts.QuestionService;
import skylab.skyhoot.Business.abstracts.UserService;
import skylab.skyhoot.Business.constants.Messages;
import skylab.skyhoot.core.result.*;
import skylab.skyhoot.dataAccess.QuestionDao;
import skylab.skyhoot.entities.DTOs.Question.CreateQuestionDto;
import skylab.skyhoot.entities.DTOs.Question.GetQuestionDto;
import skylab.skyhoot.entities.DTOs.Question.GetQuestionForHostDto;
import skylab.skyhoot.entities.Question;

import java.util.List;

@Service
public class QuestionManager implements QuestionService {

    private QuestionDao questionDao;
    private GameService gameService;
    private AnswerOptionService answerOptionService;
    private UserService userService;

    public QuestionManager(QuestionDao questionDao, @Lazy GameService gameService, @Lazy AnswerOptionService answerOptionService, @Lazy UserService userService) {
        this.questionDao = questionDao;
        this.gameService = gameService;
        this.answerOptionService = answerOptionService;
        this.userService = userService;
    }

    @Override
    public DataResult<Integer> addQuestion(CreateQuestionDto createQuestionDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        var user = userService.getUserEntityByUsername(currentPrincipalName).getData();
        if(user == null){
            return new ErrorDataResult<>(Messages.userNotFound);
        }

        Question question = Question.builder()
                .question(createQuestionDto.getQuestion())
                .timeLimit(createQuestionDto.getTimeLimit())
                .score(createQuestionDto.getScore())
                .createdBy(user)
                .build();

        questionDao.save(question);
        return new SuccessDataResult<>(question.getId(),Messages.questionAdded);
    }

    @Override
    public Result deleteQuestion(int questionId) {
        var question = questionDao.findById(questionId);
        if(question == null){
            return new ErrorResult(Messages.questionNotFound);
        }
        questionDao.delete(question);
        return new SuccessResult(Messages.questionDeleted);
    }

    @Override
    public Result updateQuestion(GetQuestionDto getQuestionDto) {
        var question = questionDao.findById(getQuestionDto.getId());
        if(question == null){
            return new ErrorResult(Messages.questionNotFound);
        }
        question.setQuestion(getQuestionDto.getQuestion() != null ? getQuestionDto.getQuestion() : question.getQuestion());
        question.setTimeLimit(getQuestionDto.getTimeLimit() != 0 ? getQuestionDto.getTimeLimit() : question.getTimeLimit());
        question.setScore(getQuestionDto.getScore() != 0 ? getQuestionDto.getScore() : question.getScore());
        questionDao.save(question);
        return new SuccessResult(Messages.questionUpdated);
    }

    @Override
    public DataResult<GetQuestionDto> getQuestionById(int questionId) {
        var question = questionDao.findById(questionId);
        if(question == null){
            return new ErrorDataResult<>(Messages.questionNotFound);
        }
        var returnQuestion = new GetQuestionDto(question);
        return new SuccessDataResult<>(returnQuestion, Messages.questionFound);
    }

    @Override
    public DataResult<List<GetQuestionDto>> getQuestionsByGameId(String gameId) {
        var game = gameService.getGameByGameId(gameId).getData();
        if(game == null){
            return new ErrorDataResult<>(Messages.gameNotFound);
        }
        var questions = questionDao.findAllByGame_GameId(gameId);
        if(questions.isEmpty()){
            return new ErrorDataResult<>(Messages.questionsNotFound);
        }
        var returnQuestions = new GetQuestionDto().buildListGetQuestionDto(questions);
        return new SuccessDataResult<>(returnQuestions, Messages.questionsFound);
    }

    @Override
    public DataResult<List<GetQuestionForHostDto>> getQuestionsByHost() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        var questions = questionDao.findAllByCreatedBy_Username(currentPrincipalName);
        if(questions.isEmpty()){
            return new ErrorDataResult<>(Messages.questionsNotFound);
        }

        var returnQuestions = new GetQuestionForHostDto().buildListGetQuestionForHostDto(questions);
        return new SuccessDataResult<>(returnQuestions, Messages.questionsFound);
    }

    @Override
    public DataResult<Question> getQuestionEntityById(int questionId) {
        var question = questionDao.findById(questionId);
        if(question == null){
            return new ErrorDataResult<>(Messages.questionNotFound);
        }
        return new SuccessDataResult<>(question, Messages.questionFound);
    }

    @Override
    public DataResult<List<Question>> getQuestionsByIds(List<Integer> ids) {
        var questions = questionDao.findAllByIdIn(ids);
        if(questions.isEmpty()){
            return new ErrorDataResult<>(Messages.questionsNotFound);
        }
        return new SuccessDataResult<>(questions, Messages.questionsFound);
    }
}
