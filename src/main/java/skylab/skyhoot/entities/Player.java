package skylab.skyhoot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "player_id")
    private String playerId;

    @ManyToOne(targetEntity = Game.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "player_name")
    private String playerName;

    @Column(name = "score")
    private int score;

    @Column(name = "joined_at", nullable = false)
    private Date joinedAt;

    @Column(name = "ip_address")
    private String ipAddress;

    @OneToMany(targetEntity = PlayerAnswer.class, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "player_id")
    private List<PlayerAnswer> playerAnswers;


}
