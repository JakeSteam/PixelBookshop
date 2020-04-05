package uk.co.jakelee.pixelbookshop.ui.shop

import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture
import uk.co.jakelee.pixelbookshop.dto.GameTime
import uk.co.jakelee.pixelbookshop.lookups.Visitor

data class ShopUiUpdate(val time: GameTime, val visitorPosition: List<Pair<Visitor, OwnedFurniture?>>)