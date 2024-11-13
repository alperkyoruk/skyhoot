package skylab.skyhoot.entities.DTOs.Player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import skylab.skyhoot.entities.Player;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetPlayerIdGameIdDto {
    private String playerId;
    private String gameId;

    public GetPlayerIdGameIdDto(Player player) {
        this.playerId = player.getPlayerId();
        this.gameId = player.getGame().getGameId();
    }




}
