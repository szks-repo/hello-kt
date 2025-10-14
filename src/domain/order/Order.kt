package domain.order

class Order(
    val id: OrderId,
    val products: List<ProductId>,
    val price: Money,
    var state: OrderState = OrderState.Pending,
) {
    fun total(): Money = price

    fun pay() {
        require(state is OrderState.Pending) {
            "支払い可能なのはPending状態のみです"
        }
        state = OrderState.Paid
    }

    fun ship() {
        require(state is OrderState.Paid) {
            "出荷可能なのはPaid状態のみです"
        }
        state = OrderState.Shipped
    }
}