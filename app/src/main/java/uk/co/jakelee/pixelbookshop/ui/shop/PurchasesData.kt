package uk.co.jakelee.pixelbookshop.ui.shop

import uk.co.jakelee.pixelbookshop.database.dao.PlayerDao
import uk.co.jakelee.pixelbookshop.database.entity.PendingPurchase

class PurchasesData {
    var time: PlayerDao.GameTime? = null
    var pendingPurchases: List<PendingPurchase>? = null

    fun isValid() = time != null
            && pendingPurchases?.size ?: 0 > 0
}