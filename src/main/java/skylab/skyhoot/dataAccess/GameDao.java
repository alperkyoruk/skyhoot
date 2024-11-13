package skylab.skyhoot.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;
import skylab.skyhoot.entities.Game;
import skylab.skyhoot.entities.Status;

import java.util.List;
import java.util.Date;
import java.util.Optional;

public interface GameDao extends JpaRepository<Game, Integer> {

    // Find a game by its game code
    Optional<Game> findByGameCode(String gameCode);

    // Find a game by its game ID
    Game findByGameId(String gameId);

    // Find all games by a specific host user ID
    List<Game> findAllByHostIdOrderByStatus(int hostId);

    // Find all games by their status
    List<Game> findAllByStatus(Status status);

    // Find all games created after a specific date
    List<Game> findAllByCreatedAtAfter(Date date);

    // Find all games that have a specific number of current players
    List<Game> findAllByCurrentPlayers(int currentPlayers);



}

