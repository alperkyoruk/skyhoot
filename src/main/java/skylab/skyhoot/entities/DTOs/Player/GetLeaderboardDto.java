package skylab.skyhoot.entities.DTOs.Player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import skylab.skyhoot.entities.Player;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetLeaderboardDto{
    private String playerName;
    private int score;

    public GetLeaderboardDto(Player player){
        this.playerName = player.getPlayerName();
        this.score = player.getScore();
    }

    public List<GetLeaderboardDto> buildListGetLeaderboardDto(List<Player> players) {
        return players.stream()
                .map(GetLeaderboardDto::new)
                .collect(Collectors.toList());
    }
}
