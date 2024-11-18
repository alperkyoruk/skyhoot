package skylab.skyhoot.Business.concretes;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import skylab.skyhoot.Business.abstracts.*;
import skylab.skyhoot.Business.constants.Messages;
import skylab.skyhoot.core.result.*;
import skylab.skyhoot.dataAccess.PlayerAnswerDao;
import skylab.skyhoot.entities.DTOs.PlayerAnswer.CreatePlayerAnswerDto;
import skylab.skyhoot.entities.DTOs.PlayerAnswer.GetPlayerAnswerDto;
import skylab.skyhoot.entities.PlayerAnswer;

import java.sql.Timestamp;
import java.util.List;

@Service
public class PlayerAnswerManager implements PlayerAnswerService {

    private PlayerAnswerDao playerAnswerDao;
    private PlayerService playerService;
    private QuestionService questionService;
    private GameService gameService;
    private AnswerOptionService answerOptionService;

    public PlayerAnswerManager(@Lazy PlayerAnswerDao playerAnswerDao, @Lazy PlayerService playerService, @Lazy QuestionService questionService, @Lazy GameService gameService, @Lazy AnswerOptionService answerOptionService) {
        this.playerAnswerDao = playerAnswerDao;
        this.playerService = playerService;
        this.questionService = questionService;
        this.gameService = gameService;
        this.answerOptionService = answerOptionService;
    }

    @Override
    public DataResult<PlayerAnswer> addPlayerAnswer(CreatePlayerAnswerDto createPlayerAnswerDto) {
        var player = playerService.getPlayerEntityByPlayerId(createPlayerAnswerDto.getPlayerId()).getData();
        if(player == null) {
            return new ErrorDataResult<>(Messages.playerNotFound);
        }
        var question = questionService.getQuestionEntityById(createPlayerAnswerDto.getQuestionId()).getData();
        if(question == null) {
            return new ErrorDataResult<>(Messages.questionNotFound);
        }
        var game = gameService.getGameEntityByGameId(createPlayerAnswerDto.getGameId()).getData();
        if(game == null) {
            return new ErrorDataResult<>(Messages.gameNotFound);
        }
        var answerOption = answerOptionService.getAnswerOptionEntityById(createPlayerAnswerDto.getAnswerOptionId()).getData();
        if(answerOption == null) {
            return new ErrorDataResult<>(Messages.answerOptionNotFound);
        }
        if(playerAnswerDao.existsByPlayer_PlayerIdAndQuestionId(player.getPlayerId(), question.getId())) {
            return new ErrorDataResult<>(Messages.playerAnswerAlreadyExists);
        }

        PlayerAnswer playerAnswer = PlayerAnswer.builder()
                .player(player)
                .question(question)
                .game(game)
                .answerOption(answerOption)
                .answeredAt(new Timestamp(System.currentTimeMillis()))
                .timeTaken(createPlayerAnswerDto.getTimeTaken())
                .build();

        answerOptionService.updatePlayerCount(answerOption.getId(), answerOption.getPlayerCount() + 1);
        playerAnswerDao.save(playerAnswer);
        validatePlayerAnswer(playerAnswer.getId());
        return new SuccessDataResult<>(playerAnswer,Messages.playerAnswerAdded);
    }

    @Override
    public Result deletePlayerAnswer(int playerAnswerId) {
        var playerAnswer = playerAnswerDao.findById(playerAnswerId);
        if(playerAnswer == null) {
            return new ErrorResult(Messages.playerAnswerNotFound);
        }
        playerAnswerDao.delete(playerAnswer);

        return new SuccessResult(Messages.playerAnswerDeleted);
    }

    @Override
    public DataResult<GetPlayerAnswerDto> getPlayerAnswerById(int playerAnswerId) {
        var playerAnswer = playerAnswerDao.findById(playerAnswerId);
        if(playerAnswer == null) {
            return new ErrorDataResult<>(Messages.playerAnswerNotFound);
        }
        var returnPlayerAnswer = new GetPlayerAnswerDto(playerAnswer);
        return new SuccessDataResult<>(returnPlayerAnswer, Messages.playerAnswerFound);
    }

    @Override
    public DataResult<List<GetPlayerAnswerDto>> getPlayerAnswersByPlayerId(int playerId) {
        var player = playerService.getPlayerEntityById(playerId).getData();
        if(player == null) {
            return new ErrorDataResult<>(Messages.playerNotFound);
        }
        var playerAnswers = playerAnswerDao.findAllByPlayer_PlayerId(player.getPlayerId());
        if(playerAnswers.isEmpty()) {
            return new ErrorDataResult<>(Messages.playerAnswersNotFound);
        }
        var returnList = new GetPlayerAnswerDto().buildListGetPlayerAnswerDto(playerAnswers);
        return new SuccessDataResult<>(returnList, Messages.playerAnswersFound);
    }

    @Override
    public DataResult<List<GetPlayerAnswerDto>> getPlayerAnswersByQuestionId(int questionId) {

        var playerAnswers = playerAnswerDao.findAllByQuestionId(questionId);
        if(playerAnswers.isEmpty()) {
            return new ErrorDataResult<>(Messages.playerAnswersNotFound);
        }
        var returnList = new GetPlayerAnswerDto().buildListGetPlayerAnswerDto(playerAnswers);
        return new SuccessDataResult<>(returnList, Messages.playerAnswersFound);
    }


    @Override
    public Result validatePlayerAnswer(int playerAnswerId) {
        var playerAnswer = playerAnswerDao.findById(playerAnswerId);
        if(playerAnswer == null) {
            return new ErrorResult(Messages.playerAnswerNotFound);
        }
        var answerOption = playerAnswer.getAnswerOption();
        var question = playerAnswer.getQuestion();
        var player = playerAnswer.getPlayer();
        if(answerOption.isCorrect()) {
            var score = question.getScore() - ((int)playerAnswer.getTimeTaken()* 2);
            playerAnswer.setScore(score);
            player.setScore(player.getScore() + score);
            playerService.updatePlayerScore(player.getPlayerId(), player.getScore());

            playerAnswerDao.save(playerAnswer);
        }

        return new SuccessResult(Messages.playerAnswerValidated);
    }
}
