package com.project.devgram.service;

import com.project.devgram.dto.FollowDto;
import com.project.devgram.dto.UserDto;
import com.project.devgram.entity.Follow;
import com.project.devgram.entity.Users;
import com.project.devgram.exception.DevGramException;
import com.project.devgram.exception.errorcode.UserErrorCode;
import com.project.devgram.repository.FollowRepository;
import com.project.devgram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    private final UserRepository userRepository;


    @Transactional
    public void followAdd(FollowDto dto) {

        Users optionalUser = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(()-> new DevGramException(UserErrorCode.USER_NOT_EXIST));
       Users optionalFollowing = userRepository.findById(dto.getFollowingUserSeq())
               .orElseThrow(()-> new DevGramException(UserErrorCode.FOLLOWER_IS_NOT_EXIST));
        List<Follow> followList = followRepository.findByFollowing_UserSeqOrderByFollowing(dto.getFollowingUserSeq());


        if (followList.size() == 0) {
            // 내가 팔로우 신청을 했을때

            optionalUser.setFollowCount(optionalUser.getFollowCount() + 1);

            optionalFollowing.setFollowerCount(optionalUser.getFollowerCount() + 1);

            Users follower = Users.builder()
                    .userSeq(optionalUser.getUserSeq())
                    .followerList(new ArrayList<>())
                    .followingList(new ArrayList<>())
                    .build();

            Users following = Users.builder()
                    .userSeq(optionalFollowing.getUserSeq())
                    .followingList(new ArrayList<>())
                    .followerList(new ArrayList<>())
                    .build();

            Follow followers = new Follow();
            followers.setFollower(follower);
            followers.setFollowing(following);

            optionalUser.getFollowingList().add(followers);

            userRepository.save(optionalUser);
            log.info("finish to save");
        }

    }

    //나를 팔로잉한 사람 리스트
    public List<UserDto> getFollowList(FollowDto dto) {

        List<Follow> follower = followRepository.findByFollowing_UserSeqOrderByFollowing(dto.getUserSeq());

        return getUserDtoLists(follower, "follower");
    }

    //내가 팔로잉한 사람 리스트
    public List<UserDto> getFollowingList(FollowDto dto) {

        List<Follow> following = followRepository.findByFollower_UserSeqOrderByFollower(dto.getUserSeq());

        return getUserDtoLists(following, "following");
    }

    private List<UserDto> getUserDtoLists(List<Follow> followList, String check) {
        if (followList.size() > 0) {

            List<Users> userList = new ArrayList<>();

            for (int i = 0; i < followList.size(); i++) {
                //나를 팔로우한 유저 userSeq
                Long userSeq;

                if("follower".equals(check)) {
                    userSeq = followList.get(i).getFollower().getUserSeq();
                }else {
                    userSeq = followList.get(i).getFollowing().getUserSeq();
                }

                Optional<Users> optionalUser = userRepository.findById(userSeq);

                if (optionalUser.isPresent()) {

                    Users userInfo = optionalUser.get();
                    userList.add(userInfo);
                }
            }

            return UserDto.of(userList);

        }
        log.error("해당하는 list 크기가 0입니다.");
        return null;
    }



    public void deleteFollowUser(String username,Long followingUserSeq){
        log.info("following user delete");

        Users user =userRepository.findByUsername(username)
                .orElseThrow(()-> new DevGramException(UserErrorCode.USER_NOT_EXIST));

        Users followingUser = userRepository.findById(followingUserSeq)
                .orElseThrow(()-> new DevGramException(UserErrorCode.FOLLOWER_IS_NOT_EXIST));


        Optional<Follow> optionalFollow = followRepository.findFollowUserSeq(user.getUserSeq(),followingUserSeq);

        if(optionalFollow.isPresent()){

            Follow follow =optionalFollow.get();

            followRepository.deleteList(follow.getFollowSeq());

            followerCountMinus(follow);

            log.info("following user delete success");
        }

    }

    // 팔로우 취소시 상대 follower, 내 following 감소
    private void followerCountMinus (Follow follow){

        log.info("follower Minus start");

        Long mySeq = follow.getFollower().getUserSeq();
        Long yourSeq = follow.getFollowing().getUserSeq();

        Optional<Users> optionalUser = userRepository.findById(mySeq);
        Optional<Users> optionalFollowing = userRepository.findById(yourSeq);

        if(optionalUser.isPresent() ) {

            Users user = optionalUser.get();
            user.setFollowCount(user.getFollowCount() - 1);

            userRepository.save(user);
        }

        if( optionalFollowing.isPresent()){

            Users followingUser = optionalFollowing.get();
            followingUser.setFollowerCount(followingUser.getFollowerCount() - 1);

            userRepository.save(followingUser);
        }

        log.info("count minus success");
    }

}

