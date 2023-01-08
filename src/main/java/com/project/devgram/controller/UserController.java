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
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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



    @ApiOperation(value ="BLACKLIST 잔여 캐시 데이터삭제(refresh 토큰 제거)",notes = "요청시 'Authentication'이란 이름으로 accesstoken을 header에 첨부")
    @PostMapping("/api/logout")
    public void logout(HttpServletRequest request) {

        String token = request.getHeader("Authentication");
        String username = usernameMaker(token);

        boolean redis = redisService.deleteRefresh(username);

        // 추가 accessToken 만료 ;
        if (redis) {
            redisService.blackListPush(token);

        }

    }

    @ApiOperation(value ="유저 정보 조회",notes = "요청시 'Authentication'이란 이름으로 accesstoken을 header에 첨부")
    @GetMapping("/api/user")
    public UserDto getUserDetails(HttpServletRequest request) {


        String token = request.getHeader("Authentication");
        String username = usernameMaker(token);

        return userService.getUserDetails(username);

    }
    @ApiOperation(value ="유저정보수정",notes = "요청시 access토큰 첨부")
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

    @ApiOperation(value ="유저가입취소",notes = "요청시 access토큰 첨부")
    @PostMapping("/api/user/delete")
    public void deleteUserDetails(HttpServletRequest request){
        String token = request.getHeader("Authentication");
        String username = usernameMaker(token);
        log.info("확인");
        userService.deleteUserDetail(username);

    }

    
    @ApiOperation(value ="팔로잉추가",notes = "요청시 access토큰 첨부")
    @PostMapping("/api/user/follow")
    public CommonDto<?> followingUsers(HttpServletRequest request, @Valid @RequestBody FollowDto dto, BindingResult bindingResult){

        String token = request.getHeader("Authentication");

        dto.setUsername(usernameMaker(token));

        followService.followAdd(dto);

        return new CommonDto<>(HttpStatus.OK.value(), Response.SUCCESS);


    }
    @ApiOperation(value ="팔로잉취소",notes = "요청시 access토큰 첨부")
    @DeleteMapping("/api/user/follow")
    public CommonDto<?> followingUserDelete(HttpServletRequest request, @RequestBody @Valid FollowDto dto,BindingResult bindingResult) {

        String token = request.getHeader("Authentication");
        String username = usernameMaker(token);

        followService.deleteFollowUser(username,dto.getFollowingUserSeq());

        return new CommonDto<>(HttpStatus.OK.value(), Response.SUCCESS);

    }
    @ApiOperation(value ="팔로워리스트확인",notes = "요청시 access토큰 첨부")
    //나를 팔로우한 사용자
    @GetMapping("/api/user/follow/{UserSeq}")
    public ResponseEntity<List<UserDto>> followerUserList(HttpServletRequest request, FollowDto dto
            ,@PathVariable("UserSeq") String UserSeq) {

        String token = request.getHeader("Authentication");
        log.info("token followingUserList {}", token);
        dto.setUsername(usernameMaker(token));
        dto.setUserSeq(Long.valueOf(UserSeq));

        List<UserDto> userList = followService.getFollowList(dto);

        return ResponseEntity.ok(userList);

    }

    //내가 팔로우한 사용자
    @ApiOperation(value ="팔로잉한유저리스트확인",notes = "요청시 access토큰 첨부")
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


    @ApiOperation(value ="회원가입")
    @ApiResponse(responseCode = "200", description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = CommonDto.class)))
    @PostMapping(value="/join")
    public CommonDto<Token> JoinUsers(@RequestBody @Valid UserDto dto, BindingResult bindingResult){


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
