package domain.order

interface OrderRepository {
    fun save(order: Order)
    fun findById(id: OrderId): Order?
}

class InMemoryOrderRepository : OrderRepository {
    private val store = mutableMapOf<OrderId, Order>()
    override fun save(order: Order) {
        store[order.id] = order
    }
    override fun findById(id: OrderId): Order? = store[id]
}