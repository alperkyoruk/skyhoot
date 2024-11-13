package skylab.skyhoot.WebAPI.controllers;

import org.springframework.web.bind.annotation.*;
import skylab.skyhoot.Business.abstracts.UserService;
import skylab.skyhoot.core.result.DataResult;
import skylab.skyhoot.core.result.Result;
import skylab.skyhoot.entities.DTOs.User.CreateUserDto;
import skylab.skyhoot.entities.DTOs.User.GetUserDto;
import skylab.skyhoot.entities.User;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @PostMapping("/addUser")
    public Result addUser(@RequestBody CreateUserDto createUserDto){
        return userService.addUser(createUserDto);
    }

    @PostMapping("/deleteUser")
    public Result deleteUser(@RequestParam int userId){
        return userService.deleteUser(userId);
    }

    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody GetUserDto getUserDto){
        return userService.updateUser(getUserDto);
    }

    @GetMapping("/getUserById")
    public DataResult<GetUserDto> getUserById(@RequestParam int userId){
        return userService.getUserById(userId);
    }

    @GetMapping("/getUserByUsername")
    public DataResult<GetUserDto> getUserByUsername(@RequestParam String username){
        return userService.getUserByUsername(username);
    }

    @GetMapping("/getUsers")
    public DataResult<List<GetUserDto>> getUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/addModerator")
    public Result addModerator(@RequestParam int userId){
        return userService.addModerator(userId);
    }

    @PostMapping("/removeModerator")
    public Result removeModerator(@RequestParam int userId){
        return userService.removeModerator(userId);
    }
}
