package io.github.ccch1mneyyy.dsh_app.phone

import org.junit.Assert.*
import org.junit.Test

class ScreenWatchTest {
    @Test fun watchDurationIsBoundedToFiveFrames() {
        assertEquals(1, ScreenWatch.frameCount(0))
        assertEquals(2, ScreenWatch.frameCount(1))
        assertEquals(3, ScreenWatch.frameCount(2500))
        assertEquals(5, ScreenWatch.frameCount(5000))
        assertEquals(5, ScreenWatch.frameCount(9000))
        assertEquals(1L, ScreenWatch.intervalMs(1, 2))
    }

    @Test fun diffIgnoresChangingHeaderAndScreenshotTimestamp() {
        val before = "s1 · 微信\\n[1]按钮：聊天\\n截图时间：22:00:01"
        val same = "s2 · 微信\\n[1]按钮：聊天\\n截图时间：22:00:02"
        assertNull(ScreenWatch.diff(before, same))
        val changed = "s3 · 微信\\n[1]按钮：聊天\\n[2]文本：新消息\\n截图时间：22:00:03"
        assertEquals("新增：[2]文本：新消息", ScreenWatch.diff(before, changed))
    }
}
