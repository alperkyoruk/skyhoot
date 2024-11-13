package skylab.skyhoot.Business.concretes;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import skylab.skyhoot.Business.abstracts.GameService;
import skylab.skyhoot.Business.abstracts.PlayerService;
import skylab.skyhoot.Business.abstracts.QuestionService;
import skylab.skyhoot.Business.abstracts.UserService;
import skylab.skyhoot.Business.constants.Messages;
import skylab.skyhoot.core.result.*;
import skylab.skyhoot.dataAccess.GameDao;
import skylab.skyhoot.entities.DTOs.Game.*;
import skylab.skyhoot.entities.DTOs.Player.GetLeaderboardDto;
import skylab.skyhoot.entities.DTOs.Player.GetPlayerDto;
import skylab.skyhoot.entities.Game;
import skylab.skyhoot.entities.Player;
import skylab.skyhoot.entities.Question;
import skylab.skyhoot.entities.Status;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class GameManager implements GameService {


    private GameDao gameDao;
    private QuestionService questionService;
    private PlayerService playerService;
    private UserService userService;
    private SimpMessagingTemplate messagingTemplate;


    public GameManager(GameDao gameDao, QuestionService questionService, PlayerService playerService, UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.gameDao = gameDao;
        this.questionService = questionService;
        this.playerService = playerService;
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    @Override
    public DataResult<String> addGame(CreateGameDto createGameDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        var user = userService.getUserEntityByUsername(currentPrincipalName).getData();
        if(user == null) {
            return new ErrorDataResult<>(Messages.userNotFound);
        }

        var questions = questionService.getQuestionsByIds(createGameDto.getQuestionIds()).getData();
        if(questions == null) {
            return new ErrorDataResult<>(Messages.questionNotFound);
        }

        //arrange sequence numbers of the questions from 1 to n
        for (int i = 0; i < questions.size(); i++) {
            questions.get(i).setSequenceNumber(i + 1);
        }


        Game game = Game.builder()
                .maxPlayers(createGameDto.getMaxPlayers())
                .gameCode(generateUniqueGameCode())
                .questions(questions)
                .gameId(UUID.randomUUID().toString())
                .status(Status.ACTIVE)
                .questionCount(questions.size())
                .currentPlayers(0)
                .createdAt(new Date())
                .host(user)
                .build();

        gameDao.save(game);
        return new SuccessDataResult<>(game.getGameId(),Messages.gameAdded);
    }

    @Override
    public Result deleteGame(int id) {
        var game = gameDao.findById(id);
        if(game.isEmpty()) {
            return new ErrorResult(Messages.gameNotFound);
        }
        gameDao.delete(game.get());
        return new SuccessResult(Messages.gameDeleted);
    }

    @Override
    public Result updateGame(GetGameDto getGameDto) {
        var game = gameDao.findById(getGameDto.getId());
        if(game.isEmpty()) {
            return new ErrorResult(Messages.gameNotFound);
        }
        game.get().setStartedAt(getGameDto.getStartedAt());
        game.get().setStatus(getGameDto.getStatus());
        gameDao.save(game.get());
        return new SuccessResult(Messages.gameUpdated);
    }

    @Override
    public Result joinGame(String gameId, int playerId) {
        var game = gameDao.findByGameId(gameId);
        if(game == null) {
            return new ErrorResult(Messages.gameNotFound);
        }
        var player = playerService.getPlayerEntityById(playerId).getData();
        if(player == null) {
            return new ErrorResult(Messages.playerNotFound);
        }
        if(game.getCurrentPlayers() == game.getMaxPlayers()) {
            return new ErrorResult(Messages.gameFull);
        }
        game.getPlayers().add(player);
        game.setCurrentPlayers(game.getCurrentPlayers() + 1);
        gameDao.save(game);
        return new SuccessResult(Messages.playerJoined);
    }

    @Override
    public Result leaveGame(String gameId, int playerId) {
        var game = gameDao.findByGameId(gameId);
        if(game == null) {
            return new ErrorResult(Messages.gameNotFound);
        }
        var player = playerService.getPlayerEntityById(playerId).getData();
        if(player == null) {
            return new ErrorResult(Messages.playerNotFound);
        }
        game.getPlayers().remove(player);
        game.setCurrentPlayers(game.getCurrentPlayers() - 1);
        gameDao.save(game);
        return new SuccessResult(Messages.playerLeft);
    }

    @Override
    public DataResult<GetGameDto> getGameByGameId(String gameId) {
        var game = gameDao.findByGameId(gameId);
        if(game == null) {
            return new ErrorDataResult<>(Messages.gameNotFound);
        }

        var returnGame = new GetGameDto(game);
        return new SuccessDataResult<>(returnGame, Messages.GameSuccessfullyBrought);
    }

    @Override
    public DataResult<List<GetGameDto>> getGamesByHostId(int hostId) {
        var game = gameDao.findAllByHostIdOrderByStatus(hostId);
        if(game == null) {
            return new ErrorDataResult<>(Messages.gameNotFound);
        }

        var returnList = new GetGameDto().buildListGetGameDto(game);
        return new SuccessDataResult<>(returnList, Messages.gamesSuccessfullyBrought);
    }

    @Override
    public DataResult<Game> getGameEntityByGameId(String gameId) {
        var game = gameDao.findByGameId(gameId);
        if(game == null) {
            return new ErrorDataResult<>(Messages.gameNotFound);
        }

        return new SuccessDataResult<>(game, Messages.GameSuccessfullyBrought);
    }

    @Override
    public DataResult<Game> getGameEntityByGameCode(String gameCode) {
        var game = gameDao.findByGameCode(gameCode);
        if(game.isEmpty()) {
            return new ErrorDataResult<>(Messages.gameNotFound);
        }

        return new SuccessDataResult<>(game.get(), Messages.GameSuccessfullyBrought);
    }

    @Override
    public DataResult<GetGameStartedDto> startGame(String gameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if(currentPrincipalName == null) {
            return new ErrorDataResult<>(Messages.userNotFound);
        }

        var game = gameDao.findByGameId(gameId);
        if(game == null){
            return new ErrorDataResult<>(Messages.gameNotFound);
        }

        if(!game.getHost().getUsername().equals(currentPrincipalName)){
            return new ErrorDataResult<>(Messages.notAuthorized);
        }

        var firstQuestion = game.getQuestions().stream()
                .filter(q -> q.getSequenceNumber() == 1)
                .findFirst();

        if(firstQuestion.isEmpty()){
            return new ErrorDataResult<>(Messages.questionNotFound);
        }

        game.setStatus(Status.ACTIVE);
        game.setCurrentQuestion(firstQuestion.get());
        game.setStartedAt(new Date());
        gameDao.save(game);

        // Send GAME_STARTED message
        GameMessage startMessage = new GameMessage();
        startMessage.setGameId(gameId);
        startMessage.setPlayerId("");
        startMessage.setSender("System");
        startMessage.setContent("Game has started.");
        startMessage.setTimestamp(System.currentTimeMillis());
        messagingTemplate.convertAndSend("/topic/game/" + gameId, startMessage);

        // Send first question
        sendQuestion(gameId, firstQuestion.get());

        var returnGame = new GetGameStartedDto(game);

        return new SuccessDataResult<>(returnGame, Messages.gameStarted);
    }

    @Override
    public DataResult<List<GetLeaderboardDto>> getLeaderboard(String gameId) {
        var game = gameDao.findByGameId(gameId);
        if(game == null){
            return new ErrorDataResult<>(Messages.gameNotFound);
        }

        var players = game.getPlayers().stream()
                .sorted(Comparator.comparing(Player::getScore).reversed())
                .limit(5)
                .collect(Collectors.toList());

        var returnLeaderboardDto = new GetLeaderboardDto().buildListGetLeaderboardDto(players);

        return new SuccessDataResult<>(returnLeaderboardDto, Messages.leaderboardFound);
    }

    @Override
    public DataResult<Game> getNextQuestion(String gameId) {
        var game = gameDao.findByGameId(gameId);
        if(game == null){
            return new ErrorDataResult<>(Messages.gameNotFound);
        }

        var nextQuestion = game.getQuestions().stream()
                .filter(q -> q.getSequenceNumber() == game.getCurrentQuestion().getSequenceNumber() + 1)
                .findFirst();

        if(nextQuestion.isEmpty()){
            return new ErrorDataResult<>(Messages.questionNotFound);
        }

        game.setCurrentQuestion(nextQuestion.get());
        gameDao.save(game);

        // Send NEXT_QUESTION message
        sendQuestion(gameId, nextQuestion.get());

        return new SuccessDataResult<>(Messages.questionFound);
    }

    @Override
    public DataResult<GetGameStartedDto> getNextQuestionForHost(String gameId) {
        var game = gameDao.findByGameId(gameId);
        if(game == null){
            return new ErrorDataResult<>(Messages.gameNotFound);
        }

        var nextQuestion = game.getQuestions().stream()
                .filter(q -> q.getSequenceNumber() == game.getCurrentQuestion().getSequenceNumber() + 1)
                .findFirst();

        if(nextQuestion.isEmpty()){
            return new ErrorDataResult<>(Messages.questionNotFound);
        }

        game.setCurrentQuestion(nextQuestion.get());
        gameDao.save(game);

        sendQuestion(gameId, nextQuestion.get());
        var returnGame = new GetGameStartedDto(game);
        return new SuccessDataResult<>(returnGame, Messages.GameSuccessfullyBrought);
    }

    @Override
    public DataResult<GetGameStartedDto> getGameStarted(String gameId) {
        var game = gameDao.findByGameId(gameId);
        if(game == null){
            return new ErrorDataResult<>(Messages.gameNotFound);
        }

        var returnGame = new GetGameStartedDto(game);

        return new SuccessDataResult<>(returnGame, Messages.GameSuccessfullyBrought);
    }

    @Override
    public DataResult<GetGameStartedDto> getGameByGameCode(String gameCode) {
        var game = gameDao.findByGameCode(gameCode);
        if(game.isEmpty()) {
            return new ErrorDataResult<>(Messages.gameNotFound);
        }

        var returnGame = new GetGameStartedDto(game.get());
        return new SuccessDataResult<>(returnGame, Messages.GameSuccessfullyBrought);
    }

    @Override
    public DataResult<GetGameIdAndCodeDto> getGameId(String gameCode) {
        var game = gameDao.findByGameCode(gameCode);
        if(game.isEmpty()) {
            return new ErrorDataResult<>(Messages.gameNotFound);
        }

        var returnGame = new GetGameIdAndCodeDto(game.get());
        return new SuccessDataResult<>(returnGame, Messages.GameSuccessfullyBrought);
    }

    @Override
    public Result updateGamePlayerCount(String gameId) {
        var game = gameDao.findByGameId(gameId);
        if(game == null) {
            return new ErrorResult(Messages.gameNotFound);
        }
        game.setCurrentPlayers(game.getCurrentPlayers() + 1);
        gameDao.save(game);
        return new SuccessResult(Messages.gamePlayerCountUpdated);
    }


    private void sendQuestion(String gameId, Question question){
        GameMessage questionMessage = new GameMessage();
        questionMessage.setGameId(gameId);
        questionMessage.setPlayerId("");
        questionMessage.setSender("System");
        questionMessage.setContent(question.getQuestion());
        questionMessage.setTimestamp(System.currentTimeMillis());
        messagingTemplate.convertAndSend("/topic/game/" + gameId , questionMessage);
    }

    private String generateUniqueGameCode() {
        String gameCode;
        do {
            gameCode = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        } while (gameDao.findByGameCode(gameCode).isPresent()); // Check for uniqueness using Optional
        return gameCode;
    }
}
