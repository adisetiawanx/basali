package com.capstone.basaliproject.ui.scan.history

class DataModel(var nestedList: List<String>, var itemMonthText: String, var itemYearText: String) {
    var isExpandable: Boolean = false

    init {
        isExpandable = false
    }

    fun customSetExpandable(expandable: Boolean) {
        isExpandable = expandable
    }
}

