package tech.saintbassanaga.testfullstack.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import tech.saintbassanaga.testfullstack.domains.Product
import tech.saintbassanaga.testfullstack.repository.ProductRepository

/**
 * @author saintbassanaga
 * @date 8/26/25
 * @project TestFullStack
 * @file Er
 * @description [Insert description here]
 */
@Controller
class ProductController(private val repo: ProductRepository) {

    @GetMapping("/")
    fun index(): String = "index"

    @GetMapping("/search")
    fun searchPage(model: Model): String {
        model.addAttribute("products", repo.findAll())
        return "search"
    }

    @GetMapping("/products")
    fun listProducts(model: Model): String {
        model.addAttribute("products", repo.findAll())
        return "fragments/products :: table"
    }

    @GetMapping("/products/search")
    fun searchProducts(@RequestParam(required = false, name = "q") q: String?, model: Model): String {
        val query = q?.trim().orEmpty()
        val products = if (query.isBlank()) repo.findAll() else repo.searchByTitle(query)
        model.addAttribute("products", products)
        return "fragments/products :: table"
    }

    @PostMapping("/products")
    fun addProduct(@RequestParam title: String, @RequestParam price: String, model: Model): String {
        repo.save(Product(title = title, price = price.toBigDecimal()))
        model.addAttribute("products", repo.findAll())
        return "fragments/products :: table"
    }
}
