package com.capstone.basaliproject.ui.scan.history

class DataModel(var nestedList: List<String>, var itemText: String) {
    var isExpandable: Boolean = false

    init {
        isExpandable = false
    }

    fun customSetExpandable(expandable: Boolean) {
        isExpandable = expandable
    }
}

