import domain.order.*
import domain.validation.flatMap
import domain.validation.onInvalid
import domain.validation.onValid
import domain.validation.toProductIds
import domain.validation.validatedNonNull
import legacy.LegacyApi

fun main() {
    println("== Order lifecycle sample ==")
    demoOrderLifecycle()

    println()
    println("== Definitely-non-null validation sample ==")
    demoLegacyInterop()
}

private fun demoOrderLifecycle() {
    val repo = InMemoryOrderRepository()

    val order = Order.create(
        id = OrderId(),
        products = listOf(ProductId("001"), ProductId("002")),
        price = Money(120_000)
    )

    repo.save(order)

    val pendingOrders: List<Order<OrderState.Pending>> = repo.findAllPending()
    println("Pending orders: $pendingOrders")

    for (pending in pendingOrders) {
        println("Pending → ${pending.state.labelJa()}")

        val paid = pending.pay()
        println("Paid → ${paid.state.labelJa()}")

        val shipped = paid.ship()
        println("Shipped → ${shipped.state.labelJa()}")
    }
}

private fun demoLegacyInterop() {
    val rawCodesFromLegacy = LegacyApi.fetchProductCodes()
    println("Legacy platform codes (List<String!>): $rawCodesFromLegacy")

    val legacyResult = rawCodesFromLegacy
        .validatedNonNull("legacy product codes")
        .flatMap { codes -> codes.toProductIds("legacy product codes") }

    legacyResult
        .onValid { validated ->
            println("Unexpected success, codes were sanitized: ${validated.toRegularList()}")
        }
        .onInvalid { errors ->
            println("Validation errors from legacy data:")
            errors.forEach { println(" - $it") }
        }

    val sanitizedCodes = LegacyApi.fetchSanitizedProductCodes()
    println("Sanitized codes (List<String!>): $sanitizedCodes")

    val sanitizedResult = sanitizedCodes
        .validatedNonNull("sanitized product codes")
        .flatMap { codes -> codes.toProductIds("sanitized product codes") }

    sanitizedResult
        .onInvalid { errors ->
            println("Unexpected validation failure:")
            errors.forEach { println(" - $it") }
        }
        .onValid { validated ->
            println("Validated product ids (NonNullList): $validated")

            val repo = InMemoryOrderRepository()
            val order = Order.create(
                id = OrderId(),
                products = validated.toRegularList(),
                price = Money(48_900)
            )
            repo.save(order)

            println("Stored order from sanitized input: $order")
        }
}
