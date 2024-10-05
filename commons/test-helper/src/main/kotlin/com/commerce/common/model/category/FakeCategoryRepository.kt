package com.commerce.common.model.category

class FakeCategoryRepository : CategoryRepository {

    var autoIncrementId = 1L
    var data: MutableList<Category> = mutableListOf()

    override fun findAll(): List<Category> {
        initData()
        return data
    }
    fun initData() {
           val 국내도서 = Category(
               id = 1L,
               parentId = 0L,
               name = "국내도서",
               depth = 1,
           )
            val 해외도서 = Category(
                id = 2L,
                parentId = 0L,
                name = "해외도서",
                depth = 1,
            )
        data.add(국내도서)
        data.add(해외도서)
    }
}