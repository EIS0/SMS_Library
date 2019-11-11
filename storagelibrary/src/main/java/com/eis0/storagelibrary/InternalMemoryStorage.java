package com.eis0.storagelibrary;

import android.content.Context;
    public interface InternalMemoryStorage <T> {
        void saveJsonToInternal(Context context, String fileName, String json);
        String loadJsonFromInternal(Context context, String fileName);
        void deleteInternalFile(Context context, String fileName);
    }


