package com.example.nayanepal.common

import java.util.concurrent.atomic.AtomicLong
/**
 * Enterprise Monitoring Utility.
 * Provides hooks for Prometheus/Grafana to track high-volume national transactions.
 */
object Monitoring {
    private val transactionCounter = AtomicLong(0)
    private val errorCounter = AtomicLong(0)
    fun incrementTransactions() {
        transactionCounter.incrementAndGet()
    }
    fun incrementErrors() {
        errorCounter.incrementAndGet()
    fun getMetrics(): Map<String, Long> {
        return mapOf(
            "wallet_transactions_total" to transactionCounter.get(),
            "system_errors_total" to errorCounter.get()
        )
}
