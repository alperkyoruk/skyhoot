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
public class GetAnswerOptionForQuestionDto {
    private int id;
    private String option;
    private int questionId;

    public GetAnswerOptionForQuestionDto(AnswerOption answerOption) {
        this.id = answerOption.getId();
        this.option = answerOption.getOption();
        this.questionId = answerOption.getQuestion().getId();
    }



    public List<GetAnswerOptionForQuestionDto> buildListGetAnswerOptionForQuestionDto(List<AnswerOption> answerOptions) {
        return answerOptions.stream()
                .map(GetAnswerOptionForQuestionDto::new)
                .collect(Collectors.toList());
    }
}
