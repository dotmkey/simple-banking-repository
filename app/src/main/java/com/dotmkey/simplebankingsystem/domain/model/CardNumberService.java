package com.dotmkey.simplebankingsystem.domain.model;

import java.util.Arrays;
import java.util.Random;

public class CardNumberService {

    public String generate() {
        Random random = new Random();
        String withoutCheckSum = "400000"
            .concat(String.valueOf(random.nextInt(999999999 - 100000000 + 1) + 100000000));

        int sum = 0;
        for (int i = 0; i < withoutCheckSum.length(); i++) {
            int digit = Integer.parseInt("" + withoutCheckSum.charAt(i));
            if (i % 2 == 0) {
                digit *= 2;
                digit = digit > 9 ? (digit / 10) + (digit % 10) : digit;
            }
            sum += digit;
        }
        int mod = sum % 10;

        return withoutCheckSum.concat(String.valueOf(mod == 0 ? 0 : 10 - mod));
    }

    public boolean isValid(String cardNumber) {
        if (cardNumber.length() < 16) {
            return false;
        }

        int[] digits = new int[cardNumber.length()];

        for (int i = 0; i < cardNumber.length(); i++) {
            digits[i] = Integer.parseInt("" + cardNumber.charAt(i));
        }

        for (int i = digits.length - 2; i >= 0; i -= 2) {
            int num = digits[i] * 2;
            digits[i] = num > 9 ? num % 10 + num / 10 : num;
        }

        int sum = Arrays.stream(digits).sum();

        return sum % 10 == 0;
    }
}
