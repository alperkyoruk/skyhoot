package skylab.skyhoot.Business.abstracts;


import skylab.skyhoot.core.result.DataResult;
import skylab.skyhoot.core.result.Result;
import skylab.skyhoot.entities.DTOs.Player.CreatePlayerDto;
import skylab.skyhoot.entities.DTOs.Player.GetPlayerDto;
import skylab.skyhoot.entities.DTOs.Player.GetPlayerIdGameIdDto;
import skylab.skyhoot.entities.Player;

import java.util.List;

public interface PlayerService {
    DataResult<GetPlayerIdGameIdDto> addPlayer(CreatePlayerDto createPlayerDto);

    Result deletePlayer(int playerId);

    DataResult<GetPlayerDto> getPlayerById(int playerId);

    DataResult<List<GetPlayerDto>> getPlayersByGameId(String gameId);

    DataResult<List<GetPlayerDto>> getPlayersByIpAddress(String ipAddress);

    DataResult<Player> getPlayerEntityById(int id);

    Result updatePlayerScore(String playerId, int score);

    DataResult<Player> getPlayerEntityByPlayerId(String playerId);

}
