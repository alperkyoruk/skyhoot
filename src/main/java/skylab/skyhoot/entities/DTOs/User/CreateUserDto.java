package skylab.skyhoot.entities.DTOs.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import skylab.skyhoot.entities.Role;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateUserDto {
    private String username;
    private String password;
    private String email;
    private Role role;

}
