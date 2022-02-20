package com.imooc.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.reader.entity.Book;
import com.imooc.reader.mapper.BookMapper;
import com.imooc.reader.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("bookService")
@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
public class BookServiceImpl implements BookService {
    @Resource
    private BookMapper bookMapper;

    /**
     * 分页查询图书
     * @param page 页号
     * @param rows 每页记录数
     * @param categoryId 分类编号
     * @param order 排序方式
     * @return 分页对象
     */
    public IPage<Book> paging(Long categoryId , String order,Integer page, Integer rows) {
        // 第几页 几行数据？
        Page<Book> p = new Page<Book>(page,rows);
        // 条件构造器
        QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>();
        if(categoryId != null && categoryId != -1){
            queryWrapper.eq("category_id",categoryId);
        }
        if(order != null){
            if(order.equals("quantity")){
                queryWrapper.orderByDesc("evaluation_quantity");
            }else if(order.equals("score")){
                queryWrapper.orderByDesc("evaluation_score");
            }
        }

        // 默认对所有数据进行查询
        IPage<Book> pageObject = bookMapper.selectPage(p, queryWrapper);
        return pageObject;
    }

    public Book selectById(Long bookId) {
        Book book = bookMapper.selectById(bookId);
        return book;
    }

    /**
     * 更新图书评分/评价数量
     */
    @Transactional
    public void updateEvaluation() {
        bookMapper.updateEvaluation();
    }

    /**
     * 创建新的图书
     *
     * @param book
     */
    @Transactional
    public Book createBook(Book book) {
        bookMapper.insert(book);
        return book;
    }

    /**
     * 更新图书
     *
     * @param book 新图书数据
     * @return 更新后的数据
     */
    public Book updateBook(Book book) {
        return null;
    }

    /**
     * 删除图书及相关数据
     *
     * @param bookId 图书编号
     */
    public void deleteBook(Long bookId) {

    }


}
