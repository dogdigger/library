package com.elias.reader.service;

import com.elias.book.entity.Book;
import com.elias.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chengrui
 * <p>create at: 2020-07-27 20:06</p>
 * <p>description: </p>
 */
@Service
public class ReaderService {
    private final BookService bookService;

    @Autowired
    public ReaderService(BookService bookService){
        this.bookService = bookService;
    }

    public Book searchBook(String category){
        return this.bookService.searchBook(category);
    }
}
