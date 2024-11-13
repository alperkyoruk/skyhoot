package skylab.skyhoot.entities.DTOs.User;

import skylab.skyhoot.entities.Role;
import skylab.skyhoot.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetUserDto {
    private int id;
    private String username;
    private String email;
    private Set<Role> authorities;

    public GetUserDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.authorities = user.getAuthorities();
    }

    public List<GetUserDto> buildListGetUserDto(List<User> users){
        return users.stream()
                .map(GetUserDto::new)
                .collect(Collectors.toList());
    }
}

