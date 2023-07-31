package com.chuwa.reward.controller;

import com.chuwa.reward.payload.UserDto;
import com.chuwa.reward.payload.UserResponse;
import com.chuwa.reward.service.UserService;
import com.chuwa.reward.utils.AppConstants;
import com.chuwa.reward.utils.CommonUtils;
import com.chuwa.reward.utils.JsonData;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public JsonData createUser(@Valid @RequestBody UserDto userDto, HttpServletResponse httpServletResponse) {

        log.debug("save user with Post /api/v1/users with request: {}", userDto);
        userDto.setPoints(0); // set initial point
        UserDto result = userService.createUser(userDto);
        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        log.debug("Respond Post /api/v1/users with: {}", result);
        return JsonData.buildSuccess(CommonUtils.encode(result));
    }

    @GetMapping()
    public JsonData getUser(HttpServletResponse httpServletResponse) {
        log.debug("get all user with Get /api/v1/users");
        List<UserDto> userDtoList = userService.getAllUser();
        httpServletResponse.setStatus(HttpStatus.OK.value());
        log.debug("Respond Get /api/v1/users with: {}", userDtoList);
        return JsonData.buildSuccess(CommonUtils.encode(userDtoList));
    }

    @GetMapping("pdf")
    public void generateAllUserNamePDF(HttpServletResponse response) throws DocumentException, IOException {
        // Set the response content type for PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"alluser.pdf\"");
        Cookie cookie = new Cookie("myCookie", "cookieValue");
        cookie.setMaxAge(3600); // Set cookie's age in seconds (1 hour in this case)

        // Add the cookie to the response
        response.addCookie(cookie);

        // Create a new PDF document
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        List<UserDto> userDtoList = userService.getAllUser();
        // Open the document
        try {
            document.open();
        } catch (Exception e) {
            for (UserDto userDto: userDtoList) {
                Paragraph paragraph = new Paragraph("user: " + userDto.getUsername());
                document.add(paragraph);
            }
        } finally {
            document.close();
        }
    }

    @GetMapping("page")
    public JsonData getAllUsers(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.USER_DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.USER_DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.USER_DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.USER_DEFAULT_SORT_DIR, required = false) String sortDir,
            HttpServletResponse httpServletResponse
    ) {
        log.debug("page records with Get /api/v1/users/page with requests: {}, {}, {}, {}", pageNo, pageSize, sortBy, sortDir);
        UserResponse userResponse = userService.getAllUser(pageNo, pageSize, sortBy, sortDir);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        log.debug("Respond Get /api/v1/records/page with: {}", userResponse);
        return JsonData.buildSuccess(CommonUtils.encode(userResponse));
    }

    @GetMapping("/{id}")
    public JsonData getUserById(@PathVariable(name="id") long id, HttpServletResponse httpServletResponse) {
        log.debug("get users by id with Get /api/v1/users/{id} with requests: {}", id);
        UserDto userDto = userService.getUserById(id);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        log.debug("Respond Get /api/v1/records/{id} with: {}", userDto);
        return JsonData.buildSuccess(CommonUtils.encode(userDto));
    }

    @PutMapping("/{id}")
    public JsonData updateUserById(@PathVariable(name = "id") long id, @Valid @RequestBody UserDto userDto,
                                  HttpServletResponse httpServletResponse) {
        log.debug("update users with Put /api/v1/users/{id} with requests: {}", id, userDto);
        UserDto updateUser = userService.updateUser(userDto, id);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        log.debug("Respond Put /api/v1/records/{id} with: {}", updateUser);
        return JsonData.buildSuccess(CommonUtils.encode(updateUser));
    }

    @DeleteMapping("/{id}")
    public JsonData deleteUser(@PathVariable(name="id") Long id,
                                           HttpServletResponse httpServletResponse) {
        log.debug("delete users with Delete /api/v1/users/{id} with requests: {}", id);
        userService.deleteUserById(id);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        log.debug("Respond Delete /api/v1/records/{id}");
        return JsonData.buildSuccess();
    }
}
