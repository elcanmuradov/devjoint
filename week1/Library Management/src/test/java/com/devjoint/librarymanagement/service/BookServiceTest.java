package com.devjoint.librarymanagement.service;

import com.devjoint.librarymanagement.dto.book.BookDto;
import com.devjoint.librarymanagement.dto.book.BookRequest;
import com.devjoint.librarymanagement.entity.Author;
import com.devjoint.librarymanagement.entity.Book;
import com.devjoint.librarymanagement.exception.NotFoundException;
import com.devjoint.librarymanagement.repository.AuthorRepository;
import com.devjoint.librarymanagement.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static jdk.jfr.internal.jfc.model.Constraint.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.times;
import static sun.java2d.cmm.ProfileDataVerifier.verify;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void createBook_WhenAuthorExists_ShouldReturnBookDto() {
        // 1. ARRANGE (Hazırlıq)
        UUID authorId = UUID.randomUUID();
        Author author = new Author();
        author.setId(authorId);
        author.setFullName("Stephen King");

        BookRequest request = new BookRequest("The Shining", "HORROR");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookToDto(any(Book.class))).thenReturn(new BookDto(UUID.randomUUID(), "The Shining", "HORROR", authorId, null, null));

        // 2. ACT (İcra)
        BookDto result = bookService.createBook(authorId, request);

        // 3. ASSERT (Yoxlama)
        assertNotNull(result);
        assertEquals("The Shining", result.title());
        assertEquals(authorId, result.authorId()); // DTO-da authorId qaytarıldığını yoxlayırıq

        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBook_WhenAuthorNotFound_ShouldThrowException() {
        // ARRANGE
        UUID fakeAuthorId = UUID.randomUUID();
        when(authorRepository.findById(fakeAuthorId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(NotFoundException.class, () -> {
            bookService.createBook(fakeAuthorId, new BookRequest("Test", "FICTION"));
        });
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
