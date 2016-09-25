package com.grietenenknapen.sithandroid.maingame.usecasedata;

public class BobaFettData {
    private boolean answerYes = false;
    private long selectedUserId = 0;

    public boolean isAnswerYes() {
        return answerYes;
    }

    public void setAnswerYes(boolean answerYes) {
        this.answerYes = answerYes;
    }

    public long getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(long selectedUserId) {
        this.selectedUserId = selectedUserId;
    }
}
