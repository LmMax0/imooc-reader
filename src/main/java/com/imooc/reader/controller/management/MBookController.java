package com.imooc.reader.controller.management;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/management/book")
public class MBookController {
    @GetMapping("/index.html")
    public ModelAndView showBook(){
        return  new ModelAndView("/management/book");
    }
}
