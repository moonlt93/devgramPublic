package com.project.devgram.service;

import com.project.devgram.dto.UserDto;
import com.project.devgram.entity.Users;
import com.project.devgram.exception.DevGramException;
import com.project.devgram.exception.errorcode.UserErrorCode;
import com.project.devgram.repository.UserRepository;
import com.project.devgram.type.ROLE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageUploader uploader;


    public UserDto getUserDetails(String username){

        Users user = userRepository.findByUsername(username)
                .orElseThrow(()-> new DevGramException(UserErrorCode.USER_NOT_EXIST,
                        "해당하는 유저가 없습니다."));

            return UserDto.builder()
                    .userSeq(user.getUserSeq())
                    .job(user.getJob())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .annual(user.getAnnual())
                    .role(user.getRole())
                    .followCount(user.getFollowCount())
                    .followerCount(user.getFollowerCount())
                    .providerId(user.getPkId())
                    .build();
        }


    public void updateUserDetails(UserDto dto) throws IOException {
        log.info("dtos {}",dto);
        String IMAGE_DIR = "DevUser";

        Users user =userRepository.findByUsername(dto.getUsername())
                .orElseThrow(()-> new DevGramException(UserErrorCode.USER_NOT_EXIST));


        user.setJob(dto.getJob());
        user.setAnnual(dto.getAnnual());

        if(dto.getImageFile() != null) {
            String imageUrl = uploader.upload(dto.getImageFile(), IMAGE_DIR);
            user.setImageUrl(imageUrl);

        }
        userRepository.save(user);
    }

    public String saveUserDetails(UserDto dto) {


        String username ="github"+dto.getId();

        log.info("username {} ",dto.getId());

        Optional<Users> userEntity = userRepository.findByUsername(username);

        if(userEntity.isPresent()) {
          log.info("user update");

          Users modifyUser = userEntity.get();

          return modifyUser.getUsername();
        }
        Users users = Users.builder()
                .username(username)
                .pkId(dto.getId())
                .email(dto.getEmail())
                .role(RoleMaker(username))
                .build();

        userRepository.save(users);
        return username;
    }

    public void deleteUserDetail(String username) {


        Optional<Users> optionalUsers = userRepository.findByUsername(username);

        if(optionalUsers.isPresent()){
            Users user = optionalUsers.get();

            userRepository.delete(user);
            log.info("유저정보 삭제 완료");
        }

    }


    private static ROLE RoleMaker(String username){
        if(username.equals("githubADMIN")){
            return ROLE.ROLE_ADMIN;
        }else {
            return ROLE.ROLE_USER;
        }
    }


}
