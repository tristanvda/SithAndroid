package com.grietenenknapen.sithandroid.ui.presenter;


    public interface PresenterFactory<T extends Presenter> {

        /**
         * Create a new instance of a Presenter
         *
         * @return The Presenter instance
         */
         T createPresenter();
    }

