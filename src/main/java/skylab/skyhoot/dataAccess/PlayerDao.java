package skylab.skyhoot.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;
import skylab.skyhoot.entities.Player;

import java.util.List;

public interface PlayerDao extends JpaRepository<Player, Integer> {

    Player findByPlayerId(String playerId);

    Player findById(int id);

    List<Player> findAllByGame_GameId(String gameId);

    List<Player> findAllByIpAddress(String ipAddress);

}
