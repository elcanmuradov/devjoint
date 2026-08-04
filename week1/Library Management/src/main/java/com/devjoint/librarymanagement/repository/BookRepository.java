package com.devjoint.librarymanagement.repository;

import com.devjoint.librarymanagement.dto.book.BookDto;
import com.devjoint.librarymanagement.entity.Author;
import com.devjoint.librarymanagement.entity.Book;
import com.devjoint.librarymanagement.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

    Page<Book> findBooksByAuthor(Author author, Pageable pageable);

    @Query("SELECT b.id FROM Book b ORDER BY b.id ASC")
    Page<UUID> findBookIds(Pageable pageable);

    @Query("SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN FETCH b.author " +
            "LEFT JOIN FETCH b.genres " +
            "WHERE b.id IN :ids")
    List<Book> findByIdsWithDetails(@Param("ids") List<UUID> ids);

    @Query(""" 
            SELECT DISTINCT b FROM Book b
            JOIN b.author a
            JOIN b.genres g
            WHERE a.id = :authorId AND g.id = :genreId""")
    Page<Book> filter(@Param("genreId") UUID genreId, @Param("authorId") UUID authorId, Pageable pageable);
}
