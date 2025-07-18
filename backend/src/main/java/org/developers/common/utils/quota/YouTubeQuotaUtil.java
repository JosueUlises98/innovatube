package org.developers.common.utils.quota;

import java.util.concurrent.atomic.AtomicInteger;

public class YouTubeQuotaUtil {
    private static final AtomicInteger quotaUsed = new AtomicInteger(0);
    private static final int DAILY_QUOTA_LIMIT = 10000;

    public static boolean checkQuotaAvailable() {
        return quotaUsed.get() < 10000;
    }

    public static void incrementQuota(int cost) {
        quotaUsed.addAndGet(cost);
    }

    public static void resetQuota() {
        quotaUsed.set(0);
    }
}
