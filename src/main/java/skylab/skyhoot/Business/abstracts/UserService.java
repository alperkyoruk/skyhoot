package skylab.skyhoot.Business.abstracts;

import org.springframework.security.core.userdetails.UserDetailsService;
import skylab.skyhoot.core.result.DataResult;
import skylab.skyhoot.core.result.Result;
import skylab.skyhoot.entities.DTOs.User.CreateUserDto;
import skylab.skyhoot.entities.DTOs.User.GetUserDto;
import skylab.skyhoot.entities.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    Result addUser(CreateUserDto createUserDto);

    Result deleteUser(int userId);

    Result updateUser(GetUserDto getUserDto);

    DataResult<GetUserDto> getUserById(int userId);

    DataResult<GetUserDto> getUserByEmail(String email);

    DataResult<GetUserDto> getUserByUsername(String username);

    DataResult<User> getUserEntityByUsername(String username);

    DataResult<List<GetUserDto>> getAllUsers();

    Result addModerator(int userId);

    Result removeModerator(int userId);

    Result addVip(int userId);

    Result removeVip(int userId);


}
