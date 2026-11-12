package io.github.ccch1mneyyy.dsh_app.phone

/** Bounded, on-demand screen sampling. No background capture survives the MCP call. */
internal object ScreenWatch {
    const val MAX_DURATION_MS = 5_000
    private const val SAMPLE_INTERVAL_MS = 1_500

    fun frameCount(durationMs: Int): Int = if (durationMs <= 0) 1 else
        1 + ((durationMs + SAMPLE_INTERVAL_MS - 1) / SAMPLE_INTERVAL_MS).coerceIn(1, 4)

    fun intervalMs(durationMs: Int, frames: Int): Long =
        if (frames <= 1) 0L else (durationMs.toLong() / (frames - 1)).coerceAtLeast(1L)

    /** Compare stable screen text, ignoring the per-capture header and timestamp metadata. */
    fun diff(previous: String, current: String): String? {
        fun stableLines(value: String) = value.lineSequence().drop(1)
            .map(String::trim)
            .filter { it.isNotEmpty() && !it.startsWith("截图时间：") }
            .toCollection(LinkedHashSet())

        val before = stableLines(previous)
        val after = stableLines(current)
        val added = after.filterNot(before::contains).take(8)
        val removed = before.filterNot(after::contains).take(8)
        if (added.isEmpty() && removed.isEmpty()) return null
        return buildString {
            if (added.isNotEmpty()) append("新增：").append(added.joinToString("；") { it.take(140) })
            if (removed.isNotEmpty()) {
                if (isNotEmpty()) append("\\n")
                append("消失：").append(removed.joinToString("；") { it.take(140) })
            }
        }.take(1_400)
    }
}
