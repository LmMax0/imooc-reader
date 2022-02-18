package com.imooc.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.reader.entity.Member;
import com.imooc.reader.mapper.MemberMapper;
import com.imooc.reader.service.MemberService;
import com.imooc.reader.service.exception.BussinessException;
import com.imooc.reader.utils.MD5Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("memberService")
@Transactional
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberMapper memberMapper;

    public Member createMember(String username, String password, String nickname) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<Member>();
        queryWrapper.eq("username",username);
        List<Member> memberList = memberMapper.selectList(queryWrapper);
        if(memberList.size() > 0){
            // 用户名已经存在 要抛出异常
            throw new BussinessException("M01","用户名已存在");
        }
        Member member = new Member();
        member.setUsername(username);
        member.setNickname(nickname);
//         保存加密密码 -- 需要添加加密解密组件
        // 使用随机数字作为盐值
        // 生成1000以内的随机数 + 1000 表示 1000 - 1999
        int salt = new Random().nextInt(1000) + 1000;
        String md5Digest = MD5Utils.md5Digest(password, salt);

        member.setPassword(md5Digest);
        member.setSalt(salt);
        member.setCreateTime(new Date());

        memberMapper.insert(member);
        return member;
    }
}
