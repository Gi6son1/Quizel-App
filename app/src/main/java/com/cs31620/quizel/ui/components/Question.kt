package com.cs31620.quizel.ui.components


class Question(
    questionId: Int = -1,
    _title: String = "",
    _description: String = "",
    _answers: MutableList<Answer> = mutableListOf()
) {
    var questionId = questionId
        get() = field

    var title = _title
        get() = field
        set(value) {
            field = value
        }


    var description = _description
        get() = field
        set(value) {
            field = value
        }

    var answers = _answers
        get() = field
        set(value) {
            field = value
        }

    public fun getNumAnswers() = answers.size

    public fun removeAnswer(answer: Answer){
        answers.remove(answer)
    }

    public fun addAnswer(answer: Answer){
        answers.add(answer)
    }
}