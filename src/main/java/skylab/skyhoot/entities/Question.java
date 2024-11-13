package skylab.skyhoot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "question")
    private String question;

    @Column(name = "sequence_number")
    private int sequenceNumber;

    @OneToMany(targetEntity = AnswerOption.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private List<AnswerOption> answerOptions;

    @ManyToOne(targetEntity = Game.class, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "time_limit", nullable = false)
    private int timeLimit;

    @Column(name = "score", nullable = false)
    private int score;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "created_by")
    private User createdBy;

}
