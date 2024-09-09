package com.swcamp9th.springsecuritypratice.member.command.application.service;

import com.swcamp9th.springsecuritypratice.common.exception.CustomErrorCode;
import com.swcamp9th.springsecuritypratice.common.exception.CustomException;
import com.swcamp9th.springsecuritypratice.member.command.application.dto.req.ReqSignupDTO;
import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.RoleMemberPk;
import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.RoleType;
import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.entity.Member;
import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.entity.RoleMember;
import com.swcamp9th.springsecuritypratice.member.command.domain.repository.MemberRepository;
import com.swcamp9th.springsecuritypratice.member.command.domain.repository.RoleRepository;
import com.swcamp9th.springsecuritypratice.security.CustomUser;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "commandMemberService")
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public MemberServiceImpl(ModelMapper modelMapper
                           , MemberRepository memberRepository
                           , RoleRepository roleRepository
                           ,BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.modelMapper = modelMapper;
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void registMember(ReqSignupDTO reqSignupDTO) {

        log.info(reqSignupDTO.toString());

        if(!reqSignupDTO.isEmailApprove())
            throw new CustomException(CustomErrorCode.NO_APPROVE_EMAIL);

        Member member = modelMapper.map(reqSignupDTO, Member.class);
        member.setMemberUniqueId(UUID.randomUUID().toString());
        member.setPassword(bCryptPasswordEncoder.encode(reqSignupDTO.getPassword()));
        Member savedMember = memberRepository.save(member);

        // 회원 권한 부여
        roleRepository.save(RoleMember.builder()
            .memberId(savedMember.getId())
            .roleId(RoleType.USER.getRoleId())
            .build());

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        /* 설명. 넘어온 email이 사용자가 입력한 id로써 email로 회원을 조회하는 쿼리 메소드 작성 */
        Member loginUser = memberRepository.findByEmail(email);

        if(loginUser == null)
            throw new UsernameNotFoundException(email + " 이메일 아이디의 유저는 존재하지 않습니다");

        /* 설명. 사용자의 권한들을 가져왔다는 가정 */
        List<RoleMember> roleMembers = roleRepository.findByMemberId(loginUser.getId());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for(RoleMember roleMember: roleMembers){
            if(roleMember.getRoleId().equals(RoleType.USER.getRoleId())){
                grantedAuthorities.add(new SimpleGrantedAuthority(RoleType.USER.getType()));
            } else if (roleMember.getRoleId().equals(RoleType.ADMIN.getRoleId())) {
                grantedAuthorities.add(new SimpleGrantedAuthority(RoleType.ADMIN.getType()));
            }
        }

        return new CustomUser(loginUser.getId(), loginUser.getEmail()
            , loginUser.getPassword(), loginUser.getMemberUniqueId(), grantedAuthorities);
    }
}
