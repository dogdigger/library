package com.elias.reader.controller;

import com.elias.book.entity.Book;
import com.elias.common.annotation.LogIt;
import com.elias.common.response.Response;
import com.elias.reader.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public Response<Book> searchBook(@RequestParam(name = "category") String category){
        return Response.ok(this.readerService.searchBook(category));
    }

    @LogIt
    @PostMapping("/book")
    public Response<Book> addBook(@RequestBody Book book){
        return Response.ok(this.readerService.addBook(book));
    }

}
