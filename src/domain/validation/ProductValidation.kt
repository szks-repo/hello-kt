package domain.validation

import domain.order.ProductId

/**
 * Java プラットフォーム型から入ってきた商品コードを `ProductId` へマップする検証パイプライン。
 */
fun NonNullList<String>.toProductIds(sourceName: String = "productCodes"): ValidationResult<NonNullList<ProductId>> {
    val errors = mutableListOf<String>()
    val productIds = mutableListOf<ProductId>()

    for ((index, code) in this.withIndex()) {
        val trimmed = code.trim()
        if (trimmed.isEmpty()) {
            errors += "$sourceName[$index]: must not be blank"
            continue
        }

        try {
            val productId = ProductId(trimmed)
            productIds += productId
        } catch (ex: IllegalArgumentException) {
            errors += "$sourceName[$index]: ${ex.message}"
        }
    }

    return if (errors.isEmpty()) {
        ValidationResult.Valid(NonNullList.trusted(productIds))
    } else {
        ValidationResult.Invalid(errors)
    }
}
