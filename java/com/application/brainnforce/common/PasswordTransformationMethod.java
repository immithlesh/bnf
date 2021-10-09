package com.application.brainnforce.common;

import android.view.View;

import org.jetbrains.annotations.NotNull;

public class PasswordTransformationMethod extends android.text.method.PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private static class PasswordCharSequence implements CharSequence {
        private final CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }

        public char charAt(int index) {
            return '*'; // This is the important part
        }

        public int length() {
            return mSource.length(); // Return default
        }

        @NotNull
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }
}
