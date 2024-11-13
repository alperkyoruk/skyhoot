package skylab.skyhoot.entities.DTOs.Game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import skylab.skyhoot.entities.Game;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetGameIdAndCodeDto {
    private String gameId;
    private String gameCode;

    public GetGameIdAndCodeDto(Game game) {
        this.gameId = game.getGameId();
        this.gameCode = game.getGameCode();
    }

}
