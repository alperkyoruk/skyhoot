package skylab.skyhoot.entities.DTOs.Player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreatePlayerDto {
    private String playerName;
    private String gameCode;
    private String ipAddress;
}
