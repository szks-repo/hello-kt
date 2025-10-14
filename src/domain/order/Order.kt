package domain.order

class Order(
    val id: OrderId,
    val products: List<ProductId>,
    val price: Money
) {
    fun total(): Money = price
}