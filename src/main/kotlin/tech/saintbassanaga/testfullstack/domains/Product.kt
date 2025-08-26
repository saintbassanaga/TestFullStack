package tech.saintbassanaga.testfullstack.domains

/**
 * @author saintbassanaga
 * @date 8/26/25
 * @project TestFullStack
 * @file Product
 * @description [Insert description here]
 */

import org.springframework.boot.autoconfigure.domain.EntityScan
import java.math.BigDecimal
import java.time.LocalDateTime

data class Product(
    val id: Long? = null,
    val title: String,
    val price: BigDecimal,
    val variants: String? = null,
    val createdAt: LocalDateTime? = null
)
