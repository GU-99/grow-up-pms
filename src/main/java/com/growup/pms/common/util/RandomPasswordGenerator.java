package com.growup.pms.common.util;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RandomPasswordGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static String generatePassword(int length, PasswordOptions options) {
        Map<CharacterType, Boolean> charTypeInclusion = createCharTypeInclusionMap(options);

        validatePasswordPolicy(length, options);

        StringBuilder charPool = createCharPool(charTypeInclusion);
        String initialPassword = createInitialPassword(charTypeInclusion);
        String fullPassword = completePassword(initialPassword, length, charPool);
        return shuffleString(fullPassword);
    }

    private static Map<CharacterType, Boolean> createCharTypeInclusionMap(PasswordOptions options) {
        Map<CharacterType, Boolean> charTypeInclusion = new EnumMap<>(CharacterType.class);
        charTypeInclusion.put(CharacterType.LOWER, options.includeLower());
        charTypeInclusion.put(CharacterType.UPPER, options.includeUpper());
        charTypeInclusion.put(CharacterType.DIGITS, options.includeDigits());
        charTypeInclusion.put(CharacterType.SPECIAL, options.includeSpecial());
        return charTypeInclusion;
    }

    private static void validatePasswordPolicy(int length, PasswordOptions options) {
        if (length < 8 || length > 16) {
            throw new IllegalArgumentException("비밀번호 길이는 8 이상 16 이하여야 합니다.");
        }

        if (!(options.includeLower() || options.includeUpper() || options.includeDigits() || options.includeSpecial())) {
            throw new IllegalArgumentException("최소한 하나의 문자 유형은 포함되어야 합니다.");
        }
    }

    private static StringBuilder createCharPool(Map<CharacterType, Boolean> charTypeInclusion) {
        StringBuilder charPool = new StringBuilder();
        for (Map.Entry<CharacterType, Boolean> entry : charTypeInclusion.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                charPool.append(entry.getKey().getCharacters());
            }
        }
        return charPool;
    }

    private static String createInitialPassword(Map<CharacterType, Boolean> charTypeInclusion) {
        StringBuilder initialPassword = new StringBuilder();
        for (Map.Entry<CharacterType, Boolean> entry : charTypeInclusion.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                String chars = entry.getKey().getCharacters();
                initialPassword.append(chars.charAt(random.nextInt(chars.length())));
            }
        }
        return initialPassword.toString();
    }

    private static String completePassword(String initialPassword, int length, StringBuilder charPool) {
        StringBuilder password = new StringBuilder(initialPassword);
        while (password.length() < length) {
            password.append(charPool.charAt(random.nextInt(charPool.length())));
        }
        return password.toString();
    }

    private static String shuffleString(String input) {
        List<Character> characters = input.chars()
                .mapToObj(ch -> (char) ch)
                .collect(Collectors.toList());
        Collections.shuffle(characters, random);
        return characters.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    @Getter
    @Builder
    public record PasswordOptions(
            boolean includeLower,
            boolean includeUpper,
            boolean includeDigits,
            boolean includeSpecial
    ) {
    }

    @Getter
    public enum CharacterType {
        LOWER("abcdefghijklmnopqrstuvwxyz"),
        UPPER("ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
        DIGITS("0123456789"),
        SPECIAL("!@#$%^&*()_+-=[]{}|;:,.<>?");

        private final String characters;

        CharacterType(String characters) {
            this.characters = characters;
        }
    }
}
