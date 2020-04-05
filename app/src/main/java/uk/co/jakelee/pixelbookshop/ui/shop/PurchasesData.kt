package uk.co.jakelee.pixelbookshop.ui.shop

import uk.co.jakelee.pixelbookshop.database.entity.PendingPurchase
import uk.co.jakelee.pixelbookshop.dto.GameTime

class PurchasesData {
    var time: GameTime? = null
    var pendingPurchases: List<PendingPurchase>? = null

    fun isValid() = time != null
            && pendingPurchases?.size ?: 0 > 0
}