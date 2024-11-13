package skylab.skyhoot.entities.DTOs.Question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateQuestionDto {
    private String question;
    private List<Integer> answerOptionIds;
    private int timeLimit;
    private int sequenceNumber;
    private int score;
}
