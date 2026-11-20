package com.genius.snapscribe.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import androidx.core.net.toUri

fun shareText(
    context: Context,
    title: String,
    text: String,
): Boolean = runCatching {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val chooser = Intent.createChooser(sendIntent, title).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(chooser)
}.isSuccess

fun openMaps(
    context: Context,
    address: String,
): Boolean = runCatching {
    val uri = "geo:0,0?q=${Uri.encode(address)}".toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}.isSuccess

fun openUrl(
    context: Context,
    rawUrl: String,
): Boolean = runCatching {
    val normalized = if (rawUrl.startsWith("http://") || rawUrl.startsWith("https://")) {
        rawUrl
    } else {
        "https://$rawUrl"
    }
    val intent = Intent(Intent.ACTION_VIEW, normalized.toUri()).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}.isSuccess

fun addCalendarEvent(
    context: Context,
    title: String,
    description: String,
): Boolean = runCatching {
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, title)
        putExtra(CalendarContract.Events.DESCRIPTION, description)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}.isSuccess
