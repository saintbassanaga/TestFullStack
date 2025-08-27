package tech.saintbassanaga.testfullstack.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
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
    fun searchPage(model: Model, @RequestParam(required = false, name = "sort") sort: String?): String {
        model.addAttribute("products", repo.findAllSorted(sort))
        model.addAttribute("sort", sort ?: "newest")
        return "search"
    }

    @GetMapping("/products")
    fun listProducts(model: Model, @RequestParam(required = false, name = "sort") sort: String?): String {
        model.addAttribute("products", repo.findAllSorted(sort))
        model.addAttribute("sort", sort ?: "newest")
        return "fragments/products :: table"
    }

    @GetMapping("/products/search")
    fun searchProducts(
        @RequestParam(required = false, name = "q") q: String?,
        @RequestParam(required = false, name = "sort") sort: String?,
        model: Model
    ): String {
        val query = q?.trim().orEmpty()
        val products = if (query.isBlank()) repo.findAllSorted(sort) else repo.searchByTitleSorted(query, sort)
        model.addAttribute("products", products)
        model.addAttribute("sort", sort ?: "newest")
        return "fragments/products :: table"
    }

    @PostMapping("/products")
    fun addProduct(@RequestParam title: String, @RequestParam price: String, model: Model): String {
        repo.save(Product(title = title, price = price.toBigDecimal()))
        model.addAttribute("products", repo.findAllSorted("newest"))
        model.addAttribute("sort", "newest")
        return "fragments/products :: table"
    }

    @GetMapping("/products/{id}/edit")
    fun editProduct(@PathVariable id: Long, model: Model): String {
        val product = repo.findById(id) ?: run {
            model.addAttribute("error", "Product not found")
            model.addAttribute("products", repo.findAllSorted("newest"))
            model.addAttribute("sort", "newest")
            return "search"
        }
        model.addAttribute("product", product)
        return "edit"
    }

    @PostMapping("/products/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestParam title: String,
        @RequestParam price: String,
        @RequestParam(required = false) variants: String?,
        redirectAttributes: RedirectAttributes
    ): String {
        val priceBd = try { price.toBigDecimal() } catch (e: Exception) { null }
        if (priceBd == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid price value")
            return "redirect:/products/$id/edit"
        }
        repo.update(id, title, priceBd, variants)
        redirectAttributes.addFlashAttribute("message", "Product updated successfully")
        return "redirect:/search"
    }

    @DeleteMapping("/products/{id}")
    fun deleteProduct(@PathVariable id: Long, model: Model): String {
        repo.deleteById(id)
        model.addAttribute("products", repo.findAllSorted("newest"))
        model.addAttribute("sort", "newest")
        return "fragments/products :: table"
    }
}
