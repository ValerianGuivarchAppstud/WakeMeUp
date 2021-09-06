package com.vguivarc.wakemeup.transport.settings

import android.view.View
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.util.adapters.CustomBaseAdapter
import kotlinx.android.synthetic.main.item_settings.view.*

class SettingsAdapter(
    settingsRubricList: List<SettingsRubric>,
    onItemClick: (SettingsRubric) -> Unit
) : CustomBaseAdapter<SettingsRubric>(
    settingsRubricList,
    R.layout.item_settings,
    onItemClick
) {
    init {
        onBinding = { view: View, item: SettingsRubric ->
            view.itemSettingsTitle.text = view.context.getString(item.titleResource)
        }
    }
}
