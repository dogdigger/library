package com.elias.book.serviceImpl;

import com.elias.book.entity.Book;
import com.elias.book.service.BookService;
import com.elias.common.annotation.LogIt;

/**
 * @author chengrui
 * <p>create at: 2020-07-27 17:43</p>
 * <p>description: </p>
 */
public class BookServiceImpl implements BookService {

    public Book addBook(Book book) {
        return book;
    }

    @LogIt()
    public Book searchBook(String category){
        Book book = new Book();
        book.setName("一千零一夜");
        book.setAuthor("佚名");
        return book;
    }
}
