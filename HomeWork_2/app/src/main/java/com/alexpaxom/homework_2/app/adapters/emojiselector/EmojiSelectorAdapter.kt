package com.alexpaxom.homework_2.app.adapters.emojiselector

import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseDiffUtilAdapter
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseHolderFactory
import com.alexpaxom.homework_2.data.models.ReactionItem

class EmojiSelectorAdapter(
    holdersFactory: BaseHolderFactory
): BaseDiffUtilAdapter<ReactionItem>(holdersFactory)