package com.roadmaker.c_kimjongbeom.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.roadmaker.c_kimjongbeom.dto.MembersDTO;

@Mapper
public interface MemberMapper {

    // 이메일로 회원 정보를 조회하는 쿼리(로그인시)
    @Select("SELECT * FROM members WHERE mememail = #{memEmail}")
    
    MembersDTO findByEmail(String memEmail);


    // 이메일로 memId를 조회하는 쿼리
    @Select("SELECT memId FROM members WHERE mememail = #{memEmail}")
    Integer findMemIdByEmail(@Param("memEmail") String memEmail);

    // 회원 정보를 데이터베이스에 저장하는 쿼리
    @Insert("INSERT INTO members (mememail, memsecret, memnickname, memgender, memage, memrole, memstatus, memjoindate) " +
            "VALUES (#{memEmail}, #{memSecret}, #{memNickname}, #{memGender}, #{memAge}, #{memRole}, #{memStatus}, #{memJoinDate})")
    void insertMember(MembersDTO mdto);
    public void insertUser(MembersDTO membersDTO);

    @Select("SELECT * FROM members")
    public List<MembersDTO> getMemberList();

    // refresh token 저장 쿼리
     @Insert("insert into refresh_tokens(memId, refresh_token, expiry_date) " +
            "values(#{memId}, #{refresh_token}, #{expiry_date}) " +
            "on duplicate key update refresh_token = #{refresh_token}, expiry_date = #{expiry_date}")
    void saveRefreshToken(@Param("memId") Integer memId, @Param("refresh_token") String refresh_token,
                        @Param("expiry_date") Date expiry_date);

    // refresh token 조회 쿼리
    @Select("SELECT refresh_token FROM refresh_tokens WHERE memEmail = #{memEmail}")
    String findRefreshToken(@Param("memEmail") String memEmail);

    // refresh token 삭제 쿼리
    @Delete("DELETE FROM refresh_tokens WHERE memEmail = #{memEmail}")
    void deleteRefreshToken(@Param("memEmail") String memEmail);    

}


/*
  Mybatis Mapper
 */