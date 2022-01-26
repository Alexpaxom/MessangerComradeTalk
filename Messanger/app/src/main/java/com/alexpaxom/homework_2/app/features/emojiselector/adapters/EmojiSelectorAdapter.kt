package com.alexpaxom.homework_2.app.features.emojiselector.adapters

import com.alexpaxom.homework_2.app.features.baseelements.adapters.BaseDiffUtilAdapter
import com.alexpaxom.homework_2.app.features.baseelements.adapters.BaseHolderFactory
import com.alexpaxom.homework_2.data.models.ReactionItem

class EmojiSelectorAdapter(
    holdersFactory: BaseHolderFactory
): BaseDiffUtilAdapter<ReactionItem>(holdersFactory)