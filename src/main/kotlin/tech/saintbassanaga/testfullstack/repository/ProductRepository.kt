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

@Repository
class ProductRepository(private val jdbcClient: JdbcClient) {

    fun findAll(): List<Product> =
        jdbcClient.sql("SELECT * FROM products ORDER BY created_at DESC")
            .query(Product::class.java)
            .list()

    fun searchByTitle(query: String): List<Product> =
        jdbcClient.sql("SELECT * FROM products WHERE title ILIKE '%' || :q || '%' ORDER BY created_at DESC")
            .param("q", query)
            .query(Product::class.java)
            .list()

    fun save(product: Product) {
        jdbcClient.sql("INSERT INTO products (title, price, variants) VALUES (:title, :price, :variants)")
            .param("title", product.title)
            .param("price", product.price)
            .param("variants", product.variants)
            .update()
    }
}
