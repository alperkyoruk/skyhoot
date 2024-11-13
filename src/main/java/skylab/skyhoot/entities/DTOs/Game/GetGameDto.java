package skylab.skyhoot.entities.DTOs.Game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import skylab.skyhoot.entities.DTOs.Player.GetPlayerDto;
import skylab.skyhoot.entities.DTOs.Question.GetQuestionDto;
import skylab.skyhoot.entities.Game;
import skylab.skyhoot.entities.Player;
import skylab.skyhoot.entities.Status;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetGameDto {
    private int id;
    private String gameCode;
    private String gameId;
    private int maxPlayers;
    private int currentPlayers;
    private int questionCount;
    private Status status;
    private Date CreatedAt;
    private Date StartedAt;
    private List<GetQuestionDto> questions;
    private List<GetPlayerDto> players;

    public GetGameDto(Game game){
        this.id = game.getId();
        this.maxPlayers = game.getMaxPlayers();
        this.currentPlayers = game.getCurrentPlayers();
        this.questionCount = game.getQuestionCount();
        this.CreatedAt = game.getCreatedAt();
        this.StartedAt = game.getStartedAt();
        this.questions = new GetQuestionDto().buildListGetQuestionDto(game.getQuestions());
        this.players = new GetPlayerDto().buildListGetPlayerDto(game.getPlayers());
        this.status = game.getStatus();
        this.gameCode = game.getGameCode();
        this.gameId = game.getGameId();
    }

    public List<GetGameDto> buildListGetGameDto(List<Game> games) {
        return games.stream()
                .map(GetGameDto::new)
                .collect(Collectors.toList());
    }


}
