package com.sd.laborator.services

import com.sd.laborator.interfaces.LibraryDAO
import com.sd.laborator.model.Book
import com.sd.laborator.model.Content
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.sql.SQLException
import java.util.regex.Pattern

class BookRowMapper : RowMapper<Book> {
    @Throws(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNum: Int): Book {
        return Book(rs.getInt("id"), Content(rs.getString("author"), rs.getString("text"), rs.getString("title"), rs.getString("publisher")))
    }
}

@Service
class LibraryDAOService: LibraryDAO {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate
    var pattern: Pattern = Pattern.compile("\\W")

    override fun createBookTable() {
        jdbcTemplate.execute("""CREATE TABLE "books" ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `author` VARCHAR, `text` TEXT, `title` VARCHAR, `publisher` VARCHAR, UNIQUE(author, title, publisher, text) );""")
    }

    override fun getBooks(): List<Book> {

        return jdbcTemplate.query("SELECT * FROM books", BookRowMapper())
    }

    override fun addBook(book: Book) {
        jdbcTemplate.update("INSERT INTO books (author, text, publisher, title) VALUES (${book.author}, ${book.content}, ${book.publisher}, ${book.name} )")
    }

    override fun findAllByAuthor(author: String): List<Book> {
        if(pattern.matcher(author).find()) {
            println("SQL Injection for book author")
            return emptyList()
        }
        return jdbcTemplate.query("SELECT * FROM books WHERE upper(author) LIKE '%${author.toUpperCase()}%'", BookRowMapper())
    }

    override fun findAllByTitle(title: String): List<Book> {
        if(pattern.matcher(title).find()) {
            println("SQL Injection for book title")
            return emptyList()
        }
        return jdbcTemplate.query("SELECT * FROM books WHERE upper(title) LIKE '%${title.toUpperCase()}%'", BookRowMapper())
    }

    override fun findAllByPublisher(publisher: String): List<Book> {
        if(pattern.matcher(publisher).find()) {
            println("SQL Injection for book publisher")
            return emptyList()
        }
        return jdbcTemplate.query("SELECT * FROM books WHERE upper(publisher) LIKE '%${publisher.toUpperCase()}%'", BookRowMapper())
    }
}