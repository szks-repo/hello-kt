import domain.order.*

fun main() {
    val name = "Kotlin"
    println("Hello, $name!")

    for (i in 1..5) {
        println("i = $i")
    }

    val repo = InMemoryOrderRepository()

    val order = Order.create(
        id = OrderId(),
        products = listOf(ProductId("001"), ProductId("002")),
        price = Money(120000)
    )

    repo.save(order)

    val pendingOrders: List<Order<OrderState.Pending>> = repo.findAllPending()
    println(pendingOrders)

    for (po in pendingOrders) {
        println("Pending: ${po.state.labelJa()}")

        val paid = po.pay()
        println("Payed ${paid.state.labelJa()}")

        val shipped = paid.ship()
        println("Shipping ${shipped.state.labelJa()}")
    }
}
