package skylab.skyhoot.WebAPI.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import skylab.skyhoot.Business.abstracts.PlayerAnswerService;
import skylab.skyhoot.core.result.Result;
import skylab.skyhoot.entities.DTOs.PlayerAnswer.CreatePlayerAnswerDto;

@RestController
@RequestMapping("/api/playerAnswers")
public class PlayerAnswerController {
    private PlayerAnswerService playerAnswerService;
    private final SimpMessagingTemplate messagingTemplate;

    public PlayerAnswerController(PlayerAnswerService playerAnswerService, SimpMessagingTemplate simpMessagingTemplate) {
        this.playerAnswerService = playerAnswerService;
        this.messagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/addPlayerAnswer")

    public Result addPlayerAnswer(@RequestBody CreatePlayerAnswerDto createPlayerAnswerDto) {
        var result = playerAnswerService.addPlayerAnswer(createPlayerAnswerDto);

        return result;
    }

    @PostMapping("/deletePlayerAnswer")
    public Result deletePlayerAnswer(@RequestBody int playerAnswerId){
        return playerAnswerService.deletePlayerAnswer(playerAnswerId);
    }

    @GetMapping("/getPlayerAnswerById")
    public Result getPlayerAnswerById(@RequestBody int playerAnswerId){
        return playerAnswerService.getPlayerAnswerById(playerAnswerId);
    }

    @GetMapping("/getPlayerAnswersByPlayerId")
    public Result getPlayerAnswersByPlayerId(@RequestBody int playerId){
        return playerAnswerService.getPlayerAnswersByPlayerId(playerId);
    }

    @GetMapping("/getPlayerAnswersByQuestionId")
    public Result getPlayerAnswersByQuestionId(@RequestBody int questionId){
        return playerAnswerService.getPlayerAnswersByQuestionId(questionId);
    }

    @PostMapping("/validatePlayerAnswer")
    public Result validatePlayerAnswer(@RequestBody int playerAnswerId){
        return playerAnswerService.validatePlayerAnswer(playerAnswerId);
    }

}


