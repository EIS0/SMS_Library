package com.eis0;

import android.content.Context;
import android.content.SharedPreferences;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContextMockOfPreferences {

    private static Context mockContext = mock(Context.class);
    private static SharedPreferences mockPref = mock(SharedPreferences.class);
    private static SharedPreferences.Editor mockEditor = mock(SharedPreferences.Editor.class);

    public static Context setupMocks(){
        when(mockContext.getPackageName()).thenReturn("mock");
        when(mockContext.getSharedPreferences(eq("mock_preferences"),
                any(Integer.class))).thenReturn(mockPref);
        when(mockPref.edit()).thenReturn(mockEditor);
        when(mockEditor.commit()).thenReturn(true);
        return mockContext;
    }

}
