package com.grietenenknapen.sithandroid.service.storage.tools;


import android.os.AsyncTask;


public final class StorageBackgroundWorker {

    private StorageBackgroundWorker() {
    }

    public static <T> void executeTask(BackgroundExecutionFactory<T> backgroundExecution) {
        DownloadFilesTask<T> downloadFilesTask = new DownloadFilesTask<T>(backgroundExecution);
        downloadFilesTask.execute();
    }

    private static class DownloadFilesTask<S> extends AsyncTask<Void, Void, S> {
        private BackgroundExecutionFactory<S> backgroundExecution;

        public DownloadFilesTask(BackgroundExecutionFactory<S> backgroundExecution) {
            this.backgroundExecution = backgroundExecution;
        }

        @Override
        protected S doInBackground(Void... voids) {
            return backgroundExecution.onExecute();
        }


        protected void onPostExecute(S result) {
            backgroundExecution.postExecute(result);
        }

    }

    public interface BackgroundExecutionFactory<U> {

        U onExecute();

        void postExecute(U response);

    }
}
