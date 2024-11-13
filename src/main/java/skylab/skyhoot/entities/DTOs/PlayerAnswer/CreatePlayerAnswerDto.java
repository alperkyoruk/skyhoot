package skylab.skyhoot.entities.DTOs.PlayerAnswer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreatePlayerAnswerDto {
    private String playerId;
    private String gameId;
    private int questionId;
    private int answerOptionId;
    private double timeTaken;
}
