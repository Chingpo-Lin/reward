package com.chuwa.reward.service;

import com.chuwa.reward.payload.UserDto;
import com.chuwa.reward.payload.UserResponse;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    List<UserDto> getAllUser();

    UserResponse getAllUser(int pageNo, int pageSize, String sortBy, String sortDir);

    UserDto getUserById(long id);

    UserDto updateUser(UserDto userDto, long id);

    void deleteUserById(long id);

    void updateUserPoints(long id, int amount);
}
