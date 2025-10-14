package domain.order

sealed class OrderState {
    data object Pending : OrderState()
    data object Paid : OrderState()
    data object Shipped : OrderState()

    fun labelJa(): String = when (this) {
        Pending -> "支払い待ち"
        Paid    -> "出荷準備中"
        Shipped -> "発送済み"
    }
}