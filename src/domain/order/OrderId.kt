package domain.order

import java.util.UUID

data class OrderId(val value: UUID = UUID.randomUUID())