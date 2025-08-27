package tech.saintbassanaga.testfullstack.service


import com.fasterxml.jackson.databind.JsonNode
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import tech.saintbassanaga.testfullstack.domains.Product
import tech.saintbassanaga.testfullstack.repository.ProductRepository
import java.math.BigDecimal
/**
 * @author saintbassanaga
 * @date 8/26/25
 * @project TestFullStack
 * @file Scheduler
 * @description [Insert description here]
 */
@Service
class ProductScheduler(private val repo: ProductRepository) {

    private val rest = RestTemplate()

    @Scheduled(initialDelay = 0, fixedRate = 24 * 60 * 60 * 1000) // once a day
    fun fetchProducts() {
        val url = "https://famme.no/products.json"
        val response = rest.getForObject(url, JsonNode::class.java)

        val products = response?.get("products")?.take(50) ?: return

        for (p in products) {
            val title = p.get("title").asText()
            val price = p.get("price")?.asText()?.toBigDecimalOrNull() ?: BigDecimal.ZERO
            val variants = p.get("variants").toString()

            val existing = repo.findByTitle(title)
            if (existing != null && existing.id != null) {
                // Update existing product with latest data
                repo.update(existing.id, title, price, variants)
            } else {
                // Insert new product
                val product = Product(
                    title = title,
                    price = price,
                    variants = variants
                )
                repo.save(product)
            }
        }
    }
}
