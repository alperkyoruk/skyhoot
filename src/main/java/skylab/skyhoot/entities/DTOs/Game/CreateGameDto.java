package skylab.skyhoot.entities.DTOs.Game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateGameDto {
    private int maxPlayers;
    private List<Integer> questionIds;
}
