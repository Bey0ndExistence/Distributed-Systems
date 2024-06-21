package com.sd.laborator.business.services

import com.sd.laborator.business.interfaces.ILibraryDAOService
import com.sd.laborator.business.models.Book
import com.sd.laborator.business.models.Content
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service


@Service
class LibraryDAOService @Autowired constructor(private val jdbcTemplate: JdbcTemplate) : ILibraryDAOService {
    private var _books: MutableSet<Book> = mutableSetOf(
        Book(
            Content(
                "Roberto Ierusalimschy",
                "Preface. When Waldemar, Luiz, and I started the development of Lua, back in 1993, we could hardly imagine that it would spread as it did. ...",
                "Programming in LUA",
                "Teora"
            )
        ),
        Book(
            Content(
                "Jules Verne",
                "Nemaipomeniti sunt francezii astia! - Vorbiti, domnule, va ascult! ....",
                "Steaua Sudului",
                "Corint"
            )
        ),
        Book(
            Content(
                "Jules Verne",
                "Cuvant Inainte. Imaginatia copiilor - zicea un mare poet romantic spaniol - este asemenea unui cal nazdravan, iar curiozitatea lor e pintenul ce-l fugareste prin lumea celor mai indraznete proiecte.",
                "O calatorie spre centrul pamantului",
                "Polirom"
            )
        ),
        Book(
            Content(
                "Jules Verne",
                "Partea intai. Naufragiatii vazduhului. Capitolul 1. Uraganul din 1865. ...",
                "Insula Misterioasa",
                "Teora"
            )
        ),
        Book(
            Content(
                "Jules Verne",
                "Capitolul I. S-a pus un premiu pe capul unui om. Se ofera premiu de 2000 de lire ...",
                "Casa cu aburi",
                "Albatros"
            )
        )
    )

    override fun getBooks(): Set<Book> {
        val selected = "SELECT author, title, publisher, text FROM book"
        val result = jdbcTemplate.queryForList(selected)
        val books = mutableSetOf<Book>()

        for (row in result) {
            val book = Book(Content(
                row["author"] as String?,
                row["text"] as String?,
                row["title"] as String?,
                row["publisher"] as String?
            ))
            books.add(book)
        }

        return books.toSet()
    }

    override fun addBook(book: Book) {
        try {
            val insertDataSQL = """
            INSERT INTO book (author, title, publisher, text ) VALUES ( ?, ?, ?, ?)
        """.trimIndent()

            jdbcTemplate.update(insertDataSQL, book.author, book.name, book.publisher, book.content)
        }catch (e: Exception) {
            println("Error inserting data: ${e.message}")
        }
    }

    override fun findAllByAuthor(author: String): Set<Book> {
        val selected = "SELECT author, title, publisher, text FROM book WHERE author LIKE ?"
        val result = jdbcTemplate.queryForList(selected, "%$author%")
        val books = mutableSetOf<Book>()

        for (row in result) {
            val book = Book(Content(
                row["author"] as String?,
                row["text"] as String?,
                row["title"] as String?,
                row["publisher"] as String?
            ))
            books.add(book)
        }

        return books.toSet()
    }

    override fun findAllByTitle(title: String): Set<Book> {
        val selected = "SELECT author, title, publisher, text FROM book WHERE title LIKE ?"
        val result = jdbcTemplate.queryForList(selected, "%$title%")
        val books = mutableSetOf<Book>()

        for (row in result) {
            val book = Book(Content(
                row["author"] as String?,
                row["text"] as String?,
                row["title"] as String?,
                row["publisher"] as String?
            ))
            books.add(book)
        }
        return books.toSet()
    }

    override fun findAllByPublisher(publisher: String): Set<Book> {
        val selected = "SELECT author, title, publisher, text FROM book WHERE publisher LIKE ?"
        val result = jdbcTemplate.queryForList(selected, "%$publisher%")
        val books = mutableSetOf<Book>()

        for (row in result) {
            val book = Book(Content(
                row["author"] as String?,
                row["text"] as String?,
                row["title"] as String?,
                row["publisher"] as String?
            ))
            books.add(book)
        }
        return books.toSet()
    }
}