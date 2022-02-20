package com.imooc.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.reader.entity.Evaluation;
import com.imooc.reader.entity.MemberReadState;
import com.imooc.reader.entity.Member;
import com.imooc.reader.mapper.EvaluationMapper;
import com.imooc.reader.mapper.MemberMapper;
import com.imooc.reader.mapper.MemberReadStateMapper;
import com.imooc.reader.service.MemberService;
import com.imooc.reader.service.exception.BussinessException;
import com.imooc.reader.utils.MD5Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("memberService")
@Transactional
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberMapper memberMapper;
    @Resource
    private MemberReadStateMapper memberReadStateMapper;

    @Resource
    private EvaluationMapper evaluationMapper;

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

    /**
     * 登录检查
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录对象
     */
    public Member checkLogin(String username, String password) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<Member>();
        queryWrapper.eq("username",username);
        // 判断用户是否存在
        Member member = memberMapper.selectOne(queryWrapper);
        if(member == null){
            throw new BussinessException("M02","用户不存在");
        }else {
            String md5 = MD5Utils.md5Digest(password, member.getSalt());
            if(!md5.equals(member.getPassword())){
                throw new BussinessException("M03","输入密码有误");
            }
        }
        return member;
    }

    /**
     * 获取阅读的状态
     *
     * @param memberId 会员编号
     * @param bookId   图书编号
     * @return 阅读状态对象
     */
    public MemberReadState selectMemberReadState(Long memberId, Long bookId) {
        QueryWrapper<MemberReadState>   queryWrapper = new QueryWrapper<MemberReadState>();
        queryWrapper.eq("book_id",bookId);
        queryWrapper.eq("member_id",memberId);
        MemberReadState memberReadState = memberReadStateMapper.selectOne(queryWrapper);

        return memberReadState;
    }

    /**
     * 更新阅读状态
     *
     * @param memberId  会员编号
     * @param bookId    图书编号
     * @param readState 阅读状态
     * @return 阅读状态对象
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public MemberReadState updateMemberReadState(Long memberId, Long bookId, Integer readState) {
        QueryWrapper<MemberReadState> queryWrapper = new QueryWrapper<MemberReadState>();
        queryWrapper.eq("book_id",bookId);
        queryWrapper.eq("member_id",memberId);

        MemberReadState memberReadState = memberReadStateMapper.selectOne(queryWrapper);
        // 无则新增 有则更新
        if(memberReadState == null){
            memberReadState = new MemberReadState();
            memberReadState.setMemberId(memberId);
            memberReadState.setBookId(bookId);
            memberReadState.setCreateTime(new Date());
            memberReadState.setReadState(readState);
            memberReadStateMapper.insert(memberReadState);
        }else {
            memberReadState.setReadState(readState);
            memberReadStateMapper.updateById(memberReadState);
        }
        return memberReadState;
    }

    /**
     * 发布新的短评
     *
     * @param memberId 会员编号
     * @param bookId   图书编号
     * @param score    评分
     * @param content  短评内容
     * @return 短评对象
     */
    public Evaluation evaluate(Long memberId, Long bookId, Integer score, String content) {
        return null;
    }

    /**
     * 短评点赞
     *
     * @param evaluationId 短评编号
     * @return 短评对象
     */
    public Evaluation enjoy(Long evaluationId) {
        // 点赞对象自增+1
        Evaluation evaluation = evaluationMapper.selectById(evaluationId);
        evaluation.setEnjoy(evaluation.getEnjoy()+1);
        evaluationMapper.updateById(evaluation);
        return evaluation;
    }


}
