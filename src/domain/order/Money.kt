package domain.order

@JvmInline
value class Money(val amount: Int) {
    init {
        require(amount >= 0) { "金額は0以上である必要があります" }
    }

    operator fun plus(other: Money) = Money(this.amount + other.amount)
    operator fun minus(other: Money) = Money(this.amount - other.amount)
    override fun toString() = "¥$amount"
}