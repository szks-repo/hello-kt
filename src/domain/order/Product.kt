package domain.order

@JvmInline
value class ProductId(val id: String) {
    init {
        require(id.isNotEmpty()) { "Product ID must not be empty" }
    }
}