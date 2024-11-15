package skylab.skyhoot.Business.concretes;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import skylab.skyhoot.Business.abstracts.UserService;
import skylab.skyhoot.Business.constants.Messages;
import skylab.skyhoot.core.result.*;
import skylab.skyhoot.dataAccess.UserDao;
import skylab.skyhoot.entities.DTOs.User.CreateUserDto;
import skylab.skyhoot.entities.DTOs.User.GetUserDto;
import skylab.skyhoot.entities.User;
import skylab.skyhoot.entities.Role;

import java.util.List;
import java.util.Set;

@Service
public class UserManager implements UserService {

    private UserDao userDao;
    private BCryptPasswordEncoder passwordEncoder;

    public UserManager(UserDao userDao, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Result addUser(CreateUserDto createUserDto) {

        if(createUserDto.getUsername() == null || createUserDto.getEmail() == null || createUserDto.getPassword() == null){
            return new SuccessResult(Messages.userCouldNotBeAdded);
        }

        if(userDao.existsByUsername(createUserDto.getUsername())){
            return new SuccessResult(Messages.usernameAlreadyExists);
        }
        if(userDao.existsByEmail(createUserDto.getEmail())){
            return new SuccessResult(Messages.emailAlreadyExists);
        }


        User user = User.builder()
                .username(createUserDto.getUsername())
                .email(createUserDto.getEmail())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .authorities(Set.of(Role.ROLE_USER))
                .build();

        userDao.save(user);
        return new SuccessResult(Messages.userAdded);
    }

    @Override
    public Result deleteUser(int userId) {
        var user = userDao.findById(userId);
        if(user == null){
            return new SuccessResult(Messages.userNotFound);
        }
        userDao.delete(user);
        return new SuccessResult(Messages.userDeleted);
    }

    @Override
    public Result updateUser(GetUserDto getUserDto) {
        var user = userDao.findById(getUserDto.getId());
        if(user == null){
            return new SuccessResult(Messages.userNotFound);
        }

        user.setUsername(getUserDto.getUsername() == null ? user.getUsername() : getUserDto.getUsername());
        user.setEmail(getUserDto.getEmail() == null ? user.getEmail() : getUserDto.getEmail());
        userDao.save(user);
        return new SuccessResult(Messages.userUpdated);
    }

    @Override
    public DataResult<GetUserDto> getUserById(int userId) {
        var user = userDao.findById(userId);
        if(user == null){
            return new ErrorDataResult<>(Messages.userNotFound);
        }
        var returnUser = new GetUserDto(user);
        return new SuccessDataResult<>(returnUser, Messages.userSuccessfullyBrought);
    }

    @Override
    public DataResult<GetUserDto> getUserByEmail(String email) {
        var user = userDao.findByEmail(email);
        if(user == null){
            return new ErrorDataResult<>(Messages.userNotFound);
        }
        var returnUser = new GetUserDto(user);
        return new SuccessDataResult<>(returnUser, Messages.userSuccessfullyBrought);
    }

    @Override
    public DataResult<GetUserDto> getUserByUsername(String username) {
        var user = userDao.findByUsername(username);
        if(user == null){
            return new ErrorDataResult<>(Messages.userNotFound);
        }
        var returnUser = new GetUserDto(user);
        return new SuccessDataResult<>(returnUser, Messages.userSuccessfullyBrought);
    }

    @Override
    public DataResult<User> getUserEntityByUsername(String username) {
        var user = userDao.findByUsername(username);
        if(user == null){
            return new ErrorDataResult<>(Messages.userNotFound);
        }
        return new SuccessDataResult<>(user, Messages.userSuccessfullyBrought);
    }

    @Override
    public DataResult<List<GetUserDto>> getAllUsers() {
        var users = userDao.findAll();
        if(users.isEmpty()){
            return new ErrorDataResult<>(Messages.usersNotFound);
        }
        var returnUsers = new GetUserDto().buildListGetUserDto(users);
        return new SuccessDataResult<>(returnUsers, Messages.usersFound);
    }

    @Override
    public Result addModerator(int userId) {
        var userResponse = userDao.findById(userId);
        if(userResponse == null){
            return new ErrorResult(Messages.UserNotFound);
        }

        userResponse.addRole(Role.ROLE_MODERATOR);
        userDao.save(userResponse);
        return new SuccessResult(Messages.ModeratorAddedSuccessfully);
    }

    @Override
    public Result removeModerator(int userId) {
        var userResponse = userDao.findById(userId);
        if(userResponse == null){
            return new ErrorResult(Messages.UserNotFound);
        }

        userResponse.getAuthorities().remove(Role.ROLE_MODERATOR);
        return new SuccessResult(Messages.ModeratorRemovedSuccessfully);
    }

    @Override
    public Result addVip(int userId) {
        var user = userDao.findById(userId);
        if(user == null){
            return new ErrorResult(Messages.UserNotFound);
        }

        user.addRole(Role.ROLE_VIP);
        userDao.save(user);
        return new SuccessResult(Messages.VipAddedSuccessfully);
    }

    @Override
    public Result removeVip(int userId) {
        var user = userDao.findById(userId);
        if(user == null){
            return new ErrorResult(Messages.UserNotFound);
        }

        user.getAuthorities().remove(Role.ROLE_VIP);
        return new SuccessResult(Messages.VipRemovedSuccessfully);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = getUserEntityByUsername(username).getData();
        return user;
    }
}

