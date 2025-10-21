package domain.validation

/**
 * Kotlin 2.x の definitely non-null ジェネリクス (`T & Any`) と同等の安全性を Kotlin/JVM で表現するラッパー。
 * API 側では `T` を非 null とみなせるため、Java プラットフォーム型の検証ゲートに利用できる。
 */
class NonNullList<T : Any> private constructor(
    private val delegate: List<T>
) : List<T> by delegate {

    companion object {
        /**
         * [values] に含まれる `null` を検出し、問題なければ `NonNullList` を返す。
         *
         * @param sourceName 失敗時に出力するエラーメッセージへ埋め込む識別子。
         * @param maxNullReports 診断メッセージとして提示する最大 null インデックス数（冗長さ抑制用）。
         */
        fun <T : Any> fromIterable(
            sourceName: String,
            values: Iterable<T?>,
            maxNullReports: Int = 5
        ): ValidationResult<NonNullList<T>> {
            val nonNullValues = mutableListOf<T>()
            val nullIndices = mutableListOf<Int>()
            var observedNullCount = 0

            values.forEachIndexed { index, element ->
                if (element == null) {
                    observedNullCount += 1
                    if (nullIndices.size < maxNullReports) {
                        nullIndices += index
                    }
                } else {
                    nonNullValues += element
                }
            }

            return if (nullIndices.isEmpty()) {
                ValidationResult.Valid(NonNullList(nonNullValues.toList()))
            } else {
                val diagnostics = mutableListOf(
                    "$sourceName: null value detected at index(es) ${nullIndices.joinToString()}"
                )

                if (observedNullCount > nullIndices.size) {
                    diagnostics += "$sourceName: $observedNullCount total null entr${if (observedNullCount == 1) "y" else "ies"} (showing first ${nullIndices.size})"
                }

                ValidationResult.Invalid(
                    diagnostics
                )
            }
        }

        internal fun <T : Any> trusted(values: List<T>): NonNullList<T> =
            NonNullList(values.toList())
    }

    /**
     * `NonNullList` の内部リストをコピーして返す。
     * 呼び出し側で可変操作をしたい場合に備えて defensive copy としている。
     */
    fun toRegularList(): List<T> = delegate.toList()

    override fun toString(): String = delegate.joinToString(prefix = "[", postfix = "]")
}

fun <T : Any> Iterable<T?>.validatedNonNull(sourceName: String): ValidationResult<NonNullList<T>> =
    NonNullList.fromIterable(sourceName, this)
