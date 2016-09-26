package be.brickbit.lpm.core.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RandomValueUtil {
    public static String randomString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static String randomString() {
        return randomString(10);
    }

    public static String randomEmail() {
        return randomString(15) + "@mail.com";
    }

    public static int randomInt() {
        return randomInt(0, Integer.MAX_VALUE);
    }

    public static int randomInt(int min, int max) {
        return RandomUtils.nextInt(min, max);
    }

    public static BigDecimal randomDecimal() {
        return BigDecimal.valueOf(RandomUtils.nextDouble(0.0, Double.MAX_VALUE));
    }

    public static long randomLong() {
        return randomLong(0, Long.MAX_VALUE);
    }

    public static long randomLong(long min, long max) {
        return RandomUtils.nextLong(min, max);
    }

    public static LocalDate randomLocalDate() {
        return LocalDate.now().plusDays(randomLong(0, 1024));
    }
}
