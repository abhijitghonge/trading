package com.ag.trading.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class Utils {

    public static Double parseDouble(String value) {
        String trimmed = StringUtils.trimToEmpty(value);

        return StringUtils.isEmpty(trimmed) ? Double.NaN : Double.valueOf(trimmed);
    }

    public static BigDecimal parseBigDecimal(String value){
        String trimmed = StringUtils.trimToNull(value);
        return (trimmed==null)?BigDecimal.ZERO:new BigDecimal(trimmed);

    }
}
