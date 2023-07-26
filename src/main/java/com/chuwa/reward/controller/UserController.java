package com.chuwa.reward.controller;

import com.chuwa.reward.payload.UserDto;
import com.chuwa.reward.payload.UserResponse;
import com.chuwa.reward.service.UserService;
import com.chuwa.reward.utils.AppConstants;
import com.chuwa.reward.utils.JsonData;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public JsonData createUser(@Valid @RequestBody UserDto userDto, HttpServletResponse httpServletResponse) {
        userDto.setPoints(0); // set initial point
        UserDto result = userService.createUser(userDto);
        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return JsonData.buildSuccess(result);
    }

    @GetMapping()
    public JsonData getUser(HttpServletResponse httpServletResponse) {
        List<UserDto> userDtoList = userService.getAllUser();
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return JsonData.buildSuccess(userDtoList);
    }

    @GetMapping("page")
    public JsonData getAllUsers(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.USER_DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.USER_DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.USER_DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.USER_DEFAULT_SORT_DIR, required = false) String sortDir,
            HttpServletResponse httpServletResponse
    ) {
        UserResponse userResponse = userService.getAllUser(pageNo, pageSize, sortBy, sortDir);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return JsonData.buildSuccess(userResponse);
    }

    @GetMapping("/{id}")
    public JsonData getUserById(@PathVariable(name="id") long id, HttpServletResponse httpServletResponse) {
        UserDto userDto = userService.getUserById(id);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return JsonData.buildSuccess(userDto);
    }

    @PutMapping("/{id}")
    public JsonData updateUserById(@PathVariable(name = "id") long id, @Valid @RequestBody UserDto userDto,
                                  HttpServletResponse httpServletResponse) {
        UserDto updateUser = userService.updateUser(userDto, id);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return JsonData.buildSuccess(updateUser);
    }

    @DeleteMapping("/{id}")
    public JsonData deleteUser(@PathVariable(name="id") Long id,
                                           HttpServletResponse httpServletResponse) {
        userService.deleteUserById(id);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return JsonData.buildSuccess();
    }




}
