package cobo.writing.repository.custom

import cobo.writing.data.entity.Writing

interface WritingRepositoryCustom {

    fun ifExistsUpdateElseInsert(writing: Writing): Int
}