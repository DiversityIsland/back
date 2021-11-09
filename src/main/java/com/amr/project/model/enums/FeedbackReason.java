package com.amr.project.model.enums;



public enum FeedbackReason {
    POSITIVE("Благодарность"),
    NEGATIVE("Жалоба"),
    QUESTION("Вопрос"),
    BUG_REPORT("Сообщение об ошибке");

    private final String name;

    FeedbackReason(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
