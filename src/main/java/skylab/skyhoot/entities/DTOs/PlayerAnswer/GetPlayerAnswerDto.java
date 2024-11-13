package skylab.skyhoot.entities.DTOs.PlayerAnswer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import skylab.skyhoot.entities.PlayerAnswer;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetPlayerAnswerDto {
    private int id;
    private int playerId;
    private int questionId;
    private int answerOptionId;
    private double timeTaken;

    public GetPlayerAnswerDto(PlayerAnswer playerAnswer) {
        this.id = playerAnswer.getId();
        this.playerId = playerAnswer.getPlayer().getId();
        this.questionId = playerAnswer.getQuestion().getId();
        this.answerOptionId = playerAnswer.getAnswerOption().getId();
        this.timeTaken = playerAnswer.getTimeTaken();
    }

    public List<GetPlayerAnswerDto> buildListGetPlayerAnswerDto(List<PlayerAnswer> playerAnswers) {
        return playerAnswers.stream()
                .map(GetPlayerAnswerDto::new)
                .collect(Collectors.toList());
    }
}
