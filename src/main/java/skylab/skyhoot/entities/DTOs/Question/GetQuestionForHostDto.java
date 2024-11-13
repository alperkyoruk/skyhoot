package skylab.skyhoot.entities.DTOs.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import skylab.skyhoot.entities.DTOs.AnswerOption.GetAnswerOptionDto;
import skylab.skyhoot.entities.DTOs.AnswerOption.GetAnswerOptionForQuestionDto;
import skylab.skyhoot.entities.DTOs.Game.GetGameDto;
import skylab.skyhoot.entities.DTOs.User.GetUserDto;
import skylab.skyhoot.entities.Question;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetQuestionForHostDto {
    private int id;
    private String question;
    private int timeLimit;
    private List<GetAnswerOptionDto> answerOptions;
    private int sequenceNumber;
    private int score;
    private GetUserDto createdBy;

    public GetQuestionForHostDto(Question question){
        this.id = question.getId();
        this.question = question.getQuestion();
        this.answerOptions = new GetAnswerOptionDto().buildListGetAnswerOptionDto(question.getAnswerOptions());
        this.timeLimit = question.getTimeLimit();
        this.score = question.getScore();
        this.sequenceNumber = question.getSequenceNumber();
        this.createdBy = new GetUserDto(question.getCreatedBy());
    }

    public List<GetQuestionForHostDto> buildListGetQuestionForHostDto(List<Question> questions) {
        return questions.stream()
                .map(GetQuestionForHostDto::new)
                .collect(Collectors.toList());
    }

}
