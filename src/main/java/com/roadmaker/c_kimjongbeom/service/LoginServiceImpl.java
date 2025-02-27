package com.roadmaker.c_kimjongbeom.service;
/*S
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;

import com.roadmaker.a_common.security.JwtUtil;
import com.roadmaker.c_kimjongbeom.dto.LoginDTO;
import com.roadmaker.c_kimjongbeom.dto.MembersDTO;
import com.roadmaker.c_kimjongbeom.mapper.MemberMapper;
import com.roadmaker.f_hwangjinsang.dto.DataDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
 */
public class LoginServiceImpl implements LoginService{
/*
    // 1. 로그인 입력정보 중 이메일을 바탕으로 일치하는 사용자 정보 호출.
    // 2. 호출된 사용자 정보를 가지고 비밀번호를 비교해 일치하는지 확인.
    // 3. 비밀번호 일치하면 JWT 발급.

    @Autowired
    private MemberMapper memberMapper;
    private MembersDTO membersDTO;

    // 생성자 주입 , 변경 불가능. 필수 방법은 아님. @Autowired 대신 사용.
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;  // UserDetailsService 주입

    public DataDTO login(@RequestBody LoginDTO loginDTO){

        DataDTO dataDTO = new DataDTO();
    
        try{
            //이메일로 회원 조회
            LoginDTO member = memberMapper.findByEmail(loginDTO.getMemEmail());

            
            //member값이 존재하지 않으면, 가입되지 않았다는 의미이므로 로그인 실패
            if (member==null){
                System.out.println("이메일을 다시 확인해주십시오!");
                

                return new DataDTO(false, null, "이메일을 다시 확인해주십시오!");
            }
*/
            /*
            여기까지 도달했다는 것은 일치하는 아이디가 있다는 의미이므로, 비밀번호가 일치하는지 비교.
            암호화되어 입력받은 password와 DB값을 직접 검증할 수 없으므로,
                1. 입력받은 password를 암호화
                2. DB의 값과 암호화시킨 password값이 일치하는지 비교
                3. 비밀번호 일치하지 않으면 오류 메세지 전송
            */
/*
            // UserDetailsService를 통해 사용자 정보 로드
            UserDetails userDetails = userDetailsService.loadUserByUsername(member.getMemEmail());
           
            if(!passwordEncoder.matches(loginDTO.getMemSecret(), member.getMemSecret())){
                return new DataDTO(false, null, "비밀번호가 일치하지 않습니다!");
            }


            // JWT 생성
            String refreshToken = jwtUtil.generateRefreshToken(member.getMemEmail());
            String accessToken = jwtUtil.generateAccessToken(member.getMemEmail());

            // refreshToken DB에 저장하기
            saveRefreshToken(member.getMemEmail(), refreshToken
                                        , jwtUtil.extractExpiration(refreshToken));

            Map<String, String> tokens = new HashMap<>();
            tokens.put("refreshToken", refreshToken);
            tokens.put("accessToken", accessToken);

            dataDTO.setData(tokens);
            dataDTO.setSuccess(true);
            dataDTO.setMessage("로그인 성공");

        } catch (Exception e) {
            dataDTO.setSuccess(false);
            dataDTO.setMessage("로그인 실패");
        }
    return dataDTO;

    }

    @Override
    public void saveRefreshToken(String memEmail, String refresh_token, Date expiry_date) {
        // refreshtoken DB에 저장 로직 
        memberMapper.saveRefreshToken(memEmail, refresh_token, expiry_date);
        
    }
        */
}
