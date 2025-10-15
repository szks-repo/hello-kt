package domain.order

data class Order<S : OrderState>(
    val id: OrderId,
    val products: List<ProductId>,
    val price: Money,
    val state: S,
) {
    fun total(): Money = price

    companion object {
        fun create(
            id: OrderId,
            products: List<ProductId>,
            price: Money
        ): Order<OrderState.Pending> =
            Order(id, products, price, OrderState.Pending)
    }
}

fun Order<OrderState.Pending>.pay(): Order<OrderState.Paid> =
    Order(id = id, products = products, price = price, state = OrderState.Paid)

// Paid → Shipped
fun Order<OrderState.Paid>.ship(): Order<OrderState.Shipped> =
    Order(id = id, products = products, price = price, state = OrderState.Shipped)