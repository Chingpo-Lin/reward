package com.chuwa.reward.service.impl;

import com.chuwa.reward.dao.UserRepository;
import com.chuwa.reward.entity.User;
import com.chuwa.reward.enums.BizCodeEnum;
import com.chuwa.reward.exception.BizException;
import com.chuwa.reward.payload.UserDto;
import com.chuwa.reward.payload.UserResponse;
import com.chuwa.reward.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * user service implementation
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * create a user
     * @param userDto
     * @return
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = convertToUser(userDto);
        User newUser = userRepository.save(user);
        return convertToUserDto(newUser);
    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertToUserDto(user)).collect(Collectors.toList());
    }

    /**
     * get all user by pagination
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @Override
    public UserResponse getAllUser(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<User> pageUsers = userRepository.findAll(pageRequest);

        List<User> users = pageUsers.getContent();
        List<UserDto> userDtos = users.stream().map(user -> convertToUserDto(user)).collect(Collectors.toList());

        return new UserResponse(userDtos, pageUsers.getNumber(),
                pageUsers.getSize(), pageUsers.getTotalElements(),
                pageUsers.getTotalPages(), pageUsers.isLast());

    }

    @Override
    public UserDto getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new BizException(BizCodeEnum.USER_NOT_FOUND));
        return convertToUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new BizException(BizCodeEnum.USER_NOT_FOUND));

        if (userDto.getUsername() != null && !userDto.getUsername().isBlank()) {
            user.setUsername(userDto.getUsername());
        }

        if (userDto.getPoints() != null) {
            user.setPoints(userDto.getPoints());
        }

        User updateUser = userRepository.save(user);
        return convertToUserDto(updateUser);
    }

    @Override
    public void deleteUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new BizException(BizCodeEnum.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    @Override
    public void updateUserPoints(long id, int amount) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new BizException(BizCodeEnum.USER_NOT_FOUND));
        user.setPoints(user.getPoints() + amount);
        userRepository.save(user);
    }


    private User convertToUser(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return user;
    }

    private UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }
}
