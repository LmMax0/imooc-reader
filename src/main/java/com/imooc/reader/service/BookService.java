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

    /**
     * 更新图书评分/评价数量
     */
    public void updateEvaluation();

    /**
     * 创建新的图书
     */
    public Book createBook(Book book);

    /**
     * 更新图书
     * @param book 新图书数据
     * @return 更新后的数据
     */
    public Book updateBook(Book book);

    /**
     * 删除图书及相关数据
     * @param bookId 图书编号
     */
    public void deleteBook(Long bookId);

}
