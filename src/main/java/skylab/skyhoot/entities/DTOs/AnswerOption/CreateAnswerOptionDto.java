package skylab.skyhoot.entities.DTOs.AnswerOption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateAnswerOptionDto {
    private String option;
    private int isCorrect;
    private int questionId;
}
