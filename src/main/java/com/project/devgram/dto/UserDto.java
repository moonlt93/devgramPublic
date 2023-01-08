package com.project.devgram.dto;


import com.project.devgram.entity.Users;
import com.project.devgram.type.ROLE;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "유저dto객체")
@Data
@Slf4j
@RequiredArgsConstructor
public class UserDto {


    @NotNull(message = "아이디는 null일수 없습니다.")
    private String id;

    @Schema(description = "유저번호")
    private Long userSeq;
    @Email
    @Schema(description = "이메일", example = "abc@gmail.com")
    private String email;
    
    @Schema(description = "'github' + github에서 제공하는 pk => 서버에서 제공하는 닉네임",example = "github121pk")
    private String username;
    @Schema(description = "유저가 가진 권한",example = "ROLE_USER")
    private ROLE role;


    @Schema(description = "유저프로필사진")
    private MultipartFile imageFile;
   

    @Schema(description = "github에서 제공하는 pk값")
    private String pkId;
    @Schema(description = "연차")
    private String annual;
    @Schema(description = "해당직군")
    private String job;

    @Schema(description = "내가 팔로잉한 유저 카운트")
    private int followCount;
    @Schema(description = "나를 팔로잉한 유저 카운트")
    private int followerCount;


    @Builder
    public UserDto(Long userSeq, String email, String password, String username,
                   ROLE role, String providerId, String annual, String job, int followCount, int followerCount){

        this.username = username;
        this.userSeq = userSeq;
        this. email = email;
        this.job = job;
        this.role= role;
        this.followCount=followCount;
        this.annual = annual;
        this.followerCount= followerCount;
    }

    public static List<UserDto> of(List<Users> userList) {

        if(userList.size() > 0){
            List<UserDto> followerList = new ArrayList<>();
            for (Users u: userList) {
                UserDto dto = UserDto.builder()
                        .userSeq(u.getUserSeq())
                        .username(u.getUsername())
                        .annual(u.getAnnual())
                        .job(u.getJob())
                        .followerCount(u.getFollowerCount())
                        .followerCount(u.getFollowerCount())
                        .build();
                followerList.add(dto);
            }
            return followerList;
        }
        log.error("followerList error");
        return null;

    }


}
