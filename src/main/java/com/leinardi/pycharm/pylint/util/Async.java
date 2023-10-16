/*
 * Copyright 2023 Roberto Leinardi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leinardi.pycharm.pylint.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public final class Async {
    private static final Logger LOG = Logger.getInstance(Async.class);

    private static final int FIFTY_MS = 50;

    private Async() {
    }

    @Nullable
    public static <T> T asyncResultOf(@NotNull final Callable<T> callable,
                                      @Nullable final T defaultValue,
                                      final long timeoutInMs) {
        try {
            return whenFinished(executeOnPooledThread(callable), timeoutInMs).get();

        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static <T> Future<T> executeOnPooledThread(final Callable<T> callable) {
        return ApplicationManager.getApplication().executeOnPooledThread(callable);
    }

    public static <T> Future<T> whenFinished(final Future<T> future,
                                             final long timeoutInMs) {
        long elapsedTime = 0;
        while (!future.isDone() && !future.isCancelled()) {
            ProgressManager.checkCanceled();
            elapsedTime += waitFor(FIFTY_MS);

            if (timeoutInMs > 0 && elapsedTime >= timeoutInMs) {
                LOG.debug("Async task exhausted timeout of " + timeoutInMs + "ms, cancelling.");
                future.cancel(true);
                throw new ProcessCanceledException();
            }
        }
        return future;
    }

    private static long waitFor(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
        return millis;
    }
}
