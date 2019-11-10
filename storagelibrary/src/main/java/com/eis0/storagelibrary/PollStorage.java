package com.eis0.storagelibrary;

import android.content.Context;
    public interface PollStorage <T extends TernaryPoll>{
        String setFileName(T poll);
        String fromPollToJson(T ternaryPoll);
        T fromJsonToPoll(String content);
        void saveJsonToInternal(Context context, String fileName, String json);
        String loadJsonFromInternal(Context context, String fileName);
    }


