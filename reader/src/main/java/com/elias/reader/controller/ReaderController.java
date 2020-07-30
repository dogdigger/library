package com.elias.reader.controller;

import com.elias.book.entity.Book;
import com.elias.reader.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/book/search")
    public Book searchBook(@RequestParam(name = "category") String category){
        return this.readerService.searchBook(category);
    }
}
