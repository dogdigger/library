package com.elias.reader.controller;

import com.elias.book.entity.Book;
import com.elias.common.annotation.LogIt;
import com.elias.common.response.Response;
import com.elias.reader.service.ReaderService;
import com.elias.reader.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chengrui
 * <p>create at: 2020-07-27 20:12</p>
 * <p>description: </p>
 */
@RestController
@RequestMapping("/api/reader")
public class ReaderController {
    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService){
        this.readerService = readerService;
    }

    @LogIt
    @GetMapping("/book/search")
    public Response<Book> searchBook(@RequestParam(name = "category") String category, HttpServletResponse response){
        try{
            response.sendRedirect("http://www.baidu.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok(this.readerService.searchBook(category));
    }

    @LogIt
    @PostMapping("/book")
    public Response<Book> addBook(@RequestBody Book book){
        return Response.ok(this.readerService.addBook(book));
    }

    @RequestMapping("/redirect")
    public void redirect(HttpServletResponse response){
        try{
            response.sendRedirect("http://www.baidu.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/hello")
    public String hello(){
        return new TestService().sayHello();
    }

}
