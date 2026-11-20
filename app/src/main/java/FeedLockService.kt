package com.caasi.feedlock

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Versão modificada: bloqueia SÓ os Reels.
 * Feed, Stories, Direct, perfil e Explorar continuam liberados.
 * Reels abertos a partir de uma conversa do Direct também são liberados.
 * Ao detectar Reels, o app manda para a tela inicial do celular.
 *
 * Em vez de uma lista de telas permitidas (como o original), usa uma lista de
 * telas bloqueadas. A detecção é feita por view ID, que não depende do idioma
 * do celular (o original procurava textos em inglês, como "Audio call").
 */
class FeedLockService : AccessibilityService() {

    companion object {
        private const val TAG = "FeedLock"
        private const val IG = "com.instagram.android"

        // Containers do player de Reels (aba Reels e Reel aberto a partir do feed).
        // Se o Instagram mudar os IDs, ative DEBUG_DUMP_IDS e veja no Logcat.
        private val REELS_VIEW_IDS = listOf(
            "$IG:id/clips_viewer_view_pager",
            "$IG:id/clips_viewer_container",
            "$IG:id/clips_video_container",
            "$IG:id/clips_ufi_component"
        )

        // Botão da aba Reels na barra inferior
        private const val REELS_TAB_ID = "$IG:id/clips_tab"

        // Barra inferior de abas. Se ela está visível, você não está numa conversa.
        private val TAB_BAR_IDS = listOf(
            "$IG:id/tab_bar",
            "$IG:id/feed_tab"
        )

        // Elementos de uma conversa do Direct (campo de mensagem / lista de mensagens).
        // Reels abertos depois de passar por aqui são liberados.
        private val DIRECT_THREAD_IDS = listOf(
            "$IG:id/row_thread_composer_edittext",
            "$IG:id/row_thread_composer_container",
            "$IG:id/direct_thread_header",
            "$IG:id/message_list"
        )

        // Intervalo mínimo entre duas ações de bloqueio, pra não ficar em loop
        private const val ACTION_COOLDOWN_MS = 800L
        private const val DEBOUNCE_MS = 100L

        // true = escreve no Logcat todos os view IDs da tela (pra ajustar a lista acima)
        private const val DEBUG_DUMP_IDS = false
    }

    private var lastAnalyze = 0L
    private var lastAction = 0L

    // true depois que uma conversa do Direct apareceu na tela; volta a false
    // quando a barra de abas reaparece (você saiu da conversa).
    private var cameFromDirect = false

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.packageName?.toString() != IG) return
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        ) return

        val now = System.currentTimeMillis()
        if (now - lastAnalyze < DEBOUNCE_MS) return
        lastAnalyze = now
        if (now - lastAction < ACTION_COOLDOWN_MS) return

        val root = rootInActiveWindow ?: return
        // Se a janela em primeiro plano não é mais o Instagram (você saiu do app),
        // não faz nada. Evita agir com eventos atrasados depois de minimizar.
        if (root.packageName?.toString() != IG) {
            root.recycle()
            return
        }
        try {
            if (DEBUG_DUMP_IDS) dumpIds(root)

            if (anyVisible(root, DIRECT_THREAD_IDS)) {
                if (!cameFromDirect) Log.d(TAG, "Conversa do Direct aberta, Reels liberados")
                cameFromDirect = true
            }

            // Aba Reels selecionada: sempre bloqueia
            if (isReelsTabSelected(root)) {
                cameFromDirect = false
                block()
                return
            }

            if (anyVisible(root, TAB_BAR_IDS)) cameFromDirect = false

            if (anyVisible(root, REELS_VIEW_IDS) && !cameFromDirect) block()
        } finally {
            root.recycle()
        }
    }

    private fun isReelsTabSelected(root: AccessibilityNodeInfo): Boolean {
        val nodes = root.findAccessibilityNodeInfosByViewId(REELS_TAB_ID)
        val selected = nodes.any { it.isSelected }
        nodes.forEach { it.recycle() }
        return selected
    }

    private fun anyVisible(root: AccessibilityNodeInfo, ids: List<String>): Boolean {
        for (id in ids) {
            val nodes = root.findAccessibilityNodeInfosByViewId(id)
            val visible = nodes.any { it.isVisibleToUser }
            nodes.forEach { it.recycle() }
            if (visible) return true
        }
        return false
    }

    // Sai do Instagram e vai para a tela inicial do celular
    private fun block() {
        lastAction = System.currentTimeMillis()
        Log.d(TAG, "Reels detectado, indo para a tela inicial")
        performGlobalAction(GLOBAL_ACTION_HOME)
    }

    private fun dumpIds(node: AccessibilityNodeInfo, depth: Int = 0) {
        node.viewIdResourceName?.let { Log.d("$TAG-ids", "  ".repeat(depth) + it) }
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            dumpIds(child, depth + 1)
            child.recycle()
        }
    }

    override fun onInterrupt() {}
}