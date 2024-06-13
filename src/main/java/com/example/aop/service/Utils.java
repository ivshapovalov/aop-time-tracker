package com.example.aop.service;


import org.apache.commons.lang3.ThreadUtils;

import java.time.Duration;

public class Utils {
    private Utils() {
    }

    public static void someBusinessLogic() {
        sleep(Duration.ofMillis(200));
    }

    public static void sleep(Duration duration) {
        try {
            ThreadUtils.sleep(duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
