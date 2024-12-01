package com.cs31620.quizel.ui.components

class Question(questionId: Int) {
    var id = questionId
        get() = field

    var title: String = ""
        get() = field
        set(value) {
            field = value
        }


    var description: String = ""
        get() = field
        set(value) {
            field = value
        }

    var answers: MutableList<Answer> = mutableListOf()
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