package com.imooc.reader.controller.management;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.imooc.reader.entity.Book;
import com.imooc.reader.service.BookService;
import com.imooc.reader.service.exception.BussinessException;
import org.jsoup.Jsoup;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.annotation.Resource;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/management/book")
public class MBookController {

    @Resource
    private BookService bookService;
    @GetMapping("/index.html")
    public ModelAndView showBook(){
        return  new ModelAndView("/management/book");
    }

    /**
     *  wangEditor 文件上传
     * @param file 上传文库
     * @param request 原生请求对象
     * @return 数据
     * @throws IOException
     */
    @PostMapping("/upload")
    @ResponseBody
    public Map upload(@RequestParam("img")MultipartFile file, HttpServletRequest request) throws IOException {
        String uploadPath = request.getServletContext().getResource("/").getPath() + "/upload/";
        String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()); // 存储文件名
        // 最后出现 . 的位置 获得扩展名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        // 保存文件到upload目录
        file.transferTo(new File(uploadPath+fileName+suffix));
        Map result = new HashMap();
        result.put("errno", 0);
        result.put("data",new String[] {"/upload/"+ fileName + suffix});

        return  result;
    }

    /**
     * 新增图书
     * @param book
     * @return
     */
    @PostMapping("/create")
    @ResponseBody
    public Map createBook(Book book){
        Map result = new HashMap();
        try {
            book.setEvaluationQuantity(0);
            book.setEvaluationScore(0f);
            Document doc = (Document) Jsoup.parse(book.getDescription());//解析图书详情
            Element img = doc.select("img").first();//获取图书详情第一图的元素对象
            String cover = img.attr("src");
            book.setCover(cover); //来自于description描述的第一幅图
            bookService.createBook(book);
            result.put("code", "0");
            result.put("msg", "success");
        }catch (BussinessException ex){
            ex.printStackTrace();
            result.put("code", ex.getCode());
            result.put("msg", ex.getMsg());
        }
        return result;
    }

    /**
     * 分页查询图书数据
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public Map list(Integer page , Integer limit){
        if(page == null){
            page = 1;
        }

        if(limit == null){
            limit = 10;
        }

        IPage<Book> pageObject = bookService.paging(null, null, page, limit);
        Map result = new HashMap();
        result.put("code", "0");
        result.put("msg", "success");
        result.put("data", pageObject.getRecords());//当前页面数据
        result.put("count", pageObject.getTotal());//未分页时记录总数
        return result;
    }
    /**
     * 获取图书详细信息
     * @param bookId
     * @return
     */
    @GetMapping("/id/{id}")
    @ResponseBody
    public Map selectById(@PathVariable("id") Long bookId){
        Book book = bookService.selectById(bookId);
        Map result = new HashMap();
        result.put("code", "0");
        result.put("msg", "success");
        result.put("data", book);
        return result;
    }

    /**
     * 更新图书数据
     * @param book
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public Map updateBook(Book book){
        Map result = new HashMap();
        try {
            Book rawBook = bookService.selectById(book.getBookId());
            rawBook.setBookName(book.getBookName());
            rawBook.setSubTitle(book.getSubTitle());
            rawBook.setAuthor(book.getAuthor());
            rawBook.setCategoryId(book.getCategoryId());
            rawBook.setDescription(book.getDescription());
            Document doc = Jsoup.parse(book.getDescription());
            String cover = doc.select("img").first().attr("src");
            rawBook.setCover(cover);
            bookService.updateBook(rawBook);
            result.put("code", "0");
            result.put("msg", "success");
        }catch (BussinessException ex){
            ex.printStackTrace();
            result.put("code", ex.getCode());
            result.put("msg", ex.getMsg());
        }
        return result;
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public Map deleteBook(@PathVariable("id") Long bookId){
        Map result = new HashMap();
        try{
            bookService.deleteBook(bookId);
            result.put("code", "0");
            result.put("msg", "success");
        }catch (BussinessException ex){
            ex.printStackTrace();
            result.put("code", ex.getCode());
            result.put("msg", ex.getMsg());
        }
        return result;
    }



}
