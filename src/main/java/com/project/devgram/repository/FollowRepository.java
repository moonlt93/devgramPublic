package com.project.devgram.repository;

import com.project.devgram.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {

    List<Follow> findByFollowing_UserSeqOrderByFollowing(Long userSeq);

    List<Follow> findByFollower_UserSeqOrderByFollower(Long userSeq);

    Optional<Follow> findByFollower_UserSeq(Long userSeq);

    @Query(value="select f from Follow f where f.follower.userSeq = :userSeq and f.following.userSeq= :followingUserSeq")
    Optional<Follow> findFollowUserSeq(@Param("userSeq") Long userSeq,@Param("followingUserSeq") Long followingUserSeq);


    @Transactional
    @Modifying
    @Query(value="delete from Follow f where f.followSeq = :followSeq")
    void deleteList(@Param("followSeq") Long followSeq);

}
