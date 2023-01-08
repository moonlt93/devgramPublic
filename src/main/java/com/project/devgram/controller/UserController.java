package com.project.devgram.controller;

import com.project.devgram.dto.CommonDto;
import com.project.devgram.dto.FollowDto;
import com.project.devgram.dto.UserDto;
import com.project.devgram.oauth2.redis.RedisService;
import com.project.devgram.oauth2.token.Token;
import com.project.devgram.oauth2.token.TokenService;
import com.project.devgram.service.FollowService;
import com.project.devgram.service.UserService;
import com.project.devgram.type.ROLE;
import com.project.devgram.type.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {


    private final TokenService tokenService;
    private final UserService userService;
    private final FollowService followService;
    private final RedisService redisService;



    @PostMapping("/api/logout")
    public void logout(HttpServletRequest request) {

        String token = request.getHeader("Authentication");
        String username = usernameMaker(token);

        boolean redis = redisService.deleteRefresh(username);

        // 추가 accessToken 만료 ;
        if (redis) {
            redisService.blackListPush(token);

            log.info("add black list ");
        }

    }


    @GetMapping("/api/user")
    public UserDto getUserDetails(HttpServletRequest request) {


        String token = request.getHeader("Authentication");
        String username = usernameMaker(token);

        return userService.getUserDetails(username);

    }

    @PutMapping("/api/user")
    public void updateUserDetails(HttpServletRequest request,
                                  @RequestPart(value="user") UserDto dto
                                  ,@RequestPart(value = "file", required = false) MultipartFile file) throws IOException {


        String token = request.getHeader("Authentication");
        log.info("token {}", token);
        dto.setUsername(usernameMaker(token));
        dto.setImageFile(file);

        userService.updateUserDetails(dto);
        log.info("update success");

    }

    @PostMapping("/api/user/delete")
    public void deleteUserDetails(HttpServletRequest request){
        String token = request.getHeader("Authentication");
        String username = usernameMaker(token);
        log.info("확인");
        userService.deleteUserDetail(username);

    }



    @PostMapping("/api/user/follow")
    public CommonDto<?> followingUsers(HttpServletRequest request, @Valid @RequestBody FollowDto dto, BindingResult bindingResult){

        String token = request.getHeader("Authentication");

        dto.setUsername(usernameMaker(token));

        followService.followAdd(dto);

        return new CommonDto<>(HttpStatus.OK.value(), Response.SUCCESS);


    }

    @DeleteMapping("/api/user/follow")
    public CommonDto<?> followingUserDelete(HttpServletRequest request, @RequestBody @Valid FollowDto dto,BindingResult bindingResult) {

        String token = request.getHeader("Authentication");
        String username = usernameMaker(token);

        followService.deleteFollowUser(username,dto.getFollowingUserSeq());

        return new CommonDto<>(HttpStatus.OK.value(), Response.SUCCESS);

    }

    //나를 팔로우한 사용자
    @GetMapping("/api/user/follow/{UserSeq}")
    public ResponseEntity<List<UserDto>> followerUserList(HttpServletRequest request, FollowDto dto
            , @PathVariable("UserSeq") String UserSeq) {

        String token = request.getHeader("Authentication");
        log.info("token followingUserList {}", token);
        dto.setUsername(usernameMaker(token));
        dto.setUserSeq(Long.valueOf(UserSeq));

        List<UserDto> userList = followService.getFollowList(dto);

        return ResponseEntity.ok(userList);

    }

    //내가 팔로우한 사용자
    @GetMapping("/api/user/following/{UserSeq}")
    public ResponseEntity<List<UserDto>> followingUserList(HttpServletRequest request, FollowDto dto
            , @PathVariable("UserSeq") String UserSeq) {

        String token = request.getHeader("Authentication");

        log.info("token followingUserList {}", token);
        dto.setUsername(usernameMaker(token));
        dto.setUserSeq(Long.valueOf(UserSeq));

        List<UserDto> userList = followService.getFollowingList(dto);

        return ResponseEntity.ok(userList);

    }

    //oauth2 로그인 후 처리 토큰 발급 api
/*    @GetMapping(value = "/api/oauth/redirect")
    public CommonDto<Token> getToken(HttpServletResponse response
                                    , @RequestParam(value = "token", required = false) String token
                                    , @RequestParam(value = "refresh", required = false) String refresh) {


        response.addHeader("Authentication", token);
        response.addHeader("Refresh", refresh);

        Token tokens = new Token(token,refresh);

        return new CommonDto<>(HttpStatus.OK.value(),tokens);
    }*/
    @PostMapping(value="/join")
    public CommonDto<?> JoinUsers(@RequestBody @Valid UserDto dto, BindingResult bindingResult){


         String username = userService.saveUserDetails(dto);

         Token token = tokenService.generateToken(username, RoleMaker(username));

         return new CommonDto<>(HttpStatus.OK.value(), token);

    }

    private String usernameMaker(String token) {
          return tokenService.getUsername(token);
    }

    private static String RoleMaker(String username){
        if(username.equals("githubADMIN")){
            return String.valueOf(ROLE.ROLE_ADMIN);
        }else {
            return String.valueOf(ROLE.ROLE_USER);
        }
    }


}
