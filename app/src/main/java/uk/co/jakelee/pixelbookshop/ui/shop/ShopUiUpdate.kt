package uk.co.jakelee.pixelbookshop.ui.shop

import uk.co.jakelee.pixelbookshop.database.dao.PlayerDao

data class ShopUiUpdate(val time: PlayerDao.GameTime, val visitorPosition: List<Pair<Int, Int>>)