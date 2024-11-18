package skylab.skyhoot.WebAPI.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import skylab.skyhoot.Business.abstracts.GameService;
import skylab.skyhoot.Business.abstracts.PlayerService;
import skylab.skyhoot.core.result.DataResult;
import skylab.skyhoot.core.result.Result;
import skylab.skyhoot.entities.DTOs.Game.*;
import skylab.skyhoot.entities.DTOs.Player.CreatePlayerDto;
import skylab.skyhoot.entities.DTOs.Player.GetLeaderboardDto;
import skylab.skyhoot.entities.DTOs.Player.GetPlayerDto;
import skylab.skyhoot.entities.Game;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private GameService gameService;
    private PlayerService playerService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public GameController(GameService gameService, PlayerService playerService, SimpMessagingTemplate simpMessagingTemplate) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/addGame")
    public DataResult<String> addGame(@RequestBody CreateGameDto createGameDto){
        return gameService.addGame(createGameDto);
    }

    @PostMapping("/deleteGame")
    public Result deleteGame(@RequestParam int id){
        return gameService.deleteGame(id);
    }

    @PostMapping("/updateGame")
    public Result updateGame(@RequestBody GetGameDto getGameDto){
        return gameService.updateGame(getGameDto);
    }



    @GetMapping("/getGameById")
    public DataResult<GetGameDto> getGameById(@RequestParam String gameId){
        return gameService.getGameByGameId(gameId);
    }

    @GetMapping("/getGamesByHostId")
    public DataResult<List<GetGameDto>> getGamesByHostId(@RequestParam int hostId){
        return gameService.getGamesByHostId(hostId);
    }

    @GetMapping("/getLeaderboard")
    public DataResult<List<GetLeaderboardDto>> getLeaderboard(@RequestParam String gameId){
        return gameService.getLeaderboard(gameId);
    }

    @GetMapping("/getGameEntityByGameId")
    public DataResult<Game> getGameEntityByGameId(@RequestParam String gameId){
        return gameService.getGameEntityByGameId(gameId);
    }

    @GetMapping("/getGameEntityByGameCode")
    public DataResult<Game> getGameEntityByGameCode(@RequestParam String gameCode){
        return gameService.getGameEntityByGameCode(gameCode);
    }

    @GetMapping("/getGameByGameCode")
    public DataResult<GetGameStartedDto> getGameByGameCode(@RequestParam String gameCode){
        return gameService.getGameByGameCode(gameCode);
    }

    @PostMapping("/startGame")
    public Result startGame(@RequestParam String gameId) {
        var result = gameService.startGame(gameId);
        if (result.isSuccess()) {
            simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, result.getData());
        }
        return result;
    }

    @PostMapping("/getNextQuestion")
    public Result getNextQuestion(@RequestParam String gameId) {
        var result = gameService.getNextQuestionForHost(gameId);
        if (result.isSuccess()) {
            simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, result.getData());
        }
        return result;
    }


    @GetMapping("/getGameStarted")
    public DataResult<GetGameStartedDto> getGameStarted(@RequestParam String gameId){
        return gameService.getGameStarted(gameId);
    }

    @MessageMapping("/game/start")
    @SendTo("/topic/game/{gameId}")
    public GameMessage startGame(GameMessage message) {
        Result result = gameService.startGame(message.getGameId());

        if (result.isSuccess()) {
            message.setContent("Game has started.");
        } else {
            message.setContent("Failed to start game: " + result.getMessage());
        }
        return message;
    }

    /*@MessageMapping("/game/connect")
    @SendTo("/topic/game/{gameId}")
    public GameMessage connectPlayer(CreatePlayerDto createPlayerDto, StompHeaderAccessor headerAccessor) {
        // Get IP Address using StompHeaderAccessor
        String ipAddress = headerAccessor.getSessionId(); // or any relevant session information

        // Optionally, you can set the IP address if you have a mechanism to retrieve it
        // createPlayerDto.setIpAddress(ipAddress);

        var result = playerService.addPlayer(createPlayerDto);

        GameMessage message = new GameMessage();
        if (result.isSuccess()) {
            message.setContent("Player " + createPlayerDto.getPlayerName() + " has joined the game.");
        } else {
            message.setContent("Failed to join game: " + result.getMessage());
        }
        return message;
    }
*/

    @PostMapping("/connectPlayer")
    public ResponseEntity<GameMessage> connectPlayer(@RequestBody CreatePlayerDto createPlayerDto, HttpServletRequest request) {
        // Extract IP address logic
        String ipAddress = request.getRemoteAddr();
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            ipAddress = forwardedFor.split(",")[0];
        }

        createPlayerDto.setIpAddress(ipAddress);
        var result = playerService.addPlayer(createPlayerDto);


        GameMessage message = new GameMessage();
        if (result.isSuccess()) {
            message.setContent("Player " + createPlayerDto.getPlayerName() + " has joined the game.");

            var getGameId = gameService.getGameId(createPlayerDto.getGameCode());
            message.setGameId(getGameId.getData().getGameId());
            message.setPlayerId(result.getData().getPlayerId());
            message.setTimestamp(System.currentTimeMillis());
            message.setSender("System");
            if(!getGameId.isSuccess()){
                message.setContent("Failed to join game: " + getGameId.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message); // Return failure response with 400
            }


            System.out.println("Broadcasting WebSocket message: to " + getGameId.getData().getGameId() + " " + message);
            // Broadcast the new player join event to WebSocket subscribers
            simpMessagingTemplate.convertAndSend("/topic/game/" + getGameId.getData().getGameId(), message);

            return ResponseEntity.ok(message); // Return success response
        } else {
            message.setContent("Failed to join game: " + result.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message); // Return failure response with 400
        }
    }


    @GetMapping("/getGameId")
    public DataResult<GetGameIdAndCodeDto> getGameId(@RequestParam String gameCode){
        return gameService.getGameId(gameCode);
    }

    @PostMapping("/endGame")
    public Result endGame(@RequestParam String gameId) {
        return gameService.endGame(gameId);
    }




}
