package com.imooc.reader.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.reader.entity.Book;

public interface BookService {
//    使用Page 来实现分页--- mybatis plus

    /**
     * 分页查询图书
     * @param page 页号
     * @param rows 每页记录数
     * @return 分页对象
     */
    public IPage<Book> paging(Long categoryId , String order ,Integer page, Integer rows);

    /**
     * 根据编号查询图书对象
     * @param bookId Book编号
     * @return
     */
    public Book selectById(Long bookId);
}
