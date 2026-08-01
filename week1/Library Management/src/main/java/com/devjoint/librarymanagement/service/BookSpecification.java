package com.devjoint.librarymanagement.service;

import com.devjoint.librarymanagement.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) return null;
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + title.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Book> hasAuthorName(String authorName) {
        return (root, query, criteriaBuilder) -> {
            if (authorName == null || authorName.isEmpty()) return null;

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.join("author").get("name")),
                    "%" + authorName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Book> hasGenre(String genreName) {
        return (root, query, criteriaBuilder) -> {
            if (genreName == null || genreName.isEmpty()) return null;

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.join("genres").get("name")),
                    genreName.toLowerCase()
            );
        };
    }

}