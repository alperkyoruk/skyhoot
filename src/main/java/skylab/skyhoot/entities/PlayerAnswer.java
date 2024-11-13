package skylab.skyhoot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "player_answers")
public class PlayerAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = Player.class, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(targetEntity = Game.class, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(targetEntity = Question.class, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(targetEntity = AnswerOption.class, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_option_id")
    private AnswerOption answerOption;

    @Column(name = "answered_at", nullable = false)
    private Timestamp answeredAt;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "time_taken", nullable = false)
    private double timeTaken;




}
