package com.project.devgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Schema(description = "팔로우dto객체")
@Getter
@Setter
public class FollowDto {

    // 내 pk
    @Schema(description = "로그인한 사용자 유저번호")
    private Long userSeq;

    @Schema(description = "로그인한 사용자 유저번호")
    private String username;


    //내가 follow한 사람 정보
    @NotNull(message="해당하는 user의 UserSeq가 없습니다.")
    @Positive(message = "0보다 큰수를 입력하세요")
    @Schema(description = "해당 사용자 유저번호")
    private Long followingUserSeq;

    @Schema(description = "해당 사용자 유저네임")
    private String followingUsername;

    private String followStat;

    @Builder
    public FollowDto(Long userSeq,Long followingUserSeq){
        this.userSeq=userSeq;
        this.followingUserSeq=followingUserSeq;

    }


}
