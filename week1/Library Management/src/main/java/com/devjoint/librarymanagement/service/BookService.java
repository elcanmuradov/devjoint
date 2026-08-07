package com.devjoint.librarymanagement.service;

import com.devjoint.librarymanagement.dto.PageResponse;
import com.devjoint.librarymanagement.dto.book.BookDto;
import com.devjoint.librarymanagement.dto.book.BookRequest;
import com.devjoint.librarymanagement.dto.genre.GenreDto;
import com.devjoint.librarymanagement.entity.Author;
import com.devjoint.librarymanagement.entity.Book;
import com.devjoint.librarymanagement.entity.Genre;
import com.devjoint.librarymanagement.exception.NotFoundException;
import com.devjoint.librarymanagement.repository.AuthorRepository;
import com.devjoint.librarymanagement.repository.BookRepository;
import com.devjoint.librarymanagement.repository.GenreRepository;
import org.springframework.cache.annotation.Cacheable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final GenreService genreService;


    public BookDto createBook(BookRequest request) {
        Author author = authorRepository.findById(request.getAuthorId()).orElseThrow(() -> new NotFoundException("Author not found"));

        Book book = Book.builder()
                .title(request.getTitle())
                .author(author)
                .build();

        book = bookRepository.save(book);

        return bookToDto(book);

    }

    @Cacheable(value = "booksByAuthor", key = "{#p0.toString() + ':' + #p1.pageNumber + ':' + #p1.pageSize + ':' + #p1.sort.toString()}")
    public PageResponse<BookDto> getBooksByAuthorId(UUID id, Pageable pageable) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new NotFoundException("Author not found"));
        return PageResponse.from(bookRepository.findBooksByAuthor(author,pageable).map(this::bookToDto));

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
        bookRepository.save(book);
        return bookToDto(book);

    }


    @Cacheable(value = "allBooks", key = "{#p0.pageNumber, #p0.pageSize, #p0.sort.toString()}")
    public PageResponse<BookDto> getAllBooks(Pageable pageable) {
        Page<UUID> ids = bookRepository.findBookIds(pageable);

        if (ids.isEmpty()) {
            return PageResponse.from(Page.empty(pageable));
        }

        List<Book> books = bookRepository.findByIdsWithDetails(ids.getContent());

        return PageResponse.from(new PageImpl<>(books, pageable, ids.getTotalElements()).map(this::bookToDto));
    }



    private BookDto bookToDto(Book book) {
        Set<GenreDto> genres = new HashSet<>();

        book.getGenres().forEach((genre) -> {
            genres.add(genreService.genreToDto(genre));
        });

        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorId(book.getAuthor().getId())
                .genres(genres)
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }


    @Transactional
    public BookDto addGenre(UUID bookId, UUID genreId) {

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        Genre genre = genreRepository.findGenreById(genreId).orElseThrow(() -> new NotFoundException("Genre not found"));

        Set<Genre> genres = book.getGenres();
        genres.add(genre);
        book.setGenres(genres);

        book = bookRepository.save(book);

        Set<Book> books = genre.getBooks();
        books.add(book);
        genre.setBooks(books);
        genreRepository.save(genre);


        return bookToDto(book);
    }

    @Transactional
    public BookDto removeGenre(UUID bookId, UUID genreId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        Genre genre = genreRepository.findGenreById(genreId).orElseThrow(() -> new NotFoundException("Genre not found"));
        Set<Book> books = genre.getBooks();
        books.remove(book);
        genre.setBooks(books);

        genreRepository.save(genre);

        Set<Genre> genres = book.getGenres();
        genres.remove(genre);
        book.setGenres(genres);
        bookRepository.save(book);

        return bookToDto(book);
    }

    public PageResponse<BookDto> filter(UUID genreId, UUID authorId, Pageable pageable) {
        return PageResponse.from(bookRepository.filter(genreId, authorId, pageable).map(this::bookToDto));
    }


    public PageResponse<BookDto> search(String title,String authorName,String genreName, Pageable pageable) {
        Specification<Book> spec = null;

        if(Optional.ofNullable(genreName).isPresent()){
            spec = BookSpecification.hasTitle(title);
        }

        if (Optional.ofNullable(authorName).isPresent()) {
            Specification<Book> authorSpec = BookSpecification.hasAuthorName(authorName);
            spec = (spec == null) ? authorSpec : spec.and(authorSpec);
        }

        if (Optional.ofNullable(title).isPresent()) {
            Specification<Book> titleSpec = BookSpecification.hasTitle(title);
            spec = (spec == null) ? titleSpec : spec.and(titleSpec);
        }

        if (Optional.ofNullable(spec).isEmpty()) {
            return PageResponse.from(bookRepository.findAll(pageable).map(this::bookToDto));
        }


        return PageResponse.from(bookRepository.findAll(spec, pageable).map(this::bookToDto));

    }

}
