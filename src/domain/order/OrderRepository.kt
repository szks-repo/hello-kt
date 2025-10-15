package domain.order

import kotlin.reflect.KClass

interface OrderRepository {
    // 保存時に「このOrderの状態型」を一緒に渡す
    fun <S : OrderState> save(order: Order<S>, state: KClass<S>)

    fun findById(id: OrderId): Order<out OrderState>?

    fun findAllPending(): List<Order<OrderState.Pending>>

}

class InMemoryOrderRepository : OrderRepository {
    // 状態タグを一緒に保持する内部エントリ
    private data class Entry(
        val order: Order<out OrderState>,
        val state: KClass<out OrderState>
    )

    private val store = mutableMapOf<OrderId, Entry>()

    override fun <S : OrderState> save(order: Order<S>, state: KClass<S>) {
        store[order.id] = Entry(order, state)
    }

    override fun findById(id: OrderId): Order<out OrderState>? =
        store[id]?.order

    override fun findAllPending(): List<Order<OrderState.Pending>> {
        return store.values
            .asSequence()
            .filter { it.state == OrderState.Pending::class }
            .map { entry ->
                @Suppress("UNCHECKED_CAST")
                entry.order as Order<OrderState.Pending>
            }
            .toList()
    }
}

inline fun <reified S : OrderState> OrderRepository.save(order: Order<S>) =
    save(order, S::class)