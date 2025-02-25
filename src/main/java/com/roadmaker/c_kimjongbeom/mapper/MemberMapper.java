package com.roadmaker.c_kimjongbeom.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.roadmaker.c_kimjongbeom.dto.MembersDTO;

@Mapper // MyBatis에서 Mapper 인터페이스로 사용됨을 나타내는 어노테이션
public interface MemberMapper {

    // 이메일로 회원 정보를 찾는 쿼리
    @Select("SELECT * FROM members WHERE mememail = #{memEmail}")
    MembersDTO findByEmail(String memEmail);

    // 이메일로 회원의 ID를 찾는 쿼리
    @Select("SELECT memId FROM members WHERE mememail = #{memEmail}")
    MembersDTO findMemIdByEmail(@Param("memEmail") String memEmail);

    // 모든 회원 정보를 가져오는 쿼리
    @Select("SELECT * FROM members")
    List<MembersDTO> getMemberList();  // 회원 목록을 반환하는 메서드

    // 회원을 새로 추가하는 쿼리
    @Insert("INSERT INTO members (mememail, memsecret, memnickname, memgender, memage, memrole, memstatus, memjoindate) " +
            "VALUES (#{memEmail}, #{memSecret}, #{memNickname}, #{memGender}, #{memAge}, #{memRole}, #{memStatus}, #{memJoinDate})")
    void insertMember(MembersDTO mdto); // 새 회원 정보를 받아 DB에 저장하는 메서드

    // Refresh Token을 DB에 저장하거나 이미 존재하면 업데이트하는 쿼리
    @Insert("insert into refresh_tokens(memId, refresh_token, expiry_date) " +
            "values(#{memId}, #{refresh_token}, #{expiry_date}) " +
            "on duplicate key update refresh_token = #{refresh_token}, expiry_date = #{expiry_date}")
    void saveRefreshToken(@Param("memId") Integer memId, @Param("refresh_token") String refresh_token, @Param("expiry_date") Date expiry_date);

    // 이메일로 해당 회원의 refresh_token을 찾는 쿼리
    @Select("SELECT refresh_token FROM refresh_tokens WHERE memEmail = #{memEmail}")
    String findRefreshToken(@Param("memEmail") String memEmail);

    // 이메일로 회원의 refresh_token을 삭제하는 쿼리
    @Delete("DELETE FROM refresh_tokens WHERE memEmail = #{memEmail}")
    void deleteRefreshToken(@Param("memEmail") String memEmail);
}
