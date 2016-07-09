/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.obsessive.library.utils;

import com.orhanobut.logger.Logger;

/**
 * Author:  Tau.Chen
 * Email:   1076559197@qq.com | tauchen1990@gmail.com
 * Date:    2015/3/10.
 * Description: Log utils
 */
public class TLog {
    /**
     * This flag to indicate the log is enabled or disabled.
     */
    private static boolean isLogEnable = true;

    /**
     * Disable the log output.
     */
    public static void disableLog() {
        isLogEnable = false;
    }

    /**
     * Enable the log output.
     */
    public static void enableLog() {
        isLogEnable = true;
    }

    /**
     * Debug
     *
     * @param msg
     */
    public static void d(String msg) {
        if (isLogEnable) {
            Logger.d(msg);
        }
    }

    /**
     * Information
     *
     * @param msg
     */
    public static void i(String msg) {
        if (isLogEnable) {
            Logger.i(msg);
        }
    }

    /**
     * Verbose
     *
     * @param msg
     */
    public static void v(String msg) {
        if (isLogEnable) {
            Logger.v(msg);
        }
    }

    /**
     * Warning
     *
     * @param msg
     */
    public static void w(String msg) {
        if (isLogEnable) {
            Logger.w(msg);
        }
    }

    /**
     * Error
     *
     * @param msg
     */
    public static void e(String msg) {
        if (isLogEnable) {
            Logger.e(msg);
        }
    }
}
