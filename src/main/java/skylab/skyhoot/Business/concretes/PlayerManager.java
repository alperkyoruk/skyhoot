package skylab.skyhoot.Business.concretes;

import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import skylab.skyhoot.Business.abstracts.GameService;
import skylab.skyhoot.Business.abstracts.PlayerService;
import skylab.skyhoot.Business.constants.Messages;
import skylab.skyhoot.core.result.*;
import skylab.skyhoot.dataAccess.PlayerDao;
import skylab.skyhoot.entities.DTOs.Game.GameMessage;
import skylab.skyhoot.entities.DTOs.Player.CreatePlayerDto;
import skylab.skyhoot.entities.DTOs.Player.GetPlayerDto;
import skylab.skyhoot.entities.DTOs.Player.GetPlayerIdGameIdDto;
import skylab.skyhoot.entities.Player;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PlayerManager implements PlayerService {

    private PlayerDao playerDao;
    private GameService gameService;
    private SimpMessagingTemplate messagingTemplate;

    public PlayerManager(PlayerDao playerDao, @Lazy GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.playerDao = playerDao;
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public DataResult<GetPlayerIdGameIdDto> addPlayer(CreatePlayerDto createPlayerDto) {
        var game = gameService.getGameEntityByGameCode(createPlayerDto.getGameCode()).getData();
        if(game == null){
            return new ErrorDataResult<>(Messages.gameNotFound);
        }
        Player player = Player.builder()
                .game(game)
                .ipAddress(createPlayerDto.getIpAddress())
                .score(0)
                .playerName(createPlayerDto.getPlayerName())
                .playerId(UUID.randomUUID().toString())
                .joinedAt(new Date())
                .build();
        playerDao.save(player);

        var returnPlayer = new GetPlayerIdGameIdDto(player.getPlayerId(), game.getGameId());

        return new SuccessDataResult<>(returnPlayer,Messages.playerAdded);
    }

    @Override
    public Result deletePlayer(int playerId) {
        var player = playerDao.findById(playerId);
        if(player == null){
            return new ErrorResult(Messages.playerNotFound);
        }
        GameMessage gameMessage = new GameMessage();
        gameMessage.setGameId(player.getGame().getGameId());
        gameMessage.setPlayerId(player.getPlayerId());
        gameMessage.setTimestamp(0);
        gameMessage.setSender("server");
        gameMessage.setContent("Player " + player.getPlayerName() + " got kicked from the game.");

        messagingTemplate.convertAndSend("/topic/game/" + player.getGame().getGameId() , gameMessage);
        playerDao.delete(player);


        return new SuccessResult(Messages.playerDeleted);
    }


    @Override
    public DataResult<GetPlayerDto> getPlayerById(int playerId) {
        var player = playerDao.findById(playerId);
        if(player == null){
            return new ErrorDataResult<>(Messages.playerNotFound);
        }
        var returnPlayer = new GetPlayerDto(player);
        return new SuccessDataResult<>(returnPlayer, Messages.playerFound);
    }

    @Override
    public DataResult<List<GetPlayerDto>> getPlayersByGameId(String gameId) {
        var players = playerDao.findAllByGame_GameId(gameId);
        if(players == null){
            return new ErrorDataResult<>(Messages.playerNotFound);
        }
        var returnPlayers = new GetPlayerDto().buildListGetPlayerDto(players);
        return new SuccessDataResult<>(returnPlayers, Messages.playerFound);
    }

    @Override
    public DataResult<List<GetPlayerDto>> getPlayersByIpAddress(String ipAddress) {
        var players = playerDao.findAllByIpAddress(ipAddress);
        if(players == null){
            return new ErrorDataResult<>(Messages.playerNotFound);
        }
        var returnPlayers = new GetPlayerDto().buildListGetPlayerDto(players);
        return new SuccessDataResult<>(returnPlayers, Messages.playerFound);
    }

    @Override
    public DataResult<Player> getPlayerEntityById(int id) {
        var player = playerDao.findById(id);
        if(player == null){
            return new ErrorDataResult<>(Messages.playerNotFound);
        }
        return new SuccessDataResult<>(player, Messages.playerFound);
    }

    @Override
    public Result updatePlayerScore(String playerId, int score) {
        var player = playerDao.findByPlayerId(playerId);
        if(player == null){
            return new ErrorResult(Messages.playerNotFound);
        }
        player.setScore(score);
        playerDao.save(player);
        return new SuccessResult(Messages.playerScoreUpdated);
    }

    @Override
    public DataResult<Player> getPlayerEntityByPlayerId(String playerId) {
        var player = playerDao.findByPlayerId(playerId);
        if(player == null){
            return new ErrorDataResult<>(Messages.playerNotFound);
        }
        return new SuccessDataResult<>(player, Messages.playerFound);
    }

    @Override
    public DataResult<List<GetPlayerDto>> getPlayersByGameIdAndPlayerName(String gameId, String playerName) {
        var players = playerDao.findAllByGame_GameIdAndPlayerName(gameId, playerName);
        if(players == null){
            return new ErrorDataResult<>(Messages.playerNotFound);
        }

        var returnPlayers = new GetPlayerDto().buildListGetPlayerDto(players);
        return new SuccessDataResult<>(returnPlayers, Messages.playerFound);
    }

    @Override
    public Result kickPlayerByPlayerName(String gameId, String playerName) {
        var players = playerDao.findAllByGame_GameIdAndPlayerName(gameId, playerName);
        if(players == null){
            return new ErrorResult(Messages.playerNotFound);
        }
        for (Player player : players) {
            GameMessage gameMessage = new GameMessage();
            gameMessage.setGameId(player.getGame().getGameId());
            gameMessage.setPlayerId(player.getPlayerId());
            gameMessage.setTimestamp(0);
            gameMessage.setSender("server");
            gameMessage.setContent("Player " + player.getPlayerName() + " got kicked from the game.");

            messagingTemplate.convertAndSend("/topic/game/" + player.getGame().getGameId() , gameMessage);
            playerDao.delete(player);
        }
        return new SuccessResult(Messages.playerDeleted);
    }
}
