package br.com.itau.challenge.balance.utils;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class TimeUtil {
  private TimeUtil() {}

  public static OffsetDateTime microsToOffsetDateTime(long micros) {
    long seconds = micros / 1_000_000L;
    long microsRemainder = micros % 1_000_000L;
    Instant instant = Instant.ofEpochSecond(seconds, microsRemainder * 1_000L);
    return instant.atOffset(ZoneOffset.UTC);
  }
}
