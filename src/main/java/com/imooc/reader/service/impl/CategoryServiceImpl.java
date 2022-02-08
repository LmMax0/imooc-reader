package com.imooc.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.reader.entity.Category;
import com.imooc.reader.mapper.CategoryMapper;
import com.imooc.reader.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("categoryService") // 与接口名保持一致
@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true) // 开启事务
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    public List<Category> selectAll() {
        // 没有设置条件 代表查询所有
        List<Category> categoryList = categoryMapper.selectList(new QueryWrapper<Category>());
        return categoryList;
    }
}
