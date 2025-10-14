import domain.order.*

fun main() {
    val name = "Kotlin"
    println("Hello, $name!")

    for (i in 1..5) {
        println("i = $i")
    }

    val repo = InMemoryOrderRepository()

    val order = Order(
        id = OrderId(),
        products = listOf(ProductId("001"), ProductId("002")),
        price = Money(120000)
    )

    repo.save(order)

    val found = repo.findById(order.id)
    println("見つかった注文: $found")
}
