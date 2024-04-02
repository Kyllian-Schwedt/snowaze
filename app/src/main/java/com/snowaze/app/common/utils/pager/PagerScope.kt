package com.snowaze.app.common.utils.pager

class PagerScope(
    private val state: PagerState,
    val commingPage: Int
) {
    /**
     * Returns the current selected page
     */
    val currentPage: Int
        get() = state.currentPage

    /**
     * Returns the current selected page offset
     */
    val currentPageOffset: Float
        get() = state.currentPageOffset

    /**
     * Returns the current selection state
     */
    val selectionState: SelectionState
        get() = state.selectionState
}