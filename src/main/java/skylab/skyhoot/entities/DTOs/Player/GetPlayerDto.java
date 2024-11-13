package skylab.skyhoot.entities.DTOs.Player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import skylab.skyhoot.entities.DTOs.Game.GetGameDto;
import skylab.skyhoot.entities.Player;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetPlayerDto {
    private int id;
    private String playerName;
    private int score;
    private GetGameDto game;
    private String ipAddress;
    private Date joinedAt;

    public GetPlayerDto(Player player){
        this.id = player.getId();
        this.playerName = player.getPlayerName();
        this.score = player.getScore();
        this.game = new GetGameDto(player.getGame());
        this.ipAddress = player.getIpAddress();
        this.joinedAt = player.getJoinedAt();
    }

    public List<GetPlayerDto> buildListGetPlayerDto(List<Player> players) {
        return players.stream()
                .map(GetPlayerDto::new)
                .collect(Collectors.toList());
    }


}
