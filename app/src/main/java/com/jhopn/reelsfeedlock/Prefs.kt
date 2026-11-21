package com.jhopn.reelsfeedlock

import android.content.Context

/** Opções do usuário, salvas no próprio celular. */
object Prefs {
    private const val FILE = "reelsfeedlock"
    private const val KEY_BLOCK_REELS = "block_reels"
    private const val KEY_ALLOW_FROM_DIRECT = "allow_from_direct"

    private fun prefs(context: Context) =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    /** true = Reels bloqueados. false = Reels liberados (o serviço não faz nada). */
    fun isBlockReels(context: Context) = prefs(context).getBoolean(KEY_BLOCK_REELS, true)
    fun setBlockReels(context: Context, value: Boolean) =
        prefs(context).edit().putBoolean(KEY_BLOCK_REELS, value).apply()

    /** true = Reels abertos a partir de uma conversa do Direct ficam liberados. */
    fun isAllowFromDirect(context: Context) = prefs(context).getBoolean(KEY_ALLOW_FROM_DIRECT, true)
    fun setAllowFromDirect(context: Context, value: Boolean) =
        prefs(context).edit().putBoolean(KEY_ALLOW_FROM_DIRECT, value).apply()
}
