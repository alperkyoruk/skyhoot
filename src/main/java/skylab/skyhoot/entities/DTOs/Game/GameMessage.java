package skylab.skyhoot.entities.DTOs.Game;

import lombok.Data;

@Data
public class GameMessage {
    private String gameId;
    private String playerId;
    private String sender;
    private String content;
    private long timestamp;
}
