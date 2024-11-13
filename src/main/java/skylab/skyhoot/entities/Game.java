package skylab.skyhoot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "game_code")
    private String gameCode;

    @Column(name = "game_id")
    private String gameId;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User host;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "started_at", nullable = true)
    private Date startedAt;

    @Column(name = "ended_at", nullable = true)
    private Date endedAt;

    @Column(name = "max_players")
    private int maxPlayers;

    @Column(name = "current_players")
    private int currentPlayers;

    @Column(name = "question_count")
    private int questionCount;

    @OneToMany(targetEntity = Player.class, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private List<Player> players;

    @OneToMany(targetEntity = Question.class, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "game_id")
    private List<Question> questions;

    @ManyToOne(targetEntity = Question.class, cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "current_question_id")
    private Question currentQuestion;



}
