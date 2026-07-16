package com.devjoint.librarymanagement.service;

import com.devjoint.librarymanagement.dto.book.BookDto;
import com.devjoint.librarymanagement.dto.book.BookRequest;
import com.devjoint.librarymanagement.entity.Author;
import com.devjoint.librarymanagement.entity.Book;
import com.devjoint.librarymanagement.exception.NotFoundException;
import com.devjoint.librarymanagement.repository.AuthorRepository;
import com.devjoint.librarymanagement.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;


    public BookDto createBook(BookRequest request) {
        Author author = authorRepository.findById(request.getAuthorId()).orElseThrow(() -> new NotFoundException("Author not found"));

        Book book = Book.builder()
                .title(request.getTitle())
                .author(author)
                .genre(request.getGenre())
                .build();

        book = bookRepository.save(book);

        return bookToDto(book);

    }

    public List<BookDto> getBooksByAuthorId(UUID id) {
        List<BookDto> books = new ArrayList<>();

        var opt = authorRepository.findById(id);
        if (opt.isEmpty()) {
            throw new NotFoundException("Author not found");
        }

        var author = opt.get();

        bookRepository.findBooksByAuthor(author).forEach(book -> {
            books.add(bookToDto(book));
        });

        return books;

    }

    public BookDto getBookById( UUID id) {
        var opt = bookRepository.findById(id);
        if (opt.isEmpty()) {
            throw new NotFoundException("Book not found");
        }
        return bookToDto(opt.get());
    }

    public Void deleteBook( UUID id) {
        bookRepository.deleteById(id);
        return null;
    }

    public BookDto updateBook(UUID id,BookRequest bookRequest) {
        var opt = bookRepository.findById(id);
        if (opt.isEmpty()) {
            throw new NotFoundException("Book not found");
        }
        Book book = opt.get();
        book.setTitle(bookRequest.getTitle());
        book.setGenre(bookRequest.getGenre());
        bookRepository.save(book);
        return bookToDto(book);

    }

    public List<BookDto> getAllBooks() {
        List<BookDto> books = new ArrayList<>();
        bookRepository.findAll().forEach(book -> {
            books.add(bookToDto(book));
        });
        return books;
    }



    private BookDto bookToDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorId(book.getAuthor().getId())
                .genre(book.getGenre())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }


}
