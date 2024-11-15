package skylab.skyhoot.WebAPI.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import skylab.skyhoot.Business.abstracts.PlayerService;
import skylab.skyhoot.core.result.DataResult;
import skylab.skyhoot.core.result.Result;
import skylab.skyhoot.entities.DTOs.Player.CreatePlayerDto;
import skylab.skyhoot.entities.DTOs.Player.GetPlayerDto;
import skylab.skyhoot.entities.Player;

import java.util.List;

@RestController
@RequestMapping("api/players")
public class PlayerController {
    private PlayerService playerService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public PlayerController(PlayerService playerService, SimpMessagingTemplate simpMessagingTemplate) {
        this.playerService = playerService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/addPlayer")
    public Result addPlayer(@RequestBody CreatePlayerDto createPlayerDto, HttpServletRequest request){
        String ipAddress = request.getRemoteAddr();

        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            ipAddress = forwardedFor.split(",")[0];
        }
        createPlayerDto.setIpAddress(ipAddress);
        return playerService.addPlayer(createPlayerDto);
    }

    @PostMapping("/deletePlayer")
    public Result deletePlayer(@RequestBody int playerId){
        return playerService.deletePlayer(playerId);
    }

    @GetMapping("/getPlayerById")
    public DataResult<GetPlayerDto> getPlayerById(@RequestBody int playerId){
        return playerService.getPlayerById(playerId);
    }

    @GetMapping("/getPlayersByGameId")
    public DataResult<List<GetPlayerDto>> getPlayersByGameId(@RequestBody String gameId){
        return playerService.getPlayersByGameId(gameId);
    }

    @GetMapping("/getPlayersByIpAddress")
    public DataResult<List<GetPlayerDto>> getPlayersByIpAddress(HttpServletRequest request){
        String ipAddress = request.getRemoteAddr();

        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            ipAddress = forwardedFor.split(",")[0];
        }

        return playerService.getPlayersByIpAddress(ipAddress);
    }

    @GetMapping("/getPlayerEntityByPlayerId")
    public DataResult<Player> getPlayerEntityByPlayerId(@RequestBody int playerId){
        return playerService.getPlayerEntityById(playerId);
    }





}
