package skylab.skyhoot.Business.abstracts;

import skylab.skyhoot.core.result.DataResult;
import skylab.skyhoot.core.result.Result;
import skylab.skyhoot.entities.DTOs.Game.CreateGameDto;
import skylab.skyhoot.entities.DTOs.Game.GetGameDto;
import skylab.skyhoot.entities.DTOs.Game.GetGameIdAndCodeDto;
import skylab.skyhoot.entities.DTOs.Game.GetGameStartedDto;
import skylab.skyhoot.entities.DTOs.Player.GetLeaderboardDto;
import skylab.skyhoot.entities.DTOs.Player.GetPlayerDto;
import skylab.skyhoot.entities.Game;

import java.util.List;

public interface GameService {

    DataResult<String> addGame(CreateGameDto createGameDto);

    Result deleteGame(int id);

    Result updateGame(GetGameDto getGameDto);

    Result joinGame(String gameId, int playerId);

    Result leaveGame(String gameId, int playerId);

    DataResult<GetGameDto> getGameByGameId(String gameId);


    DataResult<List<GetGameDto>> getGamesByHostId(int hostId);

    DataResult<Game> getGameEntityByGameId(String gameId);

    DataResult<Game> getGameEntityByGameCode(String gameCode);

    DataResult<GetGameStartedDto> startGame(String gameId);

    //get leaderboard of that game
    DataResult<List<GetLeaderboardDto>> getLeaderboard(String gameId);

    DataResult<Game> getNextQuestion(String gameId);

    DataResult<GetGameStartedDto> getNextQuestionForHost(String gameId);

    DataResult<GetGameStartedDto> getGameStarted(String gameId);

    DataResult<GetGameStartedDto> getGameByGameCode(String gameCode);

    DataResult<GetGameIdAndCodeDto> getGameId(String gameCode);

    Result updateGamePlayerCount(String gameId);



}
