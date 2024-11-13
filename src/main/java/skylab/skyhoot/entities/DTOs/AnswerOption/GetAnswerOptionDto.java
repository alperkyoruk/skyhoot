package skylab.skyhoot.entities.DTOs.AnswerOption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import skylab.skyhoot.entities.AnswerOption;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAnswerOptionDto {
    private int id;
    private String option;
    private boolean isCorrect;
    private int questionId;

    public GetAnswerOptionDto(AnswerOption answerOption) {
        this.id = answerOption.getId();
        this.option = answerOption.getOption();
        this.questionId = answerOption.getQuestion().getId();
        this.isCorrect = answerOption.isCorrect();
    }



    public List<GetAnswerOptionDto> buildListGetAnswerOptionDto(List<AnswerOption> answerOptions) {
        return answerOptions.stream()
                .map(GetAnswerOptionDto::new)
                .collect(Collectors.toList());
    }
}
