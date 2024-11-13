package skylab.skyhoot.entities.DTOs.Game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import skylab.skyhoot.entities.DTOs.Question.GetQuestionDto;
import skylab.skyhoot.entities.Game;
import skylab.skyhoot.entities.Question;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetGameStartedDto {
    private int id;
    private String gameCode;
    private String gameId;
    private int questionCount;
    private GetQuestionDto currentQuestion;

    public GetGameStartedDto(Game game){
        this.id = game.getId();
        this.gameCode = game.getGameCode();
        this.gameId = game.getGameId();
        this.currentQuestion = new GetQuestionDto(game.getCurrentQuestion());
        this.questionCount = game.getQuestionCount();
    }

    public List<GetGameStartedDto> buildListGetGameStartedDto(List<Game> games) {
        return games.stream()
                .map(GetGameStartedDto::new)
                .collect(Collectors.toList());

    }
}
