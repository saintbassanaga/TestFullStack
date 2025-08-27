package tech.saintbassanaga.testfullstack.repository

/**
 * @author saintbassanaga
 * @date 8/26/25
 * @project TestFullStack
 * @file ProductRepository
 * @description [Insert description here]
 */

import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import tech.saintbassanaga.testfullstack.domains.Product
import java.math.BigDecimal

@Repository
class ProductRepository(private val jdbcClient: JdbcClient) {

    private fun toOrderBy(sort: String?): String = when (sort) {
        "priceAsc" -> "price ASC, created_at DESC"
        "priceDesc" -> "price DESC, created_at DESC"
        else -> "created_at DESC"
    }

    fun findAll(): List<Product> =
        jdbcClient.sql("SELECT * FROM products ORDER BY created_at DESC")
            .query(Product::class.java)
            .list()

    fun findAllSorted(sort: String?): List<Product> {
        val orderBy = toOrderBy(sort)
        val sql = "SELECT * FROM products ORDER BY $orderBy"
        return jdbcClient.sql(sql).query(Product::class.java).list()
    }

    fun searchByTitle(query: String): List<Product> =
        jdbcClient.sql("SELECT * FROM products WHERE title ILIKE '%' || :q || '%' ORDER BY created_at DESC")
            .param("q", query)
            .query(Product::class.java)
            .list()

    fun searchByTitleSorted(query: String, sort: String?): List<Product> {
        val orderBy = toOrderBy(sort)
        val sql = "SELECT * FROM products WHERE title ILIKE '%' || :q || '%' ORDER BY $orderBy"
        return jdbcClient.sql(sql)
            .param("q", query)
            .query(Product::class.java)
            .list()
    }

    fun findById(id: Long): Product? =
        jdbcClient.sql("SELECT * FROM products WHERE id = :id")
            .param("id", id)
            .query(Product::class.java)
            .optional()
            .orElse(null)

    fun save(product: Product) {
        jdbcClient.sql("INSERT INTO products (title, price, variants) VALUES (:title, :price, :variants)")
            .param("title", product.title)
            .param("price", product.price)
            .param("variants", product.variants)
            .update()
    }

    fun update(id: Long, title: String, price: BigDecimal, variants: String?) {
        jdbcClient.sql("UPDATE products SET title = :title, price = :price, variants = :variants WHERE id = :id")
            .param("id", id)
            .param("title", title)
            .param("price", price)
            .param("variants", variants)
            .update()
    }

    fun deleteById(id: Long) {
        jdbcClient.sql("DELETE FROM products WHERE id = :id")
            .param("id", id)
            .update()
    }
}
