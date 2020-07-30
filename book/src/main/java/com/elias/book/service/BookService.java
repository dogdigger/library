package com.elias.book.service;

import com.elias.book.entity.Book;

/**
 * @author chengrui
 * <p>create at: 2020-07-27 17:39</p>
 * <p>description: </p>
 */
public interface BookService {
    Book addBook(Book book);

    Book searchBook(String category);
}
